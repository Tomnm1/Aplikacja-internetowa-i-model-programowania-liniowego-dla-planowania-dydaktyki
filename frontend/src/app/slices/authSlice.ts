import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { API_ENDPOINTS } from '../urls';
import { BackendTeacher } from '../../utils/Interfaces';
import jwtDecode from 'jwt-decode';
import { fetchWithAuth } from "../fetchWithAuth";

interface AuthState {
    isAuthenticated: boolean;
    userId: string | null;
    role: 'admin' | 'user' | null;
    user: BackendTeacher | null;
    loading: boolean;
    error: string | null;
}

const initialState: AuthState = {
    isAuthenticated: false,
    userId: null,
    role: null,
    user: null,
    loading: false,
    error: null,
};

interface DecodedToken {
    uid: string;
    gnm: string;
    snm: string;
}

const storedAuth = localStorage.getItem('auth');
const storedToken = localStorage.getItem('access_token');
const parsedAuth: AuthState | null = storedAuth ? JSON.parse(storedAuth) : null;

export const loginUser = createAsyncThunk<
    { role: 'admin' | 'user'; userId: string; user?: BackendTeacher },
    string,
    { rejectValue: string }
>(
    'auth/loginUser',
    async (accessToken: string, { rejectWithValue }) => {
        try {
            const decoded: DecodedToken = jwtDecode(accessToken);
            const userId = decoded.uid;

            const response = await fetchWithAuth(`${API_ENDPOINTS.TEACHERS}/${userId}`, {
                headers: {
                    Authorization: `Bearer ${accessToken}`,
                },
            });

            if (response.status === 401) {
                throw new Error('Użytkownik nie był zarejestrowany. Proszę zalogować się ponownie.');
            }

            if (!response.ok) {
                throw new Error('Nie znaleziono użytkownika');
            }

            const data: BackendTeacher = await response.json();

            const role: 'admin' | 'user' = data.isAdmin ? 'admin' : 'user';

            return { role, userId, user: data };
        } catch (error: any) {
            return rejectWithValue(error.message || 'Login failed');
        }
    }
);

export const authSlice = createSlice({
    name: 'auth',
    initialState: parsedAuth || initialState,
    reducers: {
        logout: (state) => {
            state.isAuthenticated = false;
            state.userId = null;
            state.role = null;
            state.user = null;
            state.loading = false;
            state.error = null;
            localStorage.removeItem('auth');
            localStorage.removeItem('access_token');
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(loginUser.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(
                loginUser.fulfilled,
                (
                    state,
                    action: PayloadAction<{
                        role: 'admin' | 'user';
                        userId: string;
                        user?: BackendTeacher;
                    }>
                ) => {
                    state.loading = false;
                    state.isAuthenticated = true;
                    state.role = action.payload.role;
                    state.userId = action.payload.userId;
                    state.user = action.payload.user || null;
                    localStorage.setItem('auth', JSON.stringify(state));
                }
            )
            .addCase(loginUser.rejected, (state, action) => {
                state.loading = false;
                state.error = action.payload || 'Nieznany błąd podczas logowania';
                localStorage.removeItem('access_token');
                localStorage.removeItem('auth');
            });
    },
});

export const { logout } = authSlice.actions;

export default authSlice.reducer;