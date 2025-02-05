import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {API_ENDPOINTS} from '../urls';
import {BackendSemester, Semester, SemesterState} from '../../utils/Interfaces';
import {fetchWithAuth} from "../fetchWithAuth.ts";

const initialState: SemesterState = {
    rows: [], loading: false, error: null,
};

export const fetchSemesters = createAsyncThunk<Semester[]>('semesters/fetchWithAuthSemesters', async () => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.SEMESTERS}/DTO`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendSemester[] = await response.json();
    return data.map((semester) => ({
        id: semester.semesterId!,
        number: semester.number,
        specialisationId: semester.specialisation.specialisationId!,
        specialisationRepresentation: semester.specialisation.name,
        fieldOfStudyName: semester.specialisation.fieldOfStudy?.name,
        fieldOfStudyTyp: semester.specialisation.fieldOfStudy?.typ,
        cycle: semester.specialisation.cycle,
        groupCount: semester.groupCount,
        typ: semester.typ,
    }));
});

export const addSemester = createAsyncThunk<Semester, BackendSemester>('semesters/addSemester', async (semesterData) => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.SEMESTERS}/DTO`, {
        method: 'POST', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(semesterData),
    });
    if (!response.ok) {
        throw new Error('Failed to add semester');
    }
    const data: BackendSemester = await response.json();
    return {
        id: data.semesterId!,
        number: data.number,
        specialisationId: data.specialisation.specialisationId!,
        specialisationRepresentation: data.specialisation.name,
        fieldOfStudyName: data.specialisation.fieldOfStudy?.name,
        fieldOfStudyTyp: data.specialisation.fieldOfStudy?.typ,
        cycle: data.specialisation.cycle,
        typ: data.typ,
    };
});

export const updateSemester = createAsyncThunk<Semester, BackendSemester>('semesters/updateSemester', async (semesterData) => {
    if (!semesterData.semesterId) {
        throw new Error('SemesterId is required for updating');
    }
    const response = await fetchWithAuth(`${API_ENDPOINTS.SEMESTERS}/${semesterData.semesterId}`, {
        method: 'PUT', headers: {'Content-Type': 'application/json'}, body: JSON.stringify(semesterData),
    });
    if (!response.ok) {
        throw new Error('Failed to update semester');
    }
    const data: BackendSemester = await response.json();
    return {
        id: data.semesterId!,
        number: data.number,
        specialisationId: data.specialisation.specialisationId!,
        specialisationRepresentation: data.specialisation.name,
        fieldOfStudyName: data.specialisation.fieldOfStudy?.name,
        fieldOfStudyTyp: data.specialisation.fieldOfStudy?.typ,
        cycle: data.specialisation.cycle,
        typ: data.typ,
    };
});

export const deleteSemester = createAsyncThunk<number, number>('semesters/deleteSemester', async (id) => {
    const response = await fetchWithAuth(`${API_ENDPOINTS.SEMESTERS}/${id}`, {method: 'DELETE'});
    if (!response.ok) {
        throw new Error('Failed to delete semester');
    }
    return id;
});

const semesterSlice = createSlice({
    name: 'semesters', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchSemesters.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchSemesters.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchSemesters.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetchWithAuth semesters';
            })
            .addCase(addSemester.fulfilled, (state, action) => {
                state.rows.push(action.payload);
            })
            .addCase(updateSemester.fulfilled, (state, action) => {
                const index = state.rows.findIndex((row) => row.id === action.payload.id);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(deleteSemester.fulfilled, (state, action) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            });
    },
});

export default semesterSlice.reducer;
