import {createAsyncThunk, createSlice, PayloadAction} from '@reduxjs/toolkit';
import {BackendClassroom, Classroom, ClassroomsState} from '../../utils/Interfaces';
import {API_ENDPOINTS} from '../urls';

const initialState: ClassroomsState = {
    rows: [], loading: false, error: null,
};

export const fetchClassrooms = createAsyncThunk<Classroom[]>('classrooms/fetchClassrooms', async () => {
    const response = await fetch(API_ENDPOINTS.CLASSROOMS);
    if (!response.ok) {
        throw new Error('Failed to fetch classrooms');
    }
    const data: BackendClassroom[] = await response.json();

    const adjustedData: Classroom[] = data.map((classroom) => {
        const equipmentArray = Object.keys(classroom.equipment || {}).filter((key) => classroom.equipment[key]);
        return {
            id: classroom.classroomID!,
            buildingId: classroom.building.buildingId,
            buildingCode: classroom.building.code,
            code: classroom.code,
            floor: classroom.floor,
            capacity: classroom.capacity,
            equipment: equipmentArray,
        };
    });
    return adjustedData;
});

export const addClassroom = createAsyncThunk<Classroom, BackendClassroom>('classrooms/addClassroom', async (classroomData) => {
    const response = await fetch(API_ENDPOINTS.CLASSROOMS, {
        method: 'POST', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(classroomData),
    });
    if (!response.ok) {
        throw new Error('Failed to add classroom');
    }
    const data: BackendClassroom = await response.json();
    if (data.classroomID == null) {
        throw new Error('classroomId is missing in the response');
    }

    const equipmentArray = Object.keys(data.equipment || {}).filter((key) => data.equipment[key]);

    const adjustedClassroom: Classroom = {
        id: data.classroomID,
        buildingId: data.building.buildingId,
        buildingCode: data.building.code,
        code: data.code,
        floor: data.floor,
        capacity: data.capacity,
        equipment: equipmentArray,
    };
    return adjustedClassroom;
});

export const updateClassroom = createAsyncThunk<Classroom, BackendClassroom>('classrooms/updateClassroom', async (classroomData) => {
    if (classroomData.classroomID == null) {
        throw new Error('classroomId is required for updating');
    }
    const response = await fetch(`${API_ENDPOINTS.CLASSROOMS}/${classroomData.classroomID}`, {
        method: 'PUT', headers: {
            'Content-Type': 'application/json',
        }, body: JSON.stringify(classroomData),
    });
    if (!response.ok) {
        throw new Error('Failed to update classroom');
    }
    const data: BackendClassroom = await response.json();
    if (data.classroomID == null) {
        throw new Error('classroomId is missing in the response');
    }

    const equipmentArray = Object.keys(data.equipment || {}).filter((key) => data.equipment[key]);

    const adjustedClassroom: Classroom = {
        id: data.classroomID,
        buildingId: data.building.buildingId,
        buildingCode: data.building.code,
        code: data.code,
        floor: data.floor,
        capacity: data.capacity,
        equipment: equipmentArray,
    };
    return adjustedClassroom;
});
export const deleteClassroom = createAsyncThunk<number, number>('classrooms/deleteClassroom', async (id) => {
    const response = await fetch(`${API_ENDPOINTS.CLASSROOMS}/${id}`, {
        method: 'DELETE',
    });
    if (!response.ok) {
        throw new Error('Failed to delete classroom');
    }
    return id;
});

const classroomsSlice = createSlice({
    name: 'classrooms', initialState, reducers: {}, extraReducers: (builder) => {
        builder
            .addCase(fetchClassrooms.pending, (state) => {
                state.loading = true;
                state.error = null;
            })
            .addCase(fetchClassrooms.fulfilled, (state, action: PayloadAction<Classroom[]>) => {
                state.loading = false;
                state.rows = action.payload;
            })
            .addCase(fetchClassrooms.rejected, (state, action) => {
                state.loading = false;
                state.error = action.error.message || 'Failed to fetch classrooms';
            })
            .addCase(addClassroom.fulfilled, (state, action: PayloadAction<Classroom>) => {
                state.rows.push(action.payload);
            })
            .addCase(addClassroom.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to add classroom';
            })
            .addCase(updateClassroom.fulfilled, (state, action: PayloadAction<Classroom>) => {
                const index = state.rows.findIndex((row) => row.id === action.payload.id);
                if (index !== -1) {
                    state.rows[index] = action.payload;
                }
            })
            .addCase(updateClassroom.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to update classroom';
            })
            .addCase(deleteClassroom.fulfilled, (state, action: PayloadAction<number>) => {
                state.rows = state.rows.filter((row) => row.id !== action.payload);
            })
            .addCase(deleteClassroom.rejected, (state, action) => {
                state.error = action.error.message || 'Failed to delete classroom';
            });
    },
});

export default classroomsSlice.reducer;
