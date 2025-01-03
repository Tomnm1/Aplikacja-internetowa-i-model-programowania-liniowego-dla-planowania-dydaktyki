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
import Login from "./Login.tsx";


const App: React.FC = () => {


    return (
        <Router>
            <Routes>
                <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN', 'ROLE_TEACHER']} />}>
                    <Route element={<Layout />}>
                        <Route element={<ProtectedRoute allowedRoles={['ROLE_ADMIN']} />}>
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
                        <Route element={<ProtectedRoute allowedRoles={['ROLE_TEACHER']} />}>
                            <Route path="/user" element={<UserHome />} />
                            <Route path="/user-calendar" element={<UserPlan />} />
                            <Route path="/user-desiderata" element={<UserDesiderata />} />
                        </Route>
                    </Route>
                </Route>
                <Route path="/login" element={<Login />} />
                <Route path="/" element={<Navigate to="/" />} />
            </Routes>
        </Router>
    );
};

export default App;
