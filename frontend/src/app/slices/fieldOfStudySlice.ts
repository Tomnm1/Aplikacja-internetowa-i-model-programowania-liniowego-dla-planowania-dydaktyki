import {createAsyncThunk, createSlice, PayloadAction} from '@reduxjs/toolkit';
import {GridRowId, GridRowModesModel} from '@mui/x-data-grid';
import {API_ENDPOINTS} from '../urls';
import {BackendFieldOfStudies, FieldOfStudiesState, FieldOfStudy} from '../../utils/Interfaces';

const initialState: FieldOfStudiesState = {
    rows: [], rowModesModel: {}, selectedRowId: null, selectedRowName: null, loading: false, error: null,
};

export const fetchFOS = createAsyncThunk<FieldOfStudy[]>('fos/fetchFOS', async () => {
    const response = await fetch(API_ENDPOINTS.FIELD_OF_STUDIES);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendFieldOfStudies[] = await response.json();
    const adjustedData: FieldOfStudy[] = data.map((fos) => ({
        id: fos.fieldOfStudyId, name: fos.name,
    }));
    return adjustedData;
});

export const addFOS = createAsyncThunk<{
    tempId: GridRowId;
    fos: FieldOfStudy
}, FieldOfStudy>('fos/addFOS', async (fos: FieldOfStudy) => {
    const fosData = {
        name: fos.name,
    };
    const response = await fetch(API_ENDPOINTS.FIELD_OF_STUDIES, {
        method: 'POST', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(fosData),
    });
    if (!response.ok) {
        throw new Error('Failed to add field of study');
    }
    const data: BackendFieldOfStudies = await response.json();
    const adjustedFOS: FieldOfStudy = {
        id: data.fieldOfStudyId, name: data.name,
    };
    return {tempId: fos.id, fos: adjustedFOS};
});

export const updateFOS = createAsyncThunk<FieldOfStudy, FieldOfStudy>('fos/updateFOS', async (fos: FieldOfStudy) => {
    const fosData = {
        name: fos.name,
    };
    const response = await fetch(`${API_ENDPOINTS.FIELD_OF_STUDIES}/${fos.id}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(fosData),
    });
    if (!response.ok) {
        throw new Error('Failed to update field of study');
    }
    const data: BackendFieldOfStudies = await response.json();
    const adjustedBuilding: FieldOfStudy = {
        id: data.fieldOfStudyId, name: data.name,
    };
    return adjustedBuilding;
});

export const deleteFOS = createAsyncThunk<GridRowId, GridRowId>('fos/deleteFOS', async (id: GridRowId) => {
    const response = await fetch(`${API_ENDPOINTS.FIELD_OF_STUDIES}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete field of study');
    }
    return id;
});

const fieldOfStudySlice = createSlice({
    name: 'fieldOfStudies', initialState, reducers: {
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        }, setSelectedRow: (state, action: PayloadAction<{ id: GridRowId; name: string }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowName = action.payload.name;
        }, clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowName = null;
        }, addNewFOS: (state, action: PayloadAction<FieldOfStudy>) => {
            state.rows.push(action.payload);
        }, removeNewFOS: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter((row) => row.id !== action.payload);
        },
    }, extraReducers: (builder) => {
        builder
            .addCase(fetchFOS.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchFOS.fulfilled, (state, action: PayloadAction<FieldOfStudy[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchFOS.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch field of studies';
            })
            .addCase(addFOS.fulfilled, (state, action) => {
                const {tempId, fos} = action.payload;
                const index = state.rows.findIndex((row) => row.id === tempId);
                if (index !== -1) {
                    state.rows[index] = fos;
                } else {
                    state.rows.push(fos);
                }
                if (state.rowModesModel[tempId]) {
                    state.rowModesModel[fos.id] = state.rowModesModel[tempId];
                    delete state.rowModesModel[tempId];
                }
            })
            .addCase(addFOS.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add field of studies';
            })
            .addCase(updateFOS.fulfilled, (state, action: PayloadAction<FieldOfStudy>) => {
                const updatedBuilding = action.payload;
                const index = state.rows.findIndex((row) => row.id === updatedBuilding.id);
                if (index !== -1) {
                    state.rows[index] = updatedBuilding;
                }
            })
            .addCase(updateFOS.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update field of studies';
            })
            .addCase(deleteFOS.fulfilled, (state, action: PayloadAction<GridRowId>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteFOS.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete field of studies';
            });
    },
});

export const {
    setRowModesModel, setSelectedRow, clearSelectedRow, addNewFOS, removeNewFOS,
} = fieldOfStudySlice.actions;

export default fieldOfStudySlice.reducer;
