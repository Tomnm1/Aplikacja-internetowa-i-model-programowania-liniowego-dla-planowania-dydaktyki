import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { GridRowId, GridRowModesModel } from '@mui/x-data-grid';
import { API_ENDPOINTS } from '../urls';
import {BackendSlot, Slot, SlotsState, LocalTime} from '../../utils/Interfaces';

const initialState: SlotsState = {
    rows: [],
    rowModesModel: {},
    selectedRowId: null,
    selectedRowStart: null,
    selectedRowStop: null,
    loading: false,
    error: null,
};

export const fetchSlots = createAsyncThunk<Slot[]>('slots/fetchSlots', async () => {
    const response = await fetch(API_ENDPOINTS.SLOTS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSlot[] = await response.json();
    const adjustedData: Slot[] = data.map((slot) => ({
        slot_id: slot.slotId,
        start_time: slot.startTime,
        end_time: slot.endTime,
    }));
    return adjustedData;
});

export const addSlot = createAsyncThunk<{ tempId: GridRowId; slot: Slot }, Slot>(
    'slots/addSlot',
    async (slot: Slot) => {
        const slotData = {
            startTime: slot.start_time,
            endTime: slot.end_time,
        };
        const response = await fetch(API_ENDPOINTS.SLOTS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(slotData),
        });
        if (!response.ok) {
            throw new Error('Failed to add slot');
        }
        const data: BackendSlot = await response.json();
        const adjustedSlot: Slot = {
            slot_id: data.slotId,
            start_time: data.startTime,
            end_time: data.endTime,
        };
        return { tempId: slot.slot_id, slot: adjustedSlot };
    }
);

export const updateSlot = createAsyncThunk<Slot, Slot>(
    'slots/updateSlot',
    async (slot: Slot) => {
        const slotData = {
            startTime: slot.start_time,
            endTime: slot.end_time,
        };
        const response = await fetch(`${API_ENDPOINTS.SLOTS}/${slot.slot_id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(slotData),
        });
        if (!response.ok) {
            throw new Error('Failed to update slot');
        }
        const data: BackendSlot = await response.json();
        const adjustedSlot: Slot = {
            slot_id: data.slotId,
            start_time: data.startTime,
            end_time: data.endTime,
        };
        return adjustedSlot;
    }
);

export const deleteSlot = createAsyncThunk<GridRowId, GridRowId>(
    'slots/deleteSlot',
    async (id: GridRowId) => {
        const response = await fetch(`${API_ENDPOINTS.SLOTS}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Failed to delete slot');
        }
        return id;
    }
);

const slotSlice = createSlice({
    name: 'slots',
    initialState,
    reducers: {
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        },
        setSelectedRow: (state, action: PayloadAction<{ id: GridRowId; start: LocalTime, stop: LocalTime }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowStart = action.payload.start;
            state.selectedRowStop = action.payload.stop;

        },
        clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowStart = null;
            state.selectedRowStop = null;
        },
        addNewSlot: (state, action: PayloadAction<Slot>) => {
            state.rows.push(action.payload);
        },
        removeNewSlot: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter((row) => row.slot_id !== action.payload);
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchSlots.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSlots.fulfilled, (state, action: PayloadAction<Slot[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSlots.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch slots';
            })
            .addCase(addSlot.fulfilled, (state, action) => {
                const { tempId, slot } = action.payload;
                const index = state.rows.findIndex((row) => row.slot_id === tempId);
                if (index !== -1) {
                    state.rows[index] = slot;
                } else {
                    state.rows.push(slot);
                }
                if (state.rowModesModel[tempId]) {
                    state.rowModesModel[slot.slot_id] = state.rowModesModel[tempId];
                    delete state.rowModesModel[tempId];
                }
            })
            .addCase(addSlot.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add slot';
            })
            .addCase(updateSlot.fulfilled, (state, action: PayloadAction<Slot>) => {
                const updatedSlot = action.payload;
                const index = state.rows.findIndex((row) => row.slot_id === updatedSlot.slot_id);
                if (index !== -1) {
                    state.rows[index] = updatedSlot;
                }
            })
            .addCase(updateSlot.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update slot';
            })
            .addCase(deleteSlot.fulfilled, (state, action: PayloadAction<GridRowId>) => {
                state.rows = state.rows.filter((row) => row.slot_id !== action.payload);
            })
            .addCase(deleteSlot.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete slot';
            });
    },
});

export const {
    setRowModesModel,
    setSelectedRow,
    clearSelectedRow,
    addNewSlot,
    removeNewSlot,
} = slotSlice.actions;

export default slotSlice.reducer;
