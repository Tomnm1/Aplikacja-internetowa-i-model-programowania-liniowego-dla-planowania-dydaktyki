import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchCourses = createAsyncThunk('courses/fetchCourses', async () => {
    const response = await axios.get(endpoints.course.getAll);
    return response.data;
});

export const createCourse = createAsyncThunk('courses/createCourse', async (newCourse) => {
    const response = await axios.post(endpoints.course.create, newCourse);
    return response.data;
});

const courseSlice = createSlice({
    name: 'courses',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchCourses.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchCourses.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchCourses.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createCourse.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default courseSlice.reducer;