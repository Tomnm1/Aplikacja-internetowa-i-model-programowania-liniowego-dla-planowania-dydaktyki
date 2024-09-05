// classroomSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { GridRowId } from '@mui/x-data-grid';
import {ClassroomRow, ClassroomState} from "../../utils/Interfaces.ts";



const initialState: ClassroomState = {
    rows: [
        {
            id: '1',
            name: 'Sala 101',
            capacity: 30,
            floor: 'First Floor',
            isNew: false,
        },
    ],
    rowModesModel: {},
};

const classroomSlice = createSlice({
    name: 'classroom',
    initialState,
    reducers: {
        addClassroom: (state, action: PayloadAction<ClassroomRow>) => {
            state.rows.push(action.payload);
            state.rowModesModel[action.payload.id] = { mode: 'edit' };
        },
        updateClassroom: (state, action: PayloadAction<ClassroomRow>) => {
            const index = state.rows.findIndex(row => row.id === action.payload.id);
            if (index !== -1) {
                state.rows[index] = action.payload;
            }
        },
        deleteClassroom: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter(row => row.id !== action.payload);
        },
        setRowModesModel: (state, action: PayloadAction<Record<GridRowId, { mode: string }>>) => {
            state.rowModesModel = action.payload;
        },
    },
});

export const { addClassroom, updateClassroom, deleteClassroom, setRowModesModel } = classroomSlice.actions;
export default classroomSlice.reducer;
