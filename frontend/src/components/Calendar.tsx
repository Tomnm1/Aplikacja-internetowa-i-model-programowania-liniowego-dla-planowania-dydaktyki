import {useEffect, useMemo, useState} from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import {
    Autocomplete, CircularProgress, FormControl, InputLabel, MenuItem, Popover, Select, TextField, Typography
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

type ContextType = 'teacher' | 'semester' | 'classroom';

const contextOptions = [
    {value: 'teacher', label: 'Nauczyciel'}, {
    value: 'semester', label: 'Semestr'
}, {value: 'classroom', label: 'Sala'},];

const dayToIndex: { [key in Day]: number } = {
    [Day.SUNDAY]: 0,
    [Day.MONDAY]: 1,
    [Day.TUESDAY]: 2,
    [Day.WEDNESDAY]: 3,
    [Day.THURSDAY]: 4,
    [Day.FRIDAY]: 5,
    [Day.SATURDAY]: 6,
};

const Timetable = () => {
    const [context, setContext] = useState<ContextType>('teacher');
    const {userId, role} = useAppSelector((state: RootState) => state.auth);
    const [selectedItem, setSelectedItem] = useState<{ id: number | undefined; label: string } | null>(null);
    const [selectedEvent, setSelectedEvent] = useState<EventApi | null>(null);
    const [selectedPlan, setSelectedPlan] = useState<number>(0);
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
    const {enqueueSnackbar} = useSnackbar();

    const [classrooms, setClassrooms] = useState<BackendClassroom[]>([]);
    const [semesters, setSemester] = useState<BackendSemester[]>([]);
    const [teachers, setTeachers] = useState<BackendTeacher[]>([]);
    const [plans, setPlans] = useState<Plan[]>([]);
    const [events, setEvents] = useState<EventInput[]>([]);

    const [loading, setLoading] = useState<boolean>(true);
    const [eventsLoading, setEventsLoading] = useState<boolean>(false);

    useEffect(() => {
        setLoading(true);
        Promise.all([fetch(API_ENDPOINTS.TEACHERS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania nauczycieli: ${error.message}`, {variant: 'error'});
                return [];
            }), fetch(API_ENDPOINTS.SEMESTERS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania semestrow: ${error.message}`, {variant: 'error'});
                return [];
            }), fetch(API_ENDPOINTS.CLASSROOMS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania sal: ${error.message}`, {variant: 'error'});
                return [];
            }), fetch(API_ENDPOINTS.PLANS)
            .then((res) => res.json())
            .catch((error) => {
                enqueueSnackbar(`Błąd podczas pobierania planów: ${error.message}`, {variant: 'error'});
                return [];
            }),])
            .then(([teachersData, semestersData, classroomsData, plansData]:[any,any,any,Plan[]]) => {
                setTeachers(teachersData);
                setSemester(semestersData);
                setClassrooms(classroomsData);
                setPlans(plansData);
                setSelectedPlan(():number => {
                    return plansData.find((plan: Plan) => plan.published)?.planId || 0;}  );
                setLoading(false);
            })
            .catch(() => {
                setLoading(false);
            });
    }, [enqueueSnackbar]);

    const contextItems = useMemo(() => ({
        teacher: teachers.map((teacher) => ({id: teacher.id, label: `${teacher.firstName} ${teacher.lastName}`})),
        semester: semesters.map((semester) => ({
            id: semester.semesterId,
            label: `${semester.number} - (${semester.specialisation.name} - ${semester.specialisation.fieldOfStudy?.name} - ${cycleMapping[semester.specialisation.cycle]})`
        })),
        classroom: classrooms.map((classroom) => ({id: classroom.classroomID, label: classroom.code})),
    }), [teachers, semesters, classrooms]);

    useEffect(() => {
        console.log(role)
        if (role === 'user') {
            setContext('teacher');
            const foundTeacher = contextItems.teacher.find((teacher) => teacher.id!.toString() === userId);
            if (foundTeacher) {
                setSelectedItem(foundTeacher);
            }
        }
    }, []);

    useEffect(() => {
        if (role === 'admin') {
            if (contextItems[context] && contextItems[context].length > 0) {
                setSelectedItem(contextItems[context][0]);
            }
            if (plans.length > 0 && selectedPlan === 0) {
                setSelectedPlan(plans[0].planId);
            }

            setEvents([]);
        }
    }, [context, contextItems, role, userId])

    useEffect(() => {
        if (!selectedItem || selectedPlan === 0) return;

        setEventsLoading(true);

        let endpoint = '';

        switch (context) {
            case 'teacher':
                endpoint = API_ENDPOINTS.GENERATED_PLAN_TEACHERS(selectedItem.id!, selectedPlan);
                break;
            case 'semester':
                endpoint = API_ENDPOINTS.GENERATED_PLAN_SEMESTER(selectedItem.id!, selectedPlan);
                break;
            case 'classroom':
                endpoint = API_ENDPOINTS.GENERATED_PLAN_CLASSROOM(selectedItem.id!, selectedPlan);
                break;
            default:
                break;
        }

        if (endpoint) {
            fetch(endpoint)
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

                        const event: EventInput = {
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
                        };
                        return event;
                    });

                    setEvents(eventsData);
                    setEventsLoading(false);
                })
                .catch((error) => {
                    enqueueSnackbar(`Błąd podczas pobierania planu: ${error.message}`, {variant: 'error'});
                    setEvents([]);
                    setEventsLoading(false);
                });
        } else {
            setEvents([]);
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
        return (<div className="flex flex-col items-center justify-center h-full min-w-8">
            <b className={"break-keep"}>{eventInfo.timeText}</b>
            <div className={"line-clamp-1 min-h-5"}>{eventInfo.event.title}</div>
            <div>{eventInfo.event.extendedProps.subjectType}</div>
            <div>{eventInfo.event.extendedProps.teacher}</div>
            <div>{eventInfo.event.extendedProps.classroom}</div>
            <div>{eventInfo.event.extendedProps.isEvenWeek !== null && eventInfo.event.extendedProps.isEvenWeek}</div>
        </div>);
    };

    if (loading) {
        return (<div className="flex items-center justify-center p-5">
            <CircularProgress/>
        </div>);
    }

    return (<div className={'py-8 px-2 w-full overflow-auto'}>
        {role === 'admin' && (<div className={'flex gap-2 w-full'}>
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
                renderInput={(params) => <TextField {...params} label="Kontekst" variant="outlined"
                                                    fullWidth/>}
            />
            <Autocomplete
                key={selectedItem ? selectedItem.id : 'select'}
                sx={{width: 300}}
                options={contextItems[context]}
                getOptionLabel={(option) => option.label}
                value={selectedItem}
                onChange={(_event, newValue) => {
                    setSelectedItem(newValue);
                }}
                renderOption={(props, option) => (<li {...props} key={option.id}>
                    {option.label}
                </li>)}
                renderInput={(params) => <TextField {...params}
                                                    label={contextOptions.find((option) => option.value === context)?.label || ''}
                                                    variant="outlined"
                                                    fullWidth/>}
            />
        </div>)}

        {eventsLoading ? (<div className="flex items-center justify-center p-5 ">
            <CircularProgress/>
        </div>) : <div className={role === 'admin' ? 'w-[220vw]' : "w-full"}>
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
        </div>

        }

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
