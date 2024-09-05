import React, {useState} from 'react';
import {CalendarViewProps, Schedule} from "../utils/Interfaces.ts";
import Draggable, {DraggableData, DraggableEvent} from 'react-draggable';
import Select from 'react-select';

//type AxisType = 'hours' | 'rooms' | 'groups' | 'teachers';

const Calendar: React.FC<CalendarViewProps> = ({teachers, groups, rooms, hours, schedules}) => {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const [activeSchedules, setActiveSchedules] = useState<Schedule[]>(schedules);
    const [selectedTeachers, setSelectedTeachers] = useState<number[]>([]);
    const [selectedRooms, setSelectedRooms] = useState<number[]>([]);
    const [selectedHours, setSelectedHours] = useState<number[]>([]);
    const [selectedGroups, setSelectedGroups] = useState<number[]>([]);
    const [dragging, setDragging] = useState<boolean>(false); // Do obsługi stylizacji podczas dragowania

    // Przetworzenie nauczycieli/grup/sal/godzin do formatów akceptowanych przez react-select
    const teacherOptions = teachers.map(teacher => ({value: teacher.id, label: teacher.name}));
    const roomOptions = rooms.map(room => ({value: room.id, label: room.name}));
    const hourOptions = hours.map(hour => ({value: hour.id, label: hour.timeRange}));
    const groupOptions = groups.map(group => ({value: group.id, label: group.name}));

    const handleStopDrag = (_e: DraggableEvent, data: DraggableData, scheduleId: number) => {
        console.log(`Przeciągnięto plan ${scheduleId} na pozycję X: ${data.x}, Y: ${data.y}`);
        setDragging(false); // Koniec dragowania
    };

    setActiveSchedules(schedules);

    const filteredSchedules = activeSchedules.filter(schedule =>
        (selectedTeachers.length === 0 || schedule.teacherIds.some(id => selectedTeachers.includes(id))) &&
        (selectedRooms.length === 0 || selectedRooms.includes(schedule.roomId)) &&
        (selectedHours.length === 0 || selectedHours.includes(schedule.hourId)) &&
        (selectedGroups.length === 0 || schedule.groupIds.some(id => selectedGroups.includes(id)))
    );

    const findTeacherById = (id: number) => teachers.find(t => t.id === id)?.name || '';
    //const findRoomById = (id: number) => rooms.find(r => r.id === id)?.name || '';
    const findGroupById = (id: number) => groups.find(g => g.id === id)?.name || '';
   // const findHourById = (id: number) => hours.find(h => h.id === id)?.timeRange || '';

    return (
        <div className="flex flex-col h-full w-full p-4">
            {/* Sekcja wyboru filtrów */}
            <div className="flex gap-4 mb-4">
                <div className="flex-1">
                    <h3 className="font-bold mb-2">Nauczyciele</h3>
                    <Select
                        isMulti
                        options={teacherOptions}
                        onChange={selectedOptions => setSelectedTeachers(selectedOptions.map(option => option.value))}
                        className="w-full"
                        placeholder="Wybierz nauczycieli"
                    />
                </div>

                <div className="flex-1">
                    <h3 className="font-bold mb-2">Sale</h3>
                    <Select
                        isMulti
                        options={roomOptions}
                        onChange={selectedOptions => setSelectedRooms(selectedOptions.map(option => option.value))}
                        className="w-full"
                        placeholder="Wybierz sale"
                    />
                </div>

                <div className="flex-1">
                    <h3 className="font-bold mb-2">Godziny</h3>
                    <Select
                        isMulti
                        options={hourOptions}
                        onChange={selectedOptions => setSelectedHours(selectedOptions.map(option => option.value))}
                        className="w-full"
                        placeholder="Wybierz godziny"
                    />
                </div>

                <div className="flex-1">
                    <h3 className="font-bold mb-2">Grupy</h3>
                    <Select
                        isMulti
                        options={groupOptions}
                        onChange={selectedOptions => setSelectedGroups(selectedOptions.map(option => option.value))}
                        className="w-full"
                        placeholder="Wybierz grupy"
                    />
                </div>
            </div>

            {/* Kalendarz zajęć */}
            <div className="grid grid-cols-1 md:grid-cols-5 gap-4 h-full overflow-hidden">
                <div className="grid grid-rows-1 gap-4">
                    <div className="text-center font-bold">Godziny</div>
                    {hours.map(hour => (
                        <div key={hour.id} className="p-2 font-bold text-center bg-gray-200 rounded-md">
                            {hour.timeRange}
                        </div>
                    ))}
                </div>

                {rooms.map(room => (
                    <div key={room.id} className="bg-gray-100 rounded-md h-full overflow-hidden">
                        <div className="p-2 font-bold text-center bg-gray-300 rounded-md">{room.name}</div>
                        <div>
                            {hours.map(hour => (
                                <div
                                    key={hour.id}
                                    className={`border border-gray-300 h-24 relative ${
                                        dragging ? 'bg-gray-100 border-dashed' : ''
                                    }`}
                                >
                                    {/* Element planu */}
                                    {filteredSchedules
                                        .filter(schedule => schedule.roomId === room.id && schedule.hourId === hour.id)
                                        .map(schedule => (
                                            <Draggable
                                                key={schedule.id}
                                                grid={[100, 100]}
                                                onStart={() => setDragging(true)} // Zaczęcie dragowania
                                                onStop={(e, data) => handleStopDrag(e, data, schedule.id)}
                                            >
                                                <div className="absolute top-0 left-0 p-2 bg-blue-400 text-white rounded-md cursor-pointer">
                                                    <div className="font-bold">Nauczyciele:</div>
                                                    {schedule.teacherIds.map(teacherId => (
                                                        <div key={teacherId}>{findTeacherById(teacherId)}</div>
                                                    ))}
                                                    <div className="mt-2">
                                                        <div className="font-bold">Grupy:</div>
                                                        {schedule.groupIds.map(groupId => (
                                                            <div key={groupId}>{findGroupById(groupId)}</div>
                                                        ))}
                                                    </div>
                                                </div>
                                            </Draggable>
                                        ))}
                                </div>
                            ))}
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
};


export default Calendar;

