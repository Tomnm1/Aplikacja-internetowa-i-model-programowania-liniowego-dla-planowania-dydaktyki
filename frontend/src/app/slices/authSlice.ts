import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { API_ENDPOINTS } from '../urls';
import { BackendTeacher } from '../../utils/Interfaces';

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

const storedAuth = localStorage.getItem('auth');
const parsedAuth: AuthState | null = storedAuth ? JSON.parse(storedAuth) : null;

export const loginUser = createAsyncThunk<
    { role: 'admin' | 'user'; userId: string; user?: BackendTeacher },
    string,
    { rejectValue: string }
>(
    'auth/loginUser',
    async (username: string, { rejectWithValue }) => {
        try {
            if (username === 'admin') {
                return { role: 'admin', userId: 'admin' };
            } else {
                const userId = username.trim();
                const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${userId}`);
                if (!response.ok) {
                    throw new Error('Nie znaleziono użytkownika');
                }
                const data: BackendTeacher = await response.json();
                return { role: 'user', userId, user: data };
            }
        } catch (error: any) {
            return rejectWithValue(error.message || 'Login failed');
        }
    }
);

const authSlice = createSlice({
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
            });
    },
});

export const { logout } = authSlice.actions;

export default authSlice.reducer;
