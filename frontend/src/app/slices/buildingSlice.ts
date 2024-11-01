import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import endpoints from '../../utils/endpoints.ts';
import axios from 'axios';

export const fetchBuildings = createAsyncThunk('buildings/fetchBuildings', async () => {
    const response = await axios.get(endpoints.building.getAll);
    return response.data;
});

export const createBuilding = createAsyncThunk('buildings/createBuilding', async (newBuilding) => {
    const response = await axios.post(endpoints.building.create, newBuilding);
    return response.data;
});

const buildingSlice = createSlice({
    name: 'buildings',
    initialState: {
        list: [],
        status: 'idle',
        error: null,
    },
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchBuildings.pending, (state) => {
                state.status = 'loading';
            })
            .addCase(fetchBuildings.fulfilled, (state, action) => {
                state.status = 'succeeded';
                state.list = action.payload;
            })
            .addCase(fetchBuildings.rejected, (state, action) => {
                state.status = 'failed';
                state.error = action.error.message;
            })
            .addCase(createBuilding.fulfilled, (state, action) => {
                state.list.push(action.payload);
            });
    },
});

export default buildingSlice.reducer;