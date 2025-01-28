import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {API_ENDPOINTS} from '../urls';
import {BackendSlotsDay, SlotsDay, SlotsDayState} from '../../utils/Interfaces';
import {fetchWithAuth} from "../fetchWithAuth.ts";

const initialState: SlotsDayState = {
    rows: [], loading: false, error: null,
};

export const fetchSlotsDays = createAsyncThunk<SlotsDay[]>('slotsDays/fetchWithAuthSlotsDays', async () => {
    const response = await fetchWithAuth(API_ENDPOINTS.SLOTS_DAYS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSlotsDay[] = await response.json();
    return data.map((slotDay) => ({
        id: slotDay.SlotsDayId!,
        day: slotDay.day,
        slotId: slotDay.slot.slotId,
        slotRepresentation: `${slotDay.slot.startTime} - ${slotDay.slot.endTime}`,
    }));
});

export const addSlotsDay = createAsyncThunk<SlotsDay, BackendSlotsDay>('slotsDays/addSlotsDay', async (slotsDayData) => {
    const response = await fetchWithAuth(API_ENDPOINTS.SLOTS_DAYS, {
        method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(slotsDayData),
    });
    if (!response.ok) {
        throw new Error('Failed to add slotsDay');
    }
    const data: BackendSlotsDay = await response.json();
    return {
        id: data.SlotsDayId!,
        slotId: data.slot.slotId,
        day: data.day,
        slotRepresentation: `${data.slot.startTime} - ${data.slot.endTime}`,
    };
});

export const updateSlotsDay = createAsyncThunk<SlotsDay, BackendSlotsDay>('slotsDays/updateSlotsDay', async (slotsDayData) => {
    if (!slotsDayData.SlotsDayId) {
        throw new Error('SlotsDayId is required for updating');
    }
    const response = await fetchWithAuth(`${API_ENDPOINTS.SLOTS_DAYS}/${slotsDayData.SlotsDayId}`, {
        method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(slotsDayData),
    });
    if (!response.ok) {
        throw new Error('Failed to update slotsDay');
    }
    const data: BackendSlotsDay = await response.json();
    return {
        id: data.SlotsDayId!,
        slotId: data.slot.slotId,
        day: data.day,
        slotRepresentation: `${data.slot.startTime} - ${data.slot.endTime}`,
    };
});

export const deleteSlotsDay = createAsyncThunk<number, number>('slotsDays/deleteSlotsDay', async (id) => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.SLOTS_DAYS}/${id}`, {method: 'DELETE'});
    if (!response.ok) {
        throw new Error('Failed to delete SlotsDay');
    }
    return id;
});

const slotsDaysSlice = createSlice({
    name: 'slotsDays', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchSlotsDays.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSlotsDays.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSlotsDays.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetchWithAuth slotsDays';
            })
            .addCase(addSlotsDay.fulfilled, (state, action) => {
                state.rows.push(action.payload);
            })
            .addCase(updateSlotsDay.fulfilled, (state, action) => {
                const index = state.rows.findIndex((row) => row.id === action.payload.id);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(deleteSlotsDay.fulfilled, (state, action) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            });
    },
});

export default slotsDaysSlice.reducer;
