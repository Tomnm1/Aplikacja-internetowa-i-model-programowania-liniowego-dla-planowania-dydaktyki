// store.ts
import { configureStore } from '@reduxjs/toolkit';
import employeesReducer from './slices/employeesSlice';
import classroomReducer from './slices/classroomSlice'

export const store = configureStore({
    reducer: {
        employees: employeesReducer,
        classroom: classroomReducer
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
