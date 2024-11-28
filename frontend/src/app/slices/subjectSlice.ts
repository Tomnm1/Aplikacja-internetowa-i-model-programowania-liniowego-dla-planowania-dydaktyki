import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {API_ENDPOINTS} from '../urls';
import {BackendSemester, BackendSubject, Subject, SubjectState} from '../../utils/Interfaces';

const initialState: SubjectState = {
    rows: [], loading: false, error: null,
};

export const fetchSubject = createAsyncThunk<Subject[]>('subject/fetchSubjects', async () => {
    const response = await fetch(API_ENDPOINTS.SUBJECT);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSubject[] = await response.json();
    return data.map((subject) => ({
        ...subject, SubjectId: subject.SubjectId!,
    }));
});

export const addSubject = createAsyncThunk<Subject, BackendSubject>('subject/addSubject', async (subjectData) => {
    const response = await fetch(API_ENDPOINTS.SUBJECT, {
        method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(subjectData),
    });
    if (!response.ok) {
        throw new Error('Failed to add subject');
    }
    const data: BackendSubject = await response.json();
    return {
        ...data, SubjectId: data.SubjectId!, semester: data.semester as BackendSemester
    };
});

export const updateSubject = createAsyncThunk<Subject, BackendSubject>('subject/updateSubject', async (subjectData) => {
    if (!subjectData.SubjectId) {
        throw new Error('subject_id is required for updating');
    }
    const response = await fetch(`${API_ENDPOINTS.SUBJECT}/${subjectData.SubjectId}`, {
        method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(subjectData),
    });
    if (!response.ok) {
        throw new Error('Failed to update Subject');
    }
    const data: BackendSubject = await response.json();
    return {
        ...data, SubjectId: data.SubjectId!, semester: subjectData.semester!,
    };
});

export const deleteSubject = createAsyncThunk<number, number>('subject/deleteSubject', async (id) => {
    const response = await fetch(`${API_ENDPOINTS.SUBJECT}/${id}`, {method: 'DELETE'});
    if (!response.ok) {
        throw new Error('Failed to delete Subject');
    }
    return id;
});

const subjectSlice = createSlice({
    name: 'subjects', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchSubject.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSubject.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSubject.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch subjects';
            })
            .addCase(addSubject.fulfilled, (state, action) => {
                state.rows.push(action.payload);
            })
            .addCase(updateSubject.fulfilled, (state, action) => {
                const index = state.rows.findIndex((row) => row.SubjectId === action.payload.SubjectId);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(deleteSubject.fulfilled, (state, action) => {
                state.rows = state.rows.filter((row) => row.SubjectId !== action.payload);
            });
    },
});

export default subjectSlice.reducer;
