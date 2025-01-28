import {useEffect, useMemo, useState} from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import {
    Autocomplete, CircularProgress, FormControl, InputLabel, MenuItem, Popover, Select, Switch, TextField, Typography
} from '@mui/material';
import {EventInput} from '@fullcalendar/core';
import {EventApi, EventClickArg, EventContentArg} from 'fullcalendar';
import {useSnackbar} from 'notistack';
import API_ENDPOINTS from '../app/urls.ts';
import {
    BackendClassroom,
    BackendSemester,
    BackendTeacher,
    cycleMapping,
    Day,
    dayMapping,
    GeneratedPlanDTO,
    Plan,
    typeMapping,
} from '../utils/Interfaces.ts';
import {useAppSelector} from "../hooks/hooks.ts";
import {RootState} from "../app/store.ts";
import CalendarSemester from './CalendarSemester.tsx';
import ClassroomTable from "./CalendarClassroom.tsx";
import CalendarTeacher from "./CalendarTeacher.tsx";
import {fetchWithAuth} from "../app/fetchWithAuth.ts";

type ContextType = 'teacher' | 'semester' | 'classroom';

const contextOptions = [{value: 'teacher', label: 'Nauczyciel'}, {
    value: 'semester',
    label: 'Semestr'
}, {value: 'classroom', label: 'Sala'}];
const dayToIndex: { [key in Day]: number } = {
    [Day.SUNDAY]: 0,
    [Day.MONDAY]: 1,
    [Day.TUESDAY]: 2,
    [Day.WEDNESDAY]: 3,
    [Day.THURSDAY]: 4,
    [Day.FRIDAY]: 5,
    [Day.SATURDAY]: 6,
};

const colorPalette = ["#fbb4ae", "#b3cde3", "#ccebc5", "#decbe4", "#fed9a6", "#ffe9a8", "#fddaec", "#e2f0d9", "#f6eac2", "#d5e8d4", "#d5a6bd", "#c2c2f0", "#f2c2f0", "#c2f0f2", "#f2f0c2", "#f0f2c2", "#c2f0c2", "#f0c2c2", "#c2c2f0"];

const Timetable = () => {
    const [context, setContext] = useState<ContextType>('teacher');
    const {user, role} = useAppSelector((state: RootState) => state.auth);
    const [selectedItem, setSelectedItem] = useState<{ id: number | undefined; label: string } | null>({
        id: undefined,
        label: ""
    });
    const [selectedEvent, setSelectedEvent] = useState<EventApi | null>(null);
    const [selectedPlan, setSelectedPlan] = useState<number>(0);
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
    const {enqueueSnackbar} = useSnackbar();

    const [classrooms, setClassrooms] = useState<BackendClassroom[]>([]);
    const [semesters, setSemester] = useState<BackendSemester[]>([]);
    const [teachers, setTeachers] = useState<BackendTeacher[]>([]);
    const [plans, setPlans] = useState<Plan[]>([]);
    const [events, setEvents] = useState<EventInput[]>([]);
    const [clusters, setClusters] = useState<any[]>([]);
    const [allGroupCodes, setAllGroupCodes] = useState<string[]>([]);
    const [subjectColorMap, setSubjectColorMap] = useState<{ [subject: string]: string }>({});
    const [loading, setLoading] = useState<boolean>(true);
    const [eventsLoading, setEventsLoading] = useState<boolean>(false);
    const [isTableView, setIsTableView] = useState<boolean>(false);

    const contextItems = useMemo(() => ({
        teacher: [{id: 0, label: 'Wszyscy nauczyciele'}, ...teachers.map((teacher) => ({
            id: teacher.id,
            label: `${teacher.firstName} ${teacher.secondName ? teacher.secondName : ''} ${teacher.lastName}`,
        })),], semester: semesters.map((semester) => ({
            id: semester.semesterId,
            label: `${semester.number} - (${semester.specialisation.name} - ${semester.specialisation.fieldOfStudy?.name} - ${cycleMapping[semester.specialisation.cycle]})`,
        })), classroom: [{id: 0, label: 'Wszystkie sale'}, ...classrooms.map((classroom) => ({
            id: classroom.classroomID, label: `${classroom.code} (${classroom.building.code})`
        })),],
    }), [teachers, semesters, classrooms]);

    useEffect(() => {
        setLoading(true);
        Promise.all([fetchWithAuth(API_ENDPOINTS.TEACHERS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania nauczycieli: ${error.message}`, {variant: 'error'});
                return [];
            }), fetchWithAuth(API_ENDPOINTS.SEMESTERS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania semestrow: ${error.message}`, {variant: 'error'});
                return [];
            }), fetchWithAuth(API_ENDPOINTS.CLASSROOMS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania sal: ${error.message}`, {variant: 'error'});
                return [];
            }), fetchWithAuth(API_ENDPOINTS.PLANS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania planów: ${error.message}`, {variant: 'error'});
                return [];
            }),])
            .then(([teachersData, semestersData, classroomsData, plansData]: [any, any, any, Plan[]]) => {
                setTeachers(teachersData);
                setSemester(semestersData);
                setClassrooms(classroomsData);
                setPlans(plansData);
                setSelectedPlan((): number => {
                    return plansData.find((plan: Plan) => plan.published)?.planId || 0;
                });
                setLoading(false);
            })
            .catch(() => {
                setLoading(false);
            });
    }, [enqueueSnackbar]);

    useEffect(() => {
        if (role === 'ROLE_TEACHER') {
            setContext('teacher');
            const foundTeacher = contextItems.teacher.find((teacher) => teacher.id! === user!.id!);
            if (foundTeacher) {
                setSelectedItem(foundTeacher);
            }
        }
    }, [role, contextItems, user]);

    useEffect(() => {
        if (role === 'ROLE_ADMIN') {
            console.log(selectedItem)
            if (contextItems[context] && contextItems[context].length > 0) {
                setSelectedItem(contextItems[context][0]);
            }
            if (plans.length > 0 && selectedPlan === 0) {
                setSelectedPlan(plans[0].planId);
            }

            setEvents([]);
        }
    }, [context, contextItems, role, user, plans, selectedPlan, selectedEvent]);

    useEffect(() => {
        console.log(selectedItem)

        if (context === "classroom" && selectedItem!.id === 0) {
            setIsTableView(true);
        }
        if (context === "classroom" && selectedItem!.id !== 0) {
            setIsTableView(false);
        }
        if (context === "teacher" && selectedItem!.id === 0) {
            setIsTableView(true);
        }
        if (context === "teacher" && selectedItem!.id !== 0) {
            setIsTableView(false);
        }
        if (context === "semester") {
            setIsTableView(true);
        }
    }, [contextItems, selectedItem, plans, context, selectedPlan, context]);

    const formatSlotTime = (timeStr: string) => {
        return timeStr.slice(0, 5);
    };

    useEffect(() => {
        if (!selectedItem || selectedPlan === 0) return;

        setEventsLoading(true);

        let endpoint = '';

        switch (context) {
            case 'teacher':
                if (selectedItem!.id === 0) {
                    endpoint = API_ENDPOINTS.GENERATED_PLAN_ALL(selectedPlan);
                } else {
                    endpoint = API_ENDPOINTS.GENERATED_PLAN_TEACHERS(role === "ROLE_ADMIN" ?  selectedItem.id! : user!.id!, selectedPlan);
                }
                break;
            case 'semester':
                endpoint = API_ENDPOINTS.GENERATED_PLAN_SEMESTER(selectedItem.id!, selectedPlan);
                break;
            case 'classroom':
                if (selectedItem.id === 0) {
                    endpoint = API_ENDPOINTS.GENERATED_PLAN_ALL(selectedPlan);
                } else {
                    endpoint = API_ENDPOINTS.GENERATED_PLAN_CLASSROOM(selectedItem.id!, selectedPlan);
                }
                break;
            default:
                break;
        }

        if (endpoint) {
            fetchWithAuth(endpoint)
                .then((res) => res.json())
                .then((data: GeneratedPlanDTO[]) => {
                    const initialGroupedData: { [key: string]: any } = {};

                    data.forEach((item) => {
                        const key = `${item.slotsDay.day}|${item.slotsDay.slot.startTime}|${item.slotsDay.slot.endTime}|${item.teacherId}|${item.classroomId}|${item.isEvenWeek}|${item.subjectName}`;

                        if (!initialGroupedData[key]) {
                            initialGroupedData[key] = {
                                ...item, groupCodes: [item.groupCode],
                            };
                        } else {
                            if (!initialGroupedData[key].groupCodes.includes(item.groupCode)) {
                                initialGroupedData[key].groupCodes.push(item.groupCode);
                            }
                        }
                    });

                    const finalGroupedData: { [key: string]: any } = {};

                    Object.values(initialGroupedData).forEach((item: any) => {
                        const key = `${item.slotsDay.day}|${item.slotsDay.slot.startTime}|${item.slotsDay.slot.endTime}|${item.teacherId}|${item.classroomId}|${item.subjectName}`;

                        if (!finalGroupedData[key]) {
                            finalGroupedData[key] = {
                                ...item, isEvenWeeks: [item.isEvenWeek],
                            };
                        } else {
                            if (!finalGroupedData[key].isEvenWeeks.includes(item.isEvenWeek)) {
                                finalGroupedData[key].isEvenWeeks.push(item.isEvenWeek);
                            }
                            finalGroupedData[key].groupCodes = Array.from(new Set([...finalGroupedData[key].groupCodes, ...item.groupCodes]));
                        }
                    });

                    const eventsData = Object.values(finalGroupedData).map((item: any) => {
                        const dayOfWeek = dayToIndex[item.slotsDay.day as Day];
                        const isBothWeeks = item.isEvenWeeks.length > 1;
                        return {
                            id: item.id.toString(),
                            title: item.subjectName,
                            daysOfWeek: [dayOfWeek],
                            startTime: item.slotsDay.slot.startTime,
                            endTime: item.slotsDay.slot.endTime,
                            extendedProps: {
                                subjectType: typeMapping[item.classTypeOwn as keyof typeof typeMapping],
                                teacher: `${item.teacherFirstName.charAt(0)}. ${item.teacherLastName}`,
                                group: item.groupCodes.join(', '),
                                classroom: `Sala: ${item.classroomCode}`,
                                isEvenWeek: isBothWeeks ? null : item.isEvenWeeks[0],
                                dayName: dayMapping[item.slotsDay.day as keyof typeof dayMapping],
                                semester: `${item.fieldOfStudyName} Semestr: ${item.specializationSemester}  Specjalizacja: ${item.specializationName}`,
                            },
                        } as EventInput;
                    });

                    setEvents(eventsData);

                    if (context === 'semester') {
                        type ClusterKey = {
                            day: Day;
                            timeRange: string;
                            subjectName: string;
                            teacher: string;
                            classroom: number;
                            classroomName: string;
                            buildingName: string;
                            isEvenWeek: boolean | null;
                            subjectType: string;
                        };

                        const clusterMap: Map<string, { key: ClusterKey; groupCodes: string[] }> = new Map();
                        const allGroupsSet = new Set<string>();

                        Object.values(finalGroupedData).forEach((item: any) => {
                            const day = item.slotsDay.day;
                            const startTime = formatSlotTime(item.slotsDay.slot.startTime);
                            const endTime = formatSlotTime(item.slotsDay.slot.endTime);
                            const timeRange = `${startTime}-${endTime}`;
                            const subjectName = item.subjectName;
                            const teacher = `${item.teacherFirstName.charAt(0)}. ${item.teacherLastName}`;
                            const classroom = item.classroomId;
                            const classroomName = item.classroomCode;
                            const buildingName = item.buildingName;
                            const isBothWeeks = item.isEvenWeeks.length > 1 ? null : item.isEvenWeeks[0];
                            const subjectType = typeMapping[item.classTypeOwn as keyof typeof typeMapping];
                            const cKey: ClusterKey = {
                                day,
                                timeRange,
                                subjectName,
                                teacher,
                                classroom,
                                classroomName,
                                buildingName,
                                isEvenWeek: isBothWeeks,
                                subjectType,
                            };
                            const mapKey = JSON.stringify(cKey);

                            const groups = item.groupCodes;
                            groups.forEach((g: string) => allGroupsSet.add(g));
                            if (!clusterMap.has(mapKey)) {
                                clusterMap.set(mapKey, {key: cKey, groupCodes: groups});
                            } else {
                                const oldVal = clusterMap.get(mapKey)!;
                                oldVal.groupCodes = Array.from(new Set([...oldVal.groupCodes, ...groups]));
                            }
                        });

                        const allGroupCodesArray = Array.from(allGroupsSet);
                        allGroupCodesArray.sort();

                        const clusterArray = Array.from(clusterMap.values());
                        clusterArray.sort((a, b) => {
                            const dayDiff = dayToIndex[a.key.day] - dayToIndex[b.key.day];
                            if (dayDiff !== 0) return dayDiff;
                            const [aStart] = a.key.timeRange.split('-');
                            const [bStart] = b.key.timeRange.split('-');
                            return aStart.localeCompare(bStart);
                        });

                        const subjects = Array.from(new Set(clusterArray.map((c) => c.key.subjectName)));
                        const newSubjectColorMap: { [subj: string]: string } = {};
                        subjects.forEach((subj, index) => {
                            newSubjectColorMap[subj] = colorPalette[index % colorPalette.length];
                        });
                        setSubjectColorMap(newSubjectColorMap);

                        setClusters(clusterArray);
                        setAllGroupCodes(allGroupCodesArray);
                    } else if (context === 'classroom') {
                        type ClusterKey = {
                            day: Day;
                            timeRange: string;
                            subjectName: string;
                            teacher: string;
                            classroom: number;
                            isEvenWeek: boolean | null;
                            subjectType: string;
                        };

                        const clusterMap: Map<string, { key: ClusterKey; groupCodes: string[] }> = new Map();
                        const allClassroomsSet = new Set<string>();

                        Object.values(finalGroupedData).forEach((item: any) => {
                            const day = item.slotsDay.day;
                            const startTime = formatSlotTime(item.slotsDay.slot.startTime);
                            const endTime = formatSlotTime(item.slotsDay.slot.endTime);
                            const timeRange = `${startTime}-${endTime}`;
                            const subjectName = item.subjectName;
                            const teacher = `${item.teacherFirstName.charAt(0)}. ${item.teacherLastName}`;
                            const classroom = item.classroomId;
                            const isBothWeeks = item.isEvenWeeks.length > 1 ? null : item.isEvenWeeks[0];
                            const subjectType = typeMapping[item.classTypeOwn as keyof typeof typeMapping];

                            const cKey: ClusterKey = {
                                day, timeRange, subjectName, teacher, classroom, isEvenWeek: isBothWeeks, subjectType,
                            };
                            const mapKey = JSON.stringify(cKey);

                            const groups = item.groupCodes;
                            groups.forEach((g: string) => allClassroomsSet.add(g));
                            if (!clusterMap.has(mapKey)) {
                                clusterMap.set(mapKey, {key: cKey, groupCodes: groups});
                            } else {
                                const oldVal = clusterMap.get(mapKey)!;
                                oldVal.groupCodes = Array.from(new Set([...oldVal.groupCodes, ...groups]));
                            }
                        });

                        const allClassroomsArray = Array.from(allClassroomsSet);
                        allClassroomsArray.sort();

                        const clusterArray = Array.from(clusterMap.values());
                        clusterArray.sort((a, b) => {
                            const dayDiff = dayToIndex[a.key.day] - dayToIndex[b.key.day];
                            if (dayDiff !== 0) return dayDiff;
                            const [aStart] = a.key.timeRange.split('-');
                            const [bStart] = b.key.timeRange.split('-');
                            return aStart.localeCompare(bStart);
                        });
                        const subjects = Array.from(new Set(clusterArray.map((c) => c.key.subjectName)));
                        const newSubjectColorMap: { [subj: string]: string } = {};
                        subjects.forEach((subj, index) => {
                            newSubjectColorMap[subj] = colorPalette[index % colorPalette.length];
                        });
                        setSubjectColorMap(newSubjectColorMap);

                        setClusters(clusterArray);
                        setAllGroupCodes([]);
                    }
                    else if (context === 'teacher') {
                        type ClusterKey = {
                            day: Day;
                            timeRange: string;
                            subjectName: string;
                            classroom: string;
                            isEvenWeek: boolean | null;
                            subjectType: string;
                            teacherId: number;
                        };

                        const clusterMap: Map<string, { key: ClusterKey; groupCodes: string[] }> = new Map();
                        const allGroupsSet = new Set<string>();

                        Object.values(finalGroupedData).forEach((item: any) => {
                            const day = item.slotsDay.day;
                            const startTime = formatSlotTime(item.slotsDay.slot.startTime);
                            const endTime = formatSlotTime(item.slotsDay.slot.endTime);
                            const timeRange = `${startTime}-${endTime}`;
                            const subjectName = item.subjectName;
                            const classroom = item.classroomCode;
                            const isBothWeeks = item.isEvenWeeks.length > 1 ? null : item.isEvenWeeks[0];
                            const subjectType = typeMapping[item.classTypeOwn as keyof typeof typeMapping];
                            const teacherId = item.teacherId;

                            const cKey: ClusterKey = {
                                day, timeRange, subjectName, classroom, isEvenWeek: isBothWeeks, subjectType, teacherId
                            };
                            const mapKey = JSON.stringify(cKey);

                            const groups = item.groupCodes;
                            groups.forEach((g: string) => allGroupsSet.add(g));
                            if (!clusterMap.has(mapKey)) {
                                clusterMap.set(mapKey, {key: cKey, groupCodes: groups});
                            } else {
                                const oldVal = clusterMap.get(mapKey)!;
                                oldVal.groupCodes = Array.from(new Set([...oldVal.groupCodes, ...groups]));
                            }
                        });

                        const allGroupCodesArray = Array.from(allGroupsSet);
                        allGroupCodesArray.sort();

                        const clusterArray = Array.from(clusterMap.values());
                        clusterArray.sort((a, b) => {
                            const dayDiff = dayToIndex[a.key.day] - dayToIndex[b.key.day];
                            if (dayDiff !== 0) return dayDiff;
                            const [aStart] = a.key.timeRange.split('-');
                            const [bStart] = b.key.timeRange.split('-');
                            return aStart.localeCompare(bStart);
                        });

                        const subjects = Array.from(new Set(clusterArray.map((c) => c.key.subjectName)));
                        const newSubjectColorMap: { [subj: string]: string } = {};
                        subjects.forEach((subj, index) => {
                            newSubjectColorMap[subj] = colorPalette[index % colorPalette.length];
                        });
                        setSubjectColorMap(newSubjectColorMap);

                        setClusters(clusterArray);
                        setAllGroupCodes(allGroupCodesArray);
                    } else {
                        setClusters([]);
                        setAllGroupCodes([]);
                        setSubjectColorMap({});
                    }

                    setEventsLoading(false);
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas pobierania planu: ${error.message}`, {variant: 'error'});
                    setEvents([]);
                    setClusters([]);
                    setAllGroupCodes([]);
                    setSubjectColorMap({});
                    setEventsLoading(false);
                });
        } else {
            setEvents([]);
            setClusters([]);
            setAllGroupCodes([]);
            setSubjectColorMap({});
            setEventsLoading(false);
        }
    }, [context, selectedItem, selectedPlan, enqueueSnackbar]);

    const handleEventClick = (clickInfo: EventClickArg) => {
        setSelectedEvent(clickInfo.event);
        setAnchorEl(clickInfo.el);
        clickInfo.jsEvent.preventDefault();
    };

    const handlePopoverClose = () => {
        setAnchorEl(null);
        setSelectedEvent(null);
    };

    const openPopover = Boolean(anchorEl);

    const formatTime = (date: Date | null | undefined) => {
        return date ? date.toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'}) : '';
    };

    const renderEventContent = (eventInfo: EventContentArg) => {
        return (<div
            className="flex flex-col items-center justify-center h-full min-w-8"
            style={{padding: '1px', lineHeight: 1}}
        >
            <b style={{fontSize: '0.6em', marginBottom: '1px'}}>{eventInfo.timeText}</b>
            <div style={{fontWeight: 'bold', fontSize: '0.7em'}}>{eventInfo.event.title}</div>
            <div style={{fontSize: '0.6em'}}>{eventInfo.event.extendedProps.subjectType}</div>
            <div style={{fontSize: '0.6em'}}>{eventInfo.event.extendedProps.teacher}</div>
            {eventInfo.event.extendedProps.isEvenWeek !== null && (<div style={{fontSize: '0.6em'}}>
                {eventInfo.event.extendedProps.isEvenWeek ? 'Tydzień parzysty' : 'Tydzień nieparzysty'}
            </div>)}
            <div style={{fontWeight: 'bold', fontSize: '0.7em', marginTop: '1px'}}>
                {eventInfo.event.extendedProps.classroom}
            </div>
        </div>);
    };

    if (loading) {
        return (<div className="flex items-center justify-center p-5">
            <CircularProgress/>
        </div>);
    }


    return (<div className={'py-8 px-2 w-full h-full'}>
        {role === 'ROLE_ADMIN' && (<div className={'flex gap-2 w-full items-center'}>
            <FormControl sx={{width: 300}} disabled={loading}>
                <InputLabel id="plan-label">Plan</InputLabel>
                <Select
                    autoWidth
                    labelId="plan-label"
                    value={selectedPlan}
                    onChange={(event) => {
                        setSelectedPlan(event.target.value as number);
                    }}
                    label="Plan"
                    variant="outlined"
                >
                    {Object.values(plans).map((plan) => (<MenuItem key={plan.planId} value={plan.planId}>
                        {`${plan.name} ${new Date(plan.creationDate).toLocaleString()}`}
                    </MenuItem>))}
                </Select>
            </FormControl>
            <Autocomplete
                key={context}
                sx={{width: 300}}
                options={contextOptions}
                getOptionLabel={(option) => option.label}
                value={contextOptions.find((option) => option.value === context) || null}
                onChange={(_event, newValue) => {
                    if (newValue) {
                        const newContext = newValue.value as ContextType;
                        setContext(newContext);
                    }
                }}
                renderInput={(params) => (<TextField {...params} label="Kontekst" variant="outlined" fullWidth/>)}
            />
            <Autocomplete
                key={selectedItem ? selectedItem.id : 'select'}
                sx={{width: 300}}
                options={contextItems[context]}
                getOptionLabel={(option) => option.label}
                value={selectedItem}
                onChange={(_event, newValue) => {
                    newValue === null ? setSelectedItem(contextItems[context][0]) : setSelectedItem(newValue);
                }}
                renderOption={(props, option) => (<li {...props} key={option.id}>
                    {option.label}
                </li>)}
                renderInput={(params) => (<TextField
                    {...params}
                    label={contextOptions.find((option) => option.value === context)?.label || ''}
                    variant="outlined"
                    fullWidth
                />)}
            />
            {(context === 'semester') && (<div className="flex items-center">
                <Typography>Widok kalendarza</Typography>
                <Switch
                    checked={isTableView}
                    onChange={(e) => setIsTableView(e.target.checked)}
                />
                <Typography>Widok tabeli</Typography>
            </div>)}
        </div>)}

        {eventsLoading ? (<div className="flex items-center justify-center p-5 ">
            <CircularProgress/>
        </div>) : (<>
            {context === 'semester' && isTableView ? (<CalendarSemester
                clusters={clusters}
                allGroupCodes={allGroupCodes}
                subjectColorMap={subjectColorMap}
                dayMapping={dayMapping}
                dayToIndex={dayToIndex}
            />) : context === 'classroom' && isTableView ? (<ClassroomTable
                clusters={clusters}
                allClassrooms={classrooms}
                subjectColorMap={subjectColorMap}
                dayMapping={dayMapping}
                dayToIndex={dayToIndex}
            />) : context === 'teacher' && isTableView ? (<CalendarTeacher
                clusters={clusters}
                allTeachers={teachers}
                subjectColorMap={subjectColorMap}
                dayMapping={dayMapping}
                dayToIndex={dayToIndex}
            />) : (<div className={'w-full'}>
                <FullCalendar
                    plugins={[timeGridPlugin]}
                    initialView="timeGridWeek"
                    locale="pl"
                    headerToolbar={{
                        left: '', center: '', right: '',
                    }}
                    firstDay={1}
                    initialDate={new Date()}
                    events={events}
                    allDaySlot={false}
                    slotMinTime="08:00:00"
                    slotMaxTime="20:00:00"
                    expandRows={true}
                    slotLabelFormat={{
                        hour: '2-digit', minute: '2-digit', omitZeroMinute: false,
                    }}
                    slotLabelInterval="01:30:00"
                    dayHeaderFormat={{weekday: 'long'}}
                    eventContent={renderEventContent}
                    eventClick={handleEventClick}
                    eventOverlap={false}
                    slotEventOverlap={false}
                />
            </div>)}
        </>)}

        <Popover
            open={openPopover}
            anchorEl={anchorEl}
            onClose={handlePopoverClose}
            anchorOrigin={{
                vertical: 'top', horizontal: 'left',
            }}
            transformOrigin={{
                vertical: 'top', horizontal: 'right',
            }}
        >
            <Typography sx={{p: 2}}>
                <strong>{selectedEvent?.title}</strong>
                <br/>
                {selectedEvent?.extendedProps.semester}
                <br/>
                <strong>Nauczyciel:</strong> {selectedEvent?.extendedProps?.teacher}
                <br/>
                <strong>Grupa:</strong> {selectedEvent?.extendedProps?.group}
                <br/>
                <strong>Sala:</strong> {selectedEvent?.extendedProps?.classroom}
                <br/>
                <strong>Czas:</strong> {formatTime(selectedEvent?.start)} - {formatTime(selectedEvent?.end)}
                {selectedEvent?.extendedProps?.isEvenWeek !== null && (<>
                    <br/>
                    <strong>Tydzień:</strong>{' '}
                    {selectedEvent?.extendedProps?.isEvenWeek ? 'Parzysty' : 'Nieparzysty'}
                </>)}
            </Typography>
        </Popover>
    </div>);
};

export default Timetable;