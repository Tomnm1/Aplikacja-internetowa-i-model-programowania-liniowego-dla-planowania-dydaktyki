import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface AuthState {
    isAuthenticated: boolean;
    userId: string | null;
    role: 'admin' | 'user' | null;
}

const initialState: AuthState = {
    isAuthenticated: false,
    userId: null,
    role: null,
};

const storedAuth = localStorage.getItem('auth');
const parsedAuth: AuthState | null = storedAuth ? JSON.parse(storedAuth) : null;

const authSlice = createSlice({
    name: 'auth',
    initialState: parsedAuth || initialState,
    reducers: {
        login: (state, action: PayloadAction<string>) => {
            state.isAuthenticated = true;
            if (action.payload === 'admin') {
                state.role = 'admin';
                state.userId = 'admin';
            } else {
                state.role = 'user';
                state.userId = action.payload;
            }
            localStorage.setItem('auth', JSON.stringify(state));
        },
        logout: (state) => {
            state.isAuthenticated = false;
            state.userId = null;
            state.role = null;
            localStorage.removeItem('auth');
        },
    },
});

export const { login, logout } = authSlice.actions;

export default authSlice.reducer;
