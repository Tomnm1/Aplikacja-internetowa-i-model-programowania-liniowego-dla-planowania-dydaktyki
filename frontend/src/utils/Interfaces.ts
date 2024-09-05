import {GridRowId} from "@mui/x-data-grid";

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

export interface Teacher {
    id: number;
    name: string;
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