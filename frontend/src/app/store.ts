import {configureStore} from '@reduxjs/toolkit';
import authReducer from './slices/authSlice.ts';
import teacherReducer from './slices/teacherSlice.ts';
import classroomReducer from './slices/classroomSlice';
import buildingReducer from './slices/buildingSlice.ts';
import fieldReducer from './slices/fieldOfStudySlice.ts';
import specialisationReducer from './slices/specialisationSlice.ts';
import slotsReducer from './slices/slotsSlice.ts';
import slotsDayReducer from './slices/slotsDaysSlice.ts';
import semestersReducer from './slices/semesterSlice.ts';
import subjectReducer from './slices/subjectSlice.ts'
import subjectTypeReducer from './slices/subjectTypeSlice.ts'
import groupsReducer from './slices/groupSlice.ts'

export const store = configureStore({
    reducer: {
        auth: authReducer,
        teachers: teacherReducer,
        classroom: classroomReducer,
        buildings: buildingReducer,
        classrooms: classroomReducer,
        fields: fieldReducer,
        specialisations: specialisationReducer,
        slots: slotsReducer,
        slotsDays: slotsDayReducer,
        semesters: semestersReducer,
        subjects: subjectReducer,
        subjectsTypes: subjectTypeReducer,
        groups: groupsReducer,
    },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
