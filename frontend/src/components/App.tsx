import React from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import Home from './Home';
import Employees from './Teachers.tsx';
import Layout from "./Layout.tsx";
import Classrooms from "./Classrooms.tsx";
import Desiderata from "./Desiderata.tsx";
import Calendar from "./Calendar.tsx";
import Buildings from "./Buildings.tsx";
import FieldOfStudies from "./FieldOfStudies.tsx";
import Specialisations from "./Specialisations.tsx";
import Slots from "./Slots.tsx";
import UserTest from "./UserTest.tsx";
import ProtectedRoute from "./ProtectedRoute.tsx";
import Login from "./Login.tsx";

//https://blog.logrocket.com/authentication-react-router-v6/

const App: React.FC = () => {

    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/login" element={<Login />} />
                    <Route
                        path="/"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Home />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/employees"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Employees />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/classrooms"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Classrooms />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/calendar"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Calendar />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/desiderata"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Desiderata />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/buildings"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Buildings />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/fieldofstudies"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <FieldOfStudies />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/specialisations"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Specialisations />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/slots"
                        element={
                            <ProtectedRoute allowedRoles={['admin']}>
                                <Slots />
                            </ProtectedRoute>
                        }
                    />
                    <Route
                        path="/usertest"
                        element={
                            <ProtectedRoute allowedRoles={['user']}>
                                <UserTest />
                            </ProtectedRoute>
                        }
                    />
                    <Route path="*" element={<Navigate to="/" />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
