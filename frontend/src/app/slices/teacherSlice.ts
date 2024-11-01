import { createSlice, PayloadAction, createAsyncThunk } from '@reduxjs/toolkit';
import { GridRowId, GridRowModesModel } from '@mui/x-data-grid';
import { API_ENDPOINTS } from '../urls';
import { Teacher, TeachersState, BackendTeacher, SubjectType } from "../../utils/Interfaces.ts";

const initialState: TeachersState = {
    rows: [],
    rowModesModel: {},
    selectedRowId: null,
    selectedRowName: null,
    loading: false,
    error: null,
};

export const fetchTeachers = createAsyncThunk<Teacher[]>('teachers/fetchTeachers', async () => {
    const response = await fetch(API_ENDPOINTS.TEACHERS);
    if (!response.ok) {
        throw new Error('Network response was not ok');
    }
    const data: BackendTeacher[] = await response.json();
    const adjustedData: Teacher[] = data.map(teacher => ({
        ...teacher,
        subjectTypesList: teacher.subjectTypesList.map(st => st.id),
    }));
    return adjustedData;
});

export const addTeacher = createAsyncThunk<{ tempId: GridRowId; teacher: Teacher }, Teacher>(
    'teachers/addTeacher',
    async (teacher: Teacher) => {
        const teacherData = {
            firstName: teacher.firstName,
            lastName: teacher.lastName,
            degree: teacher.degree,
            preferences: teacher.preferences || {},
            subjectTypesList: (teacher.subjectTypesList || []).map(id => ({ id } as SubjectType)),
        };
        const response = await fetch(API_ENDPOINTS.TEACHERS, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(teacherData),
        });
        if (!response.ok) {
            throw new Error('Failed to add teacher');
        }
        const data: BackendTeacher = await response.json();
        const adjustedTeacher: Teacher = {
            ...data,
            subjectTypesList: data.subjectTypesList.map(st => st.id),
        };
        return { tempId: teacher.id, teacher: adjustedTeacher };
    },
);

export const updateTeacher = createAsyncThunk<Teacher, Teacher>(
    'teachers/updateTeacher',
    async (teacher: Teacher) => {
        const teacherData = {
            firstName: teacher.firstName,
            lastName: teacher.lastName,
            degree: teacher.degree,
            preferences: teacher.preferences || {},
            subjectTypesList: (teacher.subjectTypesList || []).map(id => ({ id } as SubjectType)),
        };
        const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${teacher.id}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(teacherData),
        });
        if (!response.ok) {
            throw new Error('Failed to update teacher');
        }
        const data: BackendTeacher = await response.json();
        const adjustedTeacher: Teacher = {
            ...data,
            subjectTypesList: data.subjectTypesList.map(st => st.id),
        };
        return adjustedTeacher;
    },
);

export const deleteTeacher = createAsyncThunk<GridRowId, GridRowId>(
    'teachers/deleteTeacher',
    async (id: GridRowId) => {
        const response = await fetch(`${API_ENDPOINTS.TEACHERS}/${id}`, {
            method: 'DELETE',
        });
        if (!response.ok) {
            throw new Error('Failed to delete teacher');
        }
        return id;
    },
);

const teacherSlice = createSlice({
    name: 'teachers',
    initialState,
    reducers: {
        setRowModesModel: (state, action: PayloadAction<GridRowModesModel>) => {
            state.rowModesModel = action.payload;
        },
        setSelectedRow: (state, action: PayloadAction<{ id: GridRowId; name: string }>) => {
            state.selectedRowId = action.payload.id;
            state.selectedRowName = action.payload.name;
        },
        clearSelectedRow: (state) => {
            state.selectedRowId = null;
            state.selectedRowName = null;
        },
        addNewTeacher: (state, action: PayloadAction<Teacher>) => {
            state.rows.push(action.payload);
        },
        removeNewTeacher: (state, action: PayloadAction<GridRowId>) => {
            state.rows = state.rows.filter((row) => row.id !== action.payload);
        },
    },
    extraReducers: (builder) => {
        builder
            .addCase(fetchTeachers.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchTeachers.fulfilled, (state, action: PayloadAction<Teacher[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchTeachers.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch teachers';
            })
            .addCase(addTeacher.fulfilled, (state, action) => {
                const { tempId, teacher } = action.payload;
                const index = state.rows.findIndex((row) => row.id === tempId);
                if (index !== -1) {
                    state.rows[index] = teacher;
                } else {
                    state.rows.push(teacher);
                }
                // Aktualizuj rowModesModel z nowym id
                if (state.rowModesModel[tempId]) {
                    state.rowModesModel[teacher.id] = state.rowModesModel[tempId];
                    delete state.rowModesModel[tempId];
                }
            })
            .addCase(addTeacher.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add teacher';
            })
            .addCase(updateTeacher.fulfilled, (state, action: PayloadAction<Teacher>) => {
                const updatedTeacher = action.payload;
                const index = state.rows.findIndex((row) => row.id === updatedTeacher.id);
                if (index !== -1) {
                    state.rows[index] = updatedTeacher;
                }
            })
            .addCase(updateTeacher.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update teacher';
            })
            .addCase(deleteTeacher.fulfilled, (state, action: PayloadAction<GridRowId>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteTeacher.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete teacher';
            });
    },
});

export const {
    setRowModesModel,
    setSelectedRow,
    clearSelectedRow,
    addNewTeacher,
    removeNewTeacher,
} = teacherSlice.actions;

export default teacherSlice.reducer;
