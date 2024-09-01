// App.tsx
import React from 'react';
import {BrowserRouter as Router, Route, Routes} from 'react-router-dom';
import Home from './Home';
import Pracownicy from './Pracownicy';
import Layout from "./Layout.tsx";
import Sale from "./Sale.tsx";

const App: React.FC = () => {
    return (
        <Router>
            <Layout>
                <Routes>
                    <Route path="/" element={<Home />} />
                    <Route path="/pracownicy" element={<Pracownicy />} />
                    <Route path="/sale" element={<Sale />} />
                </Routes>
            </Layout>
        </Router>
    );
};

export default App;
