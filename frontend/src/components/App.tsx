import React, {useEffect} from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes, useLocation} from 'react-router-dom';
import Home from './Home';
import Employees from './Teachers.tsx';
import Layout from "./Layout.tsx";
import Classrooms from "./Classrooms.tsx";
import UserDesiderata from "./UserDesiderata.tsx";
import Calendar from "./Calendar.tsx";
import Buildings from "./Buildings.tsx";
import FieldOfStudies from "./FieldOfStudies.tsx";
import Specialisations from "./Specialisations.tsx";
import Slots from "./Slots.tsx";
import UserHome from "./UserHome.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import SlotsDays from "./SlotsDays.tsx";
import Semesters from "./Semesters.tsx";
import Subjects from "./Subjects.tsx";
import UserPlan from "./UserPlan.tsx";
import {useDispatch} from "react-redux";
import {loginUser} from "../app/slices/authSlice.ts";
import Plans from "./Plans.tsx";


const App: React.FC = () => {
    const dispatch = useDispatch();
    useEffect(() => {
        const hash = location.hash;
        console.log(location)
        console.log('location.hash:', hash);
        if (hash.includes('access_token')) {
            const params = new URLSearchParams(hash.substring(1));
            const accessToken = params.get('access_token');

            console.log('Extracted accessToken:', accessToken);

            if (accessToken) {
                localStorage.setItem('access_token', accessToken);
                dispatch(loginUser(accessToken));
                window.history.replaceState(null, '', window.location.pathname);
                console.log('Access token processed and stored.');
            }
        } else {
            const storedToken = localStorage.getItem('access_token');
            console.log('Stored accessToken:', storedToken);

            if (storedToken) {
                dispatch(loginUser(storedToken));
                console.log('Initialized Redux state from stored token.');
            } else {
                const systemId = 'planner-dev.esys.put.poznan.pl';
                window.location.href = `https://elogin.put.poznan.pl/?do=Authorize&system=${systemId}`;
                console.log('Redirecting to authorization.');
            }
        }
    }, [dispatch]);

    return (
        <Router>
            <Routes>
                <Route element={<ProtectedRoute allowedRoles={['admin', 'user']} />}>
                    <Route element={<Layout />}>
                        <Route element={<ProtectedRoute allowedRoles={['admin']} />}>
                            <Route path="/" element={<Home />} />
                            <Route path="/employees" element={<Employees />} />
                            <Route path="/classrooms" element={<Classrooms />} />
                            <Route path="/calendar" element={<Calendar/>} />
                            <Route path="/plans" element={<Plans/>} />
                            <Route path="/buildings" element={<Buildings />} />
                            <Route path="/fieldofstudies" element={<FieldOfStudies />} />
                            <Route path="/specialisations" element={<Specialisations />} />
                            <Route path="/slots" element={<Slots />} />
                            <Route path="/slotsDays" element={<SlotsDays />} />
                            <Route path="/semesters" element={<Semesters />} />
                            <Route path="/subjects" element={<Subjects />} />
                        </Route>
                        <Route element={<ProtectedRoute allowedRoles={['user']} />}>
                            <Route path="/user" element={<UserHome />} />
                            <Route path="/user-calendar" element={<UserPlan />} />
                            <Route path="/user-desiderata" element={<UserDesiderata />} />
                        </Route>
                    </Route>
                </Route>
                <Route path="*" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
};

export default App;
