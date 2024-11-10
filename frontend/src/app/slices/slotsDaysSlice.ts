import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { API_ENDPOINTS } from '../urls';
import {BackendSlotsDay, SlotsDayState, SlotsDay} from '../../utils/Interfaces';

const initialState: SlotsDayState = {
    rows: [],
    rowModesModel: {},
    selectedRowId: null,
    loading: false,
    error: null,
};

export const fetchSlotsDays = createAsyncThunk<SlotsDay[]>('slotsDay/fetchSlotsDays', async () => {
    const response = await fetch(API_ENDPOINTS.SLOTS_DAYS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSlotsDay[] = await response.json();
    const adjustedData: SlotsDay[] = data.map((slotDay) => ({
        id: slotDay.SlotsDayId!, // Tomek spójrz na to
        day: slotDay.day,
        slotId: slotDay.slot.slotId,
        slotRepresentation: `${slotDay.slot.startTime} - ${slotDay.slot.endTime}`,
    }));
    return adjustedData;
});

export const addSlotsDay = createAsyncThunk<SlotsDay,BackendSlotsDay>(
    'slotsDay/addSlotsDay',
    async (slotsDayData) => {
        const response = await fetch(API_ENDPOINTS.SLOTS_DAYS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(slotsDayData),
        });
        if (!response.ok) {
            throw new Error('Failed to add slotsDay');
        }
        const data: BackendSlotsDay = await response.json();
        const adjustedSlotsDay: SlotsDay = {
            id: data.SlotsDayId!,
            slotId: data.slot.slotId,
            day: data.day,
            slotRepresentation: `${data.slot.startTime} - ${data.slot.endTime}`,
        };
        return adjustedSlotsDay;
    }
);

export const updateSlotsDay = createAsyncThunk<SlotsDay, BackendSlotsDay>(
    'slotsDay/updateSlotsDay',
    async (slotsDayData) => {
        if (slotsDayData.SlotsDayId == null) {
            throw new Error('SlotsDayId is required for updating');
        }
        const response = await fetch(`${API_ENDPOINTS.SLOTS_DAYS}/${slotsDayData.SlotsDayId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(slotsDayData),
        });
        if (!response.ok) {
            throw new Error('Failed to update slotsDay');
        }
        const data: BackendSlotsDay = await response.json();
        if (data.SlotsDayId == null) {
            throw new Error('SlotsDayId is missing in the response');
        }
        const adjustedSlotsDay: SlotsDay = {
            id: data.SlotsDayId,
            slotId: data.slot.slotId,
            day: data.day,
            slotRepresentation: `${data.slot.startTime} - ${data.slot.endTime}`,
        };
        return adjustedSlotsDay;
    }
);

export const deleteSlotsDay = createAsyncThunk<number, number>(
    'slotsDay/deleteSlotsDay',
    async (id) => {
        const response = await fetch(`${API_ENDPOINTS.SLOTS_DAYS}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Failed to delete SlotsDay');
        }
        return id;
    }
);

const slotsDaySlice = createSlice({
    name: 'slotsDays',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSlotsDays.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSlotsDays.fulfilled, (state, action: PayloadAction<SlotsDay[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSlotsDays.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch slotsDays';
            })
            .addCase(addSlotsDay.fulfilled, (state, action: PayloadAction<SlotsDay>) => {
                state.rows.push(action.payload);
            })
            .addCase(addSlotsDay.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add slotsDay';
            })
            .addCase(updateSlotsDay.fulfilled, (state, action: PayloadAction<SlotsDay>) => {
                const index = state.rows.findIndex((row) => row.id === action.payload.id);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(updateSlotsDay.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update slotsDay';
            })
            .addCase(deleteSlotsDay.fulfilled, (state, action: PayloadAction<number>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteSlotsDay.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete slotsDay';
            });
    },
});

export default slotsDaySlice.reducer;
