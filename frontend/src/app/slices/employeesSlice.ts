import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchEmployees = createAsyncThunk('employees/fetchEmployees', async () => {
    const response = await axios.get(endpoints.employee.getAll);
    return response.data;
});

export const createEmployee = createAsyncThunk('employees/createEmployee', async (newEmployee) => {
    const response = await axios.post(endpoints.employee.create, newEmployee);
    return response.data;
});

const employeeSlice = createSlice({
    name: 'employees',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchEmployees.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchEmployees.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchEmployees.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createEmployee.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default employeeSlice.reducer;
