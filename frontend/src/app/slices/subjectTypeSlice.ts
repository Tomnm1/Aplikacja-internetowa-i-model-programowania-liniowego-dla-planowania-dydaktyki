import { createSlice, createAsyncThunk } from '@reduxjs/toolkit';
import { API_ENDPOINTS } from '../urls';
import {
    SubjectTypeState,
    BackendSubjectType,
    SubjectType
} from '../../utils/Interfaces';

const initialState: SubjectTypeState = {
    rows: [],
    loading: false,
    error: null,
};

export const fetchSubjectType = createAsyncThunk<SubjectType[]>('subjectType/fetchSubjectsTypes', async () => {
    const response = await fetch(API_ENDPOINTS.SUBJECT_TYPE);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSubjectType[] = await response.json();
    return data.map((subjectType) => ({
        ...subjectType,
        subjectTypeId: subjectType.subjectTypeId!,
    }));
});

export const addSubjectType = createAsyncThunk<SubjectType, BackendSubjectType>(
    'subjectType/addSubjectsTypes',
    async (subjectTypeData) => {
        const response = await fetch(API_ENDPOINTS.SUBJECT_TYPE, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(subjectTypeData),
        });
        if (!response.ok) {
            throw new Error('Failed to add subjectType');
        }
        const data: BackendSubjectType = await response.json();
        return {
            ...data,
            subjectTypeId: data.subjectTypeId!,
        };
    }
);

export const updateSubjectType = createAsyncThunk<SubjectType, BackendSubjectType>(
    'subjectType/updateSubjectsTypes',
    async (subjectTypeData) => {
        if (!subjectTypeData.subjectTypeId) {
            throw new Error('subjectType_id is required for updating');
        }
        const response = await fetch(`${API_ENDPOINTS.SUBJECT_TYPE}/${subjectTypeData.subjectTypeId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(subjectTypeData),
        });
        if (!response.ok) {
            throw new Error('Failed to update SubjectType');
        }
        const data: BackendSubjectType = await response.json();
        return {
            ...data,
            subjectTypeId: data.subjectTypeId!,
        };
    }
);

export const deleteSubjectType = createAsyncThunk<number, number>(
    'subjectType/deleteSubjectsTypes',
    async (id) => {
        const response = await fetch(`${API_ENDPOINTS.SUBJECT_TYPE}/${id}`, { method: 'DELETE' });
        if (!response.ok) {
            throw new Error('Failed to delete SubjectType');
        }
        return id;
    }
);

const subjectTypeSlice = createSlice({
    name: 'subjectsTypes',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder
            .addCase(fetchSubjectType.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSubjectType.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSubjectType.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch subjects';
            })
            .addCase(addSubjectType.fulfilled, (state, action) => {
                state.rows.push(action.payload);
            })
            .addCase(updateSubjectType.fulfilled, (state, action) => {
                const index = state.rows.findIndex((row) => row.subjectTypeId === action.payload.subjectTypeId);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(deleteSubjectType.fulfilled, (state, action) => {
                state.rows = state.rows.filter((row) => row.subjectTypeId !== action.payload);
            });
    },
});

export default subjectTypeSlice.reducer;
