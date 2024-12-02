import {createAsyncThunk, createSlice} from '@reduxjs/toolkit';
import {API_ENDPOINTS} from '../urls';
import {BackendTeacher, Teacher, TeachersState} from "../../utils/Interfaces";

const initialState: TeachersState = {
    rows: [], loading: false, error: null, singleTeacher: null,
};

export const fetchTeachers = createAsyncThunk<Teacher[]>('teachers/fetchTeachers', async () => {
    const response = await fetch(API_ENDPOINTS.TEACHERS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendTeacher[] = await response.json();
    return data.map(teacher => ({
        ...teacher, id: teacher.id!,
    }));
});

export const addTeacher = createAsyncThunk<Teacher, BackendTeacher>('teachers/addTeacher', async (teacher) => {
    const teacherData = {
        ...teacher, preferences: teacher.preferences || {},
    };
    const response = await fetch(API_ENDPOINTS.TEACHERS, {
        method: 'POST', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(teacherData),
    });
    if (!response.ok) {
        throw new Error('Failed to add teacher');
    }
    const data: BackendTeacher = await response.json();
    const adjustedTeacher: Teacher = {
        ...data, id: teacher.id!,
    };
    return adjustedTeacher;
},);

export const updateTeacher = createAsyncThunk<Teacher, BackendTeacher>('teachers/updateTeacher', async (teacherData) => {
    const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${teacherData.id}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(teacherData),
    });
    if (!response.ok) {
        throw new Error('Failed to update teacher');
    }
    const data: BackendTeacher = await response.json();
    const adjustedTeacher: Teacher = {
        ...data, id: data.id!,
    };
    return adjustedTeacher;
},);

export const deleteTeacher = createAsyncThunk<number, number>('teachers/deleteTeacher', async (id: number) => {
    const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete teacher');
    }
    return id;
},);

export const getTeacher = createAsyncThunk<Teacher, number>('teachers/getTeacher', async (id: number) => {
    const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${id}`);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendTeacher = await response.json();
    const adjustedTeacher: Teacher = {
        ...data, id: data.id!,
    };
    return adjustedTeacher;
});

const teacherSlice = createSlice({
    name: 'teachers', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchTeachers.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchTeachers.fulfilled, (state, action) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchTeachers.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch teachers';
            })
            .addCase(addTeacher.fulfilled, (state, action) => {
                state.rows.push(action.payload);
            })
            .addCase(updateTeacher.fulfilled, (state, action) => {
                const updatedTeacher = action.payload;
                const index = state.rows.findIndex((row) => row.id === updatedTeacher.id);
                if (index !== -1) {
                    state.rows[index] = updatedTeacher;
                }
            })
            .addCase(deleteTeacher.fulfilled, (state, action) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(getTeacher.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(getTeacher.fulfilled, (state, action) => {
                state.loading = false;
                state.singleTeacher = action.payload;
            })
            .addCase(getTeacher.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch teacher';
            });

    },
});

export default teacherSlice.reducer;
