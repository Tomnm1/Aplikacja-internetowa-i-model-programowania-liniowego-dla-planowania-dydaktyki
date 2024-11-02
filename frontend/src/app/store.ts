import { configureStore } from '@reduxjs/toolkit';
import teacherReducer from './slices/teacherSlice.ts';
import classroomReducer from './slices/classroomSlice';
import buildingReducer from './slices/buildingSlice.ts';
import fieldReducer from './slices/fieldOfStudySlice.ts';

export const store = configureStore({
    reducer: {
        teachers: teacherReducer,
        classroom: classroomReducer,
        buildings: buildingReducer,
        classrooms: classroomReducer,
        fields: fieldReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
