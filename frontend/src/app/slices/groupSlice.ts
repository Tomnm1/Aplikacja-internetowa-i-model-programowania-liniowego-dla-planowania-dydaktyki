import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';


export const fetchGroups = createAsyncThunk('groups/fetchGroups', async () => {
    const response = await axios.get(endpoints.group.getAll);
    return response.data;
});

export const createGroup = createAsyncThunk('groups/createGroup', async (newGroup) => {
    const response = await axios.post(endpoints.group.create, newGroup);
    return response.data;
});

const groupSlice = createSlice({
    name: 'groups',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchGroups.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchGroups.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchGroups.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createGroup.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default groupSlice.reducer;