import {createAsyncThunk, createSlice, PayloadAction} from '@reduxjs/toolkit';
import {GridRowId, GridRowModesModel} from '@mui/x-data-grid';
import {API_ENDPOINTS} from '../urls';
import {Plan, PlanState} from '../../utils/Interfaces';
import {fetchWithAuth} from "../fetchWithAuth.ts";

const initialState: PlanState = {
    rows: [], rowModesModel: {}, selectedRowId: null, selectedRowCode: null, loading: false, error: null,
};

export const fetchPlans = createAsyncThunk<Plan[]>('plans/fetchWithAuthPlans', async () => {
    const response = await fetchWithAuth(API_ENDPOINTS.PLANS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: Plan[] = await response.json();
    return data;
});

export const updatePlan = createAsyncThunk<Plan, Plan>('plans/updatePlan', async (plan: Plan) => {
    const planData = {
        name: plan.name,
        published: plan.published,
    };
    const response = await fetchWithAuth(`${API_ENDPOINTS.PLANS}/${plan.planId}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(planData),
    });
    if (!response.ok) {
        throw new Error('Failed to update plan');
    }
    const data: Plan = await response.json();
    return data;
});

export const deletePlan = createAsyncThunk<GridRowId, GridRowId>('plans/deletePlan', async (id: GridRowId) => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.PLANS}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete plan');
    }
    return id;
});

const plansSlice = createSlice({
    name: 'plans', initialState, reducers: {
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        }, setSelectedRow: (state, action: PayloadAction<{ id: GridRowId; name: string }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowCode = action.payload.name;
        }, clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowCode = null;
        }, addNewPlan: (state, action: PayloadAction<Plan>) => {
            state.rows.push(action.payload);
        }, removeNewPlan: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter((row) => row.planId !== action.payload);
        },
    }, extraReducers: (builder) => {
        builder
            .addCase(fetchPlans.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchPlans.fulfilled, (state, action: PayloadAction<Plan[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchPlans.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch plans';
            })
            .addCase(updatePlan.fulfilled, (state, action: PayloadAction<Plan>) => {
                const updatedPlan = action.payload;
                const index = state.rows.findIndex((row) => row.planId === updatedPlan.planId);
                if (index !== -1) {
                    state.rows[index] = updatedPlan;
                }
            })
            .addCase(updatePlan.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update plan';
            })
            .addCase(deletePlan.fulfilled, (state, action: PayloadAction<GridRowId>) => {
                state.rows = state.rows.filter((row) => row.planId !== action.payload);
            })
            .addCase(deletePlan.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete plan';
            });
    },
});

export const {
    setRowModesModel, setSelectedRow, clearSelectedRow, addNewPlan, removeNewPlan,
} = plansSlice.actions;

export default plansSlice.reducer;
