// buildingSlice.ts

import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { GridRowId, GridRowModesModel } from '@mui/x-data-grid';
import { API_ENDPOINTS } from '../urls';
import { Building, BuildingsState, BackendBuilding } from '../../utils/Interfaces';

const initialState: BuildingsState = {
    rows: [],
    rowModesModel: {},
    selectedRowId: null,
    selectedRowCode: null,
    loading: false,
    error: null,
};

export const fetchBuildings = createAsyncThunk<Building[]>('buildings/fetchBuildings', async () => {
    const response = await fetch(API_ENDPOINTS.BUILDINGS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendBuilding[] = await response.json();
    const adjustedData: Building[] = data.map((building) => ({
        id: building.id,
        code: building.code,
    }));
    return adjustedData;
});

export const addBuilding = createAsyncThunk<{ tempId: GridRowId; building: Building }, Building>(
    'buildings/addBuilding',
    async (building: Building) => {
        const buildingData = {
            code: building.code,
        };
        const response = await fetch(API_ENDPOINTS.BUILDINGS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(buildingData),
        });
        if (!response.ok) {
            throw new Error('Failed to add building');
        }
        const data: BackendBuilding = await response.json();
        const adjustedBuilding: Building = {
            id: data.id,
            code: data.code,
        };
        return { tempId: building.id, building: adjustedBuilding };
    }
);

export const updateBuilding = createAsyncThunk<Building, Building>(
    'buildings/updateBuilding',
    async (building: Building) => {
        const buildingData = {
            code: building.code,
        };
        const response = await fetch(`${API_ENDPOINTS.BUILDINGS}/${building.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(buildingData),
        });
        if (!response.ok) {
            throw new Error('Failed to update building');
        }
        const data: BackendBuilding = await response.json();
        const adjustedBuilding: Building = {
            id: data.id,
            code: data.code,
        };
        return adjustedBuilding;
    }
);

export const deleteBuilding = createAsyncThunk<GridRowId, GridRowId>(
    'buildings/deleteBuilding',
    async (id: GridRowId) => {
        const response = await fetch(`${API_ENDPOINTS.BUILDINGS}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Failed to delete building');
        }
        return id;
    }
);

const buildingSlice = createSlice({
    name: 'buildings',
    initialState,
    reducers: {
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        },
        setSelectedRow: (state, action: PayloadAction<{ id: GridRowId; code: string }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowCode = action.payload.code;
        },
        clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowCode = null;
        },
        addNewBuilding: (state, action: PayloadAction<Building>) => {
            state.rows.push(action.payload);
        },
        removeNewBuilding: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter((row) => row.id !== action.payload);
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchBuildings.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchBuildings.fulfilled, (state, action: PayloadAction<Building[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchBuildings.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch buildings';
            })
            .addCase(addBuilding.fulfilled, (state, action) => {
                const { tempId, building } = action.payload;
                const index = state.rows.findIndex((row) => row.id === tempId);
                if (index !== -1) {
                    state.rows[index] = building;
                } else {
                    state.rows.push(building);
                }
                // Update rowModesModel with the new id
                if (state.rowModesModel[tempId]) {
                    state.rowModesModel[building.id] = state.rowModesModel[tempId];
                    delete state.rowModesModel[tempId];
                }
            })
            .addCase(addBuilding.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add building';
            })
            .addCase(updateBuilding.fulfilled, (state, action: PayloadAction<Building>) => {
                const updatedBuilding = action.payload;
                const index = state.rows.findIndex((row) => row.id === updatedBuilding.id);
                if (index !== -1) {
                    state.rows[index] = updatedBuilding;
                }
            })
            .addCase(updateBuilding.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update building';
            })
            .addCase(deleteBuilding.fulfilled, (state, action: PayloadAction<GridRowId>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteBuilding.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete building';
            });
    },
});

export const {
    setRowModesModel,
    setSelectedRow,
    clearSelectedRow,
    addNewBuilding,
    removeNewBuilding,
} = buildingSlice.actions;

export default buildingSlice.reducer;
