import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchSubjects = createAsyncThunk('subjects/fetchSubjects', async () => {
    const response = await axios.get(endpoints.subject.getAll);
    return response.data;
});

export const createSubject = createAsyncThunk('subjects/createSubject', async (newSubject) => {
    const response = await axios.post(endpoints.subject.create, newSubject);
    return response.data;
});

const subjectSlice = createSlice({
    name: 'subjects',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSubjects.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchSubjects.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchSubjects.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createSubject.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default subjectSlice.reducer;