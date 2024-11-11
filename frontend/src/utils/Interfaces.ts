import {GridRowId, GridRowModesModel} from "@mui/x-data-grid";

export const degrees = {
    BRAK: "brak",
    LIC: "lic.",
    INZ: "inż.",
    MGR: "mgr",
    MGR_INZ: "mgr inż.",
    DR: "dr",
    DR_INZ: "dr inż.",
    DR_HAB: "dr hab.",
    DR_HAB_INZ: "dr hab. inż.",
    DR_PROF_PP: "dr, prof. PP",
    DR_INZ_PROF_PP: "dr inż., prof. PP",
    DR_HAB_PROF_PP: "dr hab., prof. PP",
    DR_HAB_INZ_PROF_PP: "dr hab. inż., prof. PP",
    PROF_DR_HAB: "prof. dr hab.",
    PROF_DR_HAB_INZ: "prof. dr hab. inż."
};

export enum Cycle {
    FIRST = 'first',
    SECOND = 'second',
}

export enum Day {
    MONDAY = 'monday',
    TUESDAY = 'tuesday',
    WEDNESDAY = 'wednesday',
    THURSDAY = 'thursday',
    FRIDAY = 'friday',
    SATURDAY = 'saturday',
    SUNDAY = 'sunday',
}

export enum Language {
    POLSKI = 'polski',
    ANGIELSKI = 'angielski',
}

export const languageMapping: { [key in Language] : string} = {
    [Language.ANGIELSKI]: 'angielski',
    [Language.POLSKI]: 'polski',
}
export const dayMapping: { [key in Day]: string } = {
    [Day.MONDAY]: 'Poniedziałek',
    [Day.TUESDAY]: 'Wtorek',
    [Day.WEDNESDAY]: 'Środa',
    [Day.THURSDAY]: 'Czwartek',
    [Day.FRIDAY]: 'Piątek',
    [Day.SATURDAY]: 'Sobota',
    [Day.SUNDAY]: 'Niedziela',
};

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
    id?: number;
    firstName: string;
    lastName: string;
    degree: string;
    preferences: { [key: string]: string };
    subjectTypesList: SubjectType[];
}

export interface Teacher {
    id: number;
    firstName: string;
    lastName: string;
    degree: string;
    preferences: { [key: string]: string };
    subjectTypesList: SubjectType[];
}

export interface TeachersState {
    rows: Teacher[];
    loading: boolean;
    error: string | null;
}

export interface Building {
    id: GridRowId;
    code: string;
    isNew?: boolean;
}

export interface BackendBuilding {
    buildingId: number;
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

export interface BackendClassroom {
    classroomID?: number;
    building: BackendBuilding;
    code: string;
    floor: number;
    capacity: number;
    equipment: { [key: string]: boolean };
}

export interface Classroom {
    id: GridRowId;
    buildingId: GridRowId;
    code: string;
    floor: number;
    capacity: number;
    equipment: string[];
    buildingCode?: string;
}

export interface ClassroomsState {
    rows: Classroom[];
    loading: boolean;
    error: string | null;
}

export interface FieldOfStudy {
    id: GridRowId;
    name: string;
    isNew?: boolean;
}

export interface BackendFieldOfStudies {
    fieldOfStudyId: number;
    name: string;
}

export interface FieldOfStudiesState {
    rows: FieldOfStudy[];
    rowModesModel: GridRowModesModel;
    selectedRowId: GridRowId | null;
    selectedRowName: string | null;
    loading: boolean;
    error: string | null;
}

export interface Specialisation {
    id: GridRowId;
    name: string;
    cycle: Cycle;
    fieldOfStudyId?: number;
    fieldOfStudyName?: string;
}

export interface BackendSpecialisation {
    specialisationId?: number;
    name: string;
    cycle: Cycle;
    fieldOfStudy: BackendFieldOfStudies;
}

export type LocalTime = string;

export interface Slot {
    slot_id: GridRowId;
    start_time: LocalTime;
    end_time: LocalTime;
    isNew?: boolean;
}

export interface BackendSlot {
    slotId: number;
    startTime: LocalTime;
    endTime: LocalTime;
}

export interface SlotsState {
    rows: Slot[];
    rowModesModel: GridRowModesModel;
    selectedRowId: GridRowId | null;
    selectedRowStart: LocalTime | null;
    selectedRowStop: LocalTime | null;
    loading: boolean;
    error: string | null;
}

export interface SlotsDay {
    id: number;
    slotId: number;
    day: Day;
    slotRepresentation?: string;
}

export interface BackendSlotsDay {
    SlotsDayId?: number;
    slot: {
        slotId: number;
        startTime?: string;
        endTime?: string;
    };
    day: Day;
}

export interface SlotsDayState {
    rows: SlotsDay[];
    loading: boolean;
    error: string | null;
}

export interface Semester {
    id: number;
    number: string;
    specialisationId: number;
    specialisationRepresentation?: string;
}

export interface BackendSemester {
    semesterId?: number;
    number: string;
    specialisation: {
        specialisationId: number;
        name?: string;
    };
}

export interface SemesterState {
    rows: Semester[];
    loading: boolean;
    error: string | null;
}

export interface Subject {
    SubjectId: number;
    name: string;
    language: Language;
    exam: boolean;
    mandatory: boolean;
    planned: boolean;
    semester: BackendSemester;
}

export interface BackendSubject {
    SubjectId?: number;
    name: string;
    language: Language;
    exam: boolean;
    mandatory: boolean;
    planned: boolean;
    semester: BackendSemester;
}

export interface SubjectState {
    rows: Subject[];
    loading: boolean;
    error: string | null;
}