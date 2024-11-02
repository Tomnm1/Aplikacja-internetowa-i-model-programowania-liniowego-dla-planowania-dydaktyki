// App.tsx
import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import Employees from './Teachers.tsx';
import Layout from "./Layout.tsx";
import Classrooms from "./Classrooms.tsx";
import Desiderata from "./Desiderata.tsx";
import Calendar from "./Calendar.tsx";
import Buildings from "./Buildings.tsx";
import FieldOfStudies from "./FieldOfStudies.tsx";

const App: React.FC = () => {

    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/employees" element={<Employees />} />
                    <Route path="/classrooms" element={<Classrooms />} />
                    <Route path="/calendar" element={<Calendar /> } />
                    <Route path="/desiderata" element={<Desiderata />} />
                    <Route path="/buildings" element={<Buildings />} />
                    <Route path="/fieldofstudies" element={<FieldOfStudies />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
