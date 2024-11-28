import {createAsyncThunk, createSlice, PayloadAction} from '@reduxjs/toolkit';
import {BackendSpecialisation, Specialisation} from '../../utils/Interfaces';
import {API_ENDPOINTS} from '../urls';

interface SpecialisationsState {
    rows: Specialisation[];
    loading: boolean;
    error: string | null;
}

const initialState: SpecialisationsState = {
    rows: [], loading: false, error: null,
};

export const fetchSpecialisations = createAsyncThunk<Specialisation[]>('specialisations/fetchSpecialisations', async () => {
    const response = await fetch(API_ENDPOINTS.SPECIALISATIONS);
    if (!response.ok) {
        throw new Error('Failed to fetch specialisations');
    }
    const data: BackendSpecialisation[] = await response.json();
    const adjustedData: Specialisation[] = data.map((spec) => ({
        id: spec.specialisationId!,
        name: spec.name,
        cycle: spec.cycle,
        fieldOfStudyId: spec.fieldOfStudy.fieldOfStudyId,
        fieldOfStudyName: spec.fieldOfStudy.name,
    }));
    return adjustedData;
});

export const addSpecialisation = createAsyncThunk<Specialisation, BackendSpecialisation>('specialisations/addSpecialisation', async (specData) => {
    const response = await fetch(API_ENDPOINTS.SPECIALISATIONS, {
        method: 'POST', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(specData),
    });

    if (!response.ok) {
        throw new Error('Failed to add specialisation');
    }

    const data: BackendSpecialisation = await response.json();

    const adjustedSpec: Specialisation = {
        id: data.specialisationId!,
        name: data.name,
        cycle: data.cycle,
        fieldOfStudyId: data.fieldOfStudy.fieldOfStudyId,
        fieldOfStudyName: data.fieldOfStudy.name,
    };

    return adjustedSpec;
});

export const updateSpecialisation = createAsyncThunk<Specialisation, BackendSpecialisation>('specialisations/updateSpecialisation', async (specData) => {
    if (specData.specialisationId == null) {
        throw new Error('specialisationId is required for updating');
    }

    const response = await fetch(`${API_ENDPOINTS.SPECIALISATIONS}/${specData.specialisationId}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(specData),
    });

    if (!response.ok) {
        throw new Error('Failed to update specialisation');
    }

    const data: BackendSpecialisation = await response.json();

    const adjustedSpec: Specialisation = {
        id: data.specialisationId!,
        name: data.name,
        cycle: data.cycle,
        fieldOfStudyId: data.fieldOfStudy.fieldOfStudyId,
        fieldOfStudyName: data.fieldOfStudy.name,
    };

    return adjustedSpec;
});

export const deleteSpecialisation = createAsyncThunk<number, number>('specialisations/deleteSpecialisation', async (id) => {
    const response = await fetch(`${API_ENDPOINTS.SPECIALISATIONS}/${id}`, {
        method: 'DELETE',
    });

    if (!response.ok) {
        throw new Error('Failed to delete specialisation');
    }

    return id;
});

const specialisationsSlice = createSlice({
    name: 'specialisations', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchSpecialisations.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSpecialisations.fulfilled, (state, action: PayloadAction<Specialisation[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSpecialisations.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch specialisations';
            })
            .addCase(addSpecialisation.fulfilled, (state, action: PayloadAction<Specialisation>) => {
                state.rows.push(action.payload);
            })
            .addCase(addSpecialisation.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add specialisation';
            })
            .addCase(updateSpecialisation.fulfilled, (state, action: PayloadAction<Specialisation>) => {
                const index = state.rows.findIndex((row) => row.id === action.payload.id);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(updateSpecialisation.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update specialisation';
            })
            .addCase(deleteSpecialisation.fulfilled, (state, action: PayloadAction<number>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteSpecialisation.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete specialisation';
            });
    },
});

export default specialisationsSlice.reducer;
