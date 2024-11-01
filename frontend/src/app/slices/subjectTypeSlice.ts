import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchSubjectTypes = createAsyncThunk('subjectTypes/fetchSubjectTypes', async () => {
    const response = await axios.get(endpoints.subjectType.getAll);
    return response.data;
});

export const createSubjectType = createAsyncThunk('subjectTypes/createSubjectType', async (newSubjectType) => {
    const response = await axios.post(endpoints.subjectType.create, newSubjectType);
    return response.data;
});

const subjectTypeSlice = createSlice({
    name: 'subjectTypes',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSubjectTypes.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchSubjectTypes.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchSubjectTypes.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createSubjectType.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default subjectTypeSlice.reducer;