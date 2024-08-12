// employeesSlice.ts
import { createSlice, PayloadAction } from '@reduxjs/toolkit';
import { GridRowId, GridRowModesModel } from '@mui/x-data-grid';
import { randomCreatedDate, randomTraderName, randomId, randomArrayItem } from '@mui/x-data-grid-generator';

const roles = ['Market', 'Finance', 'Development'];
const randomRole = () => randomArrayItem(roles);

interface Employee {
    id: GridRowId;
    name: string;
    age: number | string;
    joinDate: Date;
    role: string;
    isNew: boolean;
}

interface EmployeesState {
    rows: Employee[];
    rowModesModel: GridRowModesModel;
    selectedRowId: GridRowId | null;
    selectedRowName: string | null;
}

const initialState: EmployeesState = {
    rows: [
        { id: randomId(), name: randomTraderName(), age: 25, joinDate: randomCreatedDate(), role: randomRole(), isNew: false },
        { id: randomId(), name: randomTraderName(), age: 36, joinDate: randomCreatedDate(), role: randomRole(), isNew: false },
        { id: randomId(), name: randomTraderName(), age: 19, joinDate: randomCreatedDate(), role: randomRole(), isNew: false },
        { id: randomId(), name: randomTraderName(), age: 28, joinDate: randomCreatedDate(), role: randomRole(), isNew: false },
        { id: randomId(), name: randomTraderName(), age: 23, joinDate: randomCreatedDate(), role: randomRole(), isNew: false },
    ],
    rowModesModel: {},
    selectedRowId: null,
    selectedRowName: null,
};

const employeesSlice = createSlice({
    name: 'employees',
    initialState,
    reducers: {
        setRows: (state, action: PayloadAction<Employee[]>) => {
            state.rows = action.payload;
        },
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        },
        addEmployee: (state, action: PayloadAction<Employee>) => {
            state.rows.push(action.payload);
        },
        updateEmployee: (state, action: PayloadAction<Employee>) => {
            const index = state.rows.findIndex((row) => row.id === action.payload.id);
            if (index !== -1) {
                state.rows[index] = action.payload;
            }
        },
        deleteEmployee: (state) => {
            if (state.selectedRowId) {
                state.rows = state.rows.filter(row => row.id !== state.selectedRowId);
                state.selectedRowId = null;
                state.selectedRowName = null;
            }
        },
        setSelectedRow: (state, action: PayloadAction<{ id: GridRowId, name: string }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowName = action.payload.name;
        },
        clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowName = null;
        },
    },
});

export const {
    setRows,
    setRowModesModel,
    addEmployee,
    updateEmployee,
    deleteEmployee,
    setSelectedRow,
    clearSelectedRow
} = employeesSlice.actions;

export default employeesSlice.reducer;
