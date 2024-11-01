import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchRooms = createAsyncThunk('rooms/fetchRooms', async () => {
    const response = await axios.get(endpoints.room.getAll);
    return response.data;
});

export const createRoom = createAsyncThunk('rooms/createRoom', async (newRoom) => {
    const response = await axios.post(endpoints.room.create, newRoom);
    return response.data;
});

const roomSlice = createSlice({
    name: 'rooms',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchRooms.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchRooms.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchRooms.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createRoom.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default roomSlice.reducer;
