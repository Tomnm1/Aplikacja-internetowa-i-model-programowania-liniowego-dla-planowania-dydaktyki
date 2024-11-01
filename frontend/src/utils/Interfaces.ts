import {GridRowId, GridRowModesModel} from "@mui/x-data-grid";

export interface ClassroomRow {
    id: GridRowId;
    name: string;
    capacity: number;
    floor: string;
    isNew: boolean;
}

export interface ClassroomState {
    rows: ClassroomRow[];
    rowModesModel: Record<GridRowId, { mode: string }>;
}


export interface Room {
    id: number;
    name: string;
}

export interface Hour {
    id: number;
    timeRange: string;
}

export interface Group {
    id: number;
    name: string;
}

export interface Schedule {
    id: number;
    teacherIds: number[];
    groupIds: number[];
    roomId: number;
    hourId: number;
}


export interface CalendarViewProps {
    teachers: Teacher[];
    groups: Group[];
    rooms: Room[];
    hours: Hour[];
    schedules: Schedule[];
}

export interface SubjectType {
    id: number;
    name: string;
}

export interface BackendTeacher {
    id: number;
    firstName: string;
    lastName: string;
    degree: string;
    preferences: { [key: string]: string };
    subjectTypesList: SubjectType[];
}

export interface Teacher {
    id: GridRowId;
    firstName: string;
    lastName: string;
    degree: string;
    preferences: { [key: string]: string };
    subjectTypesList: number[];
    isNew?: boolean;
}

export interface TeachersState {
    rows: Teacher[];
    rowModesModel: GridRowModesModel;
    selectedRowId: GridRowId | null;
    selectedRowName: string | null;
    loading: boolean;
    error: string | null;
}

export interface Building {
    id: GridRowId;
    code: string;
    isNew?: boolean;
}

export interface BackendBuilding {
    id: number;
    code: string;
}

export interface BuildingsState {
    rows: Building[];
    rowModesModel: GridRowModesModel;
    selectedRowId: GridRowId | null;
    selectedRowCode: string | null;
    loading: boolean;
    error: string | null;
}