// App.tsx
import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import Crew from './Crew.tsx';
import Layout from "./Layout.tsx";
import Classrooms from "./Classrooms.tsx";
import Desiderata from "./Desiderata.tsx";
import Calendar from "./Calendar.tsx";
const teachers = [
    { id: 1, name: 'Jan Kowalski' },
    { id: 2, name: 'Anna Nowak' },
    { id: 3, name: 'Krzysztof Wiśniewski' },
    { id: 4, name: 'Ewa Zielińska' },
    { id: 5, name: 'Piotr Malinowski' },
];

const groups = [
    { id: 1, name: 'Grupa A' },
    { id: 2, name: 'Grupa B' },
    { id: 3, name: 'Grupa C' },
    { id: 4, name: 'Grupa D' },
    { id: 5, name: 'Grupa E' },
];

const rooms = [
    { id: 1, name: 'Sala 101' },
    { id: 2, name: 'Sala 102' },
    { id: 3, name: 'Sala 103' },
    { id: 4, name: 'Sala 104' },
    { id: 5, name: 'Sala 105' },
];

const hours = [
    { id: 1, timeRange: '08:00 - 09:00' },
    { id: 2, timeRange: '09:00 - 10:00' },
    { id: 3, timeRange: '10:00 - 11:00' },
    { id: 4, timeRange: '11:00 - 12:00' },
    { id: 5, timeRange: '12:00 - 13:00' },
    { id: 6, timeRange: '13:00 - 14:00' },
    { id: 7, timeRange: '14:00 - 15:00' },
];

const schedules = [
    {
        id: 1,
        teacherIds: [1, 2],
        groupIds: [1],
        roomId: 1,
        hourId: 1,
    },
    {
        id: 2,
        teacherIds: [3],
        groupIds: [2, 3],
        roomId: 2,
        hourId: 1,
    },
    {
        id: 3,
        teacherIds: [4],
        groupIds: [4],
        roomId: 3,
        hourId: 2,
    },
    {
        id: 4,
        teacherIds: [2, 5],
        groupIds: [5],
        roomId: 4,
        hourId: 3,
    },
    {
        id: 5,
        teacherIds: [1],
        groupIds: [1, 2],
        roomId: 5,
        hourId: 4,
    },
    {
        id: 6,
        teacherIds: [3],
        groupIds: [3],
        roomId: 2,
        hourId: 5,
    },
    {
        id: 7,
        teacherIds: [5],
        groupIds: [4, 5],
        roomId: 1,
        hourId: 6,
    },
    {
        id: 8,
        teacherIds: [2, 4],
        groupIds: [1, 3, 5],
        roomId: 3,
        hourId: 7,
    },
];


const App: React.FC = () => {

    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/crew" element={<Crew />} />
                    <Route path="/classrooms" element={<Classrooms />} />
                    <Route path="/calendar" element={<Calendar teachers={teachers} rooms={rooms} hours={hours} schedules={schedules} groups={groups} />} />
                    <Route path="/desiderata" element={<Desiderata />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
