import React, { useEffect, useMemo, useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import timeGridPlugin from '@fullcalendar/timegrid';
import {
    Typography,
    Popover, Autocomplete, TextField
} from '@mui/material';
import { EventInput } from '@fullcalendar/core';
import { EventApi, EventClickArg } from "fullcalendar";
import { useDispatch, useSelector } from 'react-redux';
import { fetchClassrooms } from '../app/slices/classroomSlice';
import { fetchSlots } from '../app/slices/slotsSlice';
import { fetchTeachers } from '../app/slices/teacherSlice';
import { AppDispatch, RootState } from '../app/store';
import { useSnackbar } from "notistack";


type ContextType = 'teacher' | 'group' | 'classroom';

const contextOptions = [
    { value: 'teacher', label: 'Nauczyciel' },
    { value: 'group', label: 'Grupa' },
    { value: 'classroom', label: 'Sala' },
];

const Timetable: React.FC = () => {
    const [context, setContext] = useState<ContextType>('teacher');
    const [selectedItem, setSelectedItem] = useState<string>('');
    const [selectedEvent, setSelectedEvent] = useState<EventApi | null>(null);
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);

    const { enqueueSnackbar } = useSnackbar();

    const dispatch = useDispatch<AppDispatch>();

    useEffect(() => {
        dispatch(fetchSlots()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania slotów: ${error.message}`, { variant: 'error' });
        });
        dispatch(fetchClassrooms()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania sal: ${error.message}`, { variant: 'error' });
        });
        dispatch(fetchTeachers()).unwrap().catch((error) => {
            enqueueSnackbar(`Błąd podczas pobierania nauczycieli: ${error.message}`, { variant: 'error' });
        });
    }, [dispatch, enqueueSnackbar]);

    const classrooms = useSelector((state: RootState) => state.classrooms.rows);
    const slots = useSelector((state: RootState) => state.slots.rows);
    const teachers = useSelector((state: RootState) => state.teachers.rows);

    const classroomsLoading = useSelector((state: RootState) => state.classrooms.loading);
    const slotsLoading = useSelector((state: RootState) => state.slots.loading);
    const teachersLoading = useSelector((state: RootState) => state.teachers.loading);

    const isLoading = classroomsLoading || slotsLoading || teachersLoading;

    const contextItems = useMemo(() => ({
        teacher: teachers.map((teacher) => `${teacher.firstName} ${teacher.lastName}`),
        group: ['Grupa A', 'Grupa B', 'Grupa C', 'Grupa D', 'Grupa E'],
        classroom: classrooms.map((classroom) => classroom.code),
    }), [teachers, classrooms]);

    useEffect(() => {
        if (contextItems[context].length > 0) {
            setSelectedItem(contextItems[context][0]);
        } else {
            setSelectedItem('');
        }
    }, [context, contextItems]);

    const dummyEvents: EventInput[] = useMemo(() => {
        if (isLoading) return [];

        return [
            {
                id: '1',
                title: 'Algorytmy i struktury danych',
                daysOfWeek: [1],
                startTime: slots.find((s) => s.slot_id === 7)?.start_time || '08:00:00',
                endTime: slots.find((s) => s.slot_id === 7)?.end_time || '09:30:00',
                extendedProps: {
                    teacher: 'Jacek Błażewicz',
                    group: 'Grupa A',
                    classroom: 'Sala 101',
                },
            },
            {
                id: '111',
                title: 'Głoszenie prawdy',
                daysOfWeek: [1],
                startTime: slots.find((s) => s.slot_id === 8)?.start_time || '08:00:00',
                endTime: slots.find((s) => s.slot_id === 8)?.end_time || '09:30:00',
                extendedProps: {
                    teacher: 'Jacek Błażewicz',
                    group: 'Grupa A',
                    classroom: 'Sala 101',
                },
            },
            {
                id: '99',
                title: 'Seminarium instytutowe',
                daysOfWeek: [5],
                startTime: slots.find((s) => s.slot_id === 8)?.start_time || '09:45:00',
                endTime: slots.find((s) => s.slot_id === 10)?.end_time || '16:30:00',
                extendedProps: {
                    teacher: 'Jacek Błażewicz',
                    group: 'Grupa B',
                    classroom: 'Sala 101',
                },
            },
            {
                id: '98',
                title: 'Algorytmy i struktury danych',
                daysOfWeek: [3],
                startTime: slots.find((s) => s.slot_id === 8)?.start_time || '09:45:00',
                endTime: slots.find((s) => s.slot_id === 8)?.end_time || '11:15:00',
                extendedProps: {
                    teacher: 'Jacek Błażewicz',
                    group: 'Grupa B',
                    classroom: 'Sala 101',
                },
            },
            {
                id: '2',
                title: 'Fizyka',
                daysOfWeek: [2],
                startTime: slots.find((s) => s.slot_id === 5)?.start_time || '09:45:00',
                endTime: slots.find((s) => s.slot_id === 5)?.end_time || '11:15:00',
                extendedProps: {
                    teacher: 'Anna Nowak',
                    group: 'Grupa B',
                    classroom: 'Sala 102',
                },
            },
            {
                id: '3',
                title: 'Chemia',
                daysOfWeek: [3],
                startTime: slots.find((s) => s.slot_id === 7)?.start_time || '11:45:00',
                endTime: slots.find((s) => s.slot_id === 7)?.end_time || '13:15:00',
                extendedProps: {
                    teacher: 'Piotr Wiśniewski',
                    group: 'Grupa C',
                    classroom: 'Sala 103',
                },
            },
            {
                id: '4',
                title: 'Biologia',
                daysOfWeek: [4],
                startTime: slots.find((s) => s.slot_id === 8)?.start_time || '13:30:00',
                endTime: slots.find((s) => s.slot_id === 8)?.end_time || '15:00:00',
                extendedProps: {
                    teacher: 'Maria Nowicka',
                    group: 'Grupa D',
                    classroom: 'Sala 104',
                },
            },
            {
                id: '5',
                title: 'Historia',
                daysOfWeek: [5],
                startTime: slots.find((s) => s.slot_id === 9)?.start_time || '15:10:00',
                endTime: slots.find((s) => s.slot_id === 9)?.end_time || '16:40:00',
                extendedProps: {
                    teacher: 'Andrzej Kwiatkowski',
                    group: 'Grupa E',
                    classroom: 'Sala 105',
                },
            },
        ];
    }, [isLoading, slots, teachers]);

    const filteredEvents = useMemo(() => {
        return dummyEvents.filter((event: EventInput) => {
            return event.extendedProps?.[context] === selectedItem;
        });
    }, [dummyEvents, context, selectedItem]);

    if (isLoading) {
        return <div>Loading...</div>;
    }

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

    const formatTime = (date: any) => {
        return date ? date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' }) : '';
    };

    return (
        <div className={"p-8"}>
            <div className={"flex gap-2 w-full"}>
                <Autocomplete
                    sx={{ width: 300 }}
                    options={contextOptions}
                    getOptionLabel={(option) => option.label}
                    value={contextOptions.find((option) => option.value === context) || null}
                    onChange={(_event, newValue) => {
                        if (newValue) {
                            const newContext = newValue.value as ContextType;
                            setContext(newContext);
                        }
                    }}
                    renderInput={(params) => (
                        <TextField {...params} label="Kontekst" variant="outlined" fullWidth />
                    )}
                />
                <Autocomplete
                    sx={{ width: 300 }}
                    options={contextItems[context]}
                    value={selectedItem || null}
                    onChange={(_event, newValue) => {
                        setSelectedItem(newValue || '');
                    }}
                    renderInput={(params) => (
                        <TextField {...params} label="Wybierz" variant="outlined" fullWidth />
                    )}
                />

            </div>

            <FullCalendar
                plugins={[timeGridPlugin]}
                initialView="timeGridWeek"
                locale="pl"
                headerToolbar={{
                    left: '',
                    center: '',
                    right: '',
                }}
                firstDay={1}
                initialDate={new Date()}
                events={filteredEvents}
                allDaySlot={false}
                slotMinTime="08:00:00"
                slotMaxTime="20:00:00"
                height="auto"
                expandRows={true}
                slotLabelFormat={{
                    hour: '2-digit',
                    minute: '2-digit',
                    omitZeroMinute: false,
                }}
                slotLabelInterval="01:30:00"
                dayHeaderFormat={{ weekday: 'long' }}
                eventClick={handleEventClick}
            />

            <Popover
                open={openPopover}
                anchorEl={anchorEl}
                onClose={handlePopoverClose}
                anchorOrigin={{
                    vertical: 'top',
                    horizontal: 'left',
                }}
                transformOrigin={{
                    vertical: 'top',
                    horizontal: 'right',
                }}
            >
                <Typography sx={{ p: 2 }}>
                    <strong>{selectedEvent?.title}</strong>
                    <br />
                    <strong>Nauczyciel:</strong> {selectedEvent?.extendedProps?.teacher}
                    <br />
                    <strong>Grupa:</strong> {selectedEvent?.extendedProps?.group}
                    <br />
                    <strong>Sala:</strong> {selectedEvent?.extendedProps?.classroom}
                    <br />
                    <strong>Czas:</strong> {formatTime(selectedEvent?.start)} - {formatTime(selectedEvent?.end)}
                </Typography>
            </Popover>
        </div>
    );
};

export default Timetable;
