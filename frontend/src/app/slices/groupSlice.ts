import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {API_ENDPOINTS} from '../urls';
import {Group, GroupState} from '../../utils/Interfaces';
import {fetchWithAuth} from "../fetchWithAuth.ts";

const initialState: GroupState = {
    rows: [], loading: false, error: null,
};

export const fetchGroup = createAsyncThunk<Group[], number>('group/fetchWithAuthGroups', async (semesterId) => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.GROUPS}/semester/${semesterId}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: Group[] = await response.json();
    return data.map((group) => ({
        ...group,
    }));
});

const groupSlice = createSlice({
    name: 'groups', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchGroup.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchGroup.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchGroup.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetchWithAuth groups';
            })
    },
});

export default groupSlice.reducer;
