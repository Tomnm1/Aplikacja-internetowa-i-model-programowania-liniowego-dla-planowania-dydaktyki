import React from 'react';
import { Navigate } from 'react-router-dom';
import { useAppSelector } from '../hooks/hooks';

interface ProtectedRouteProps {
    children: JSX.Element;
    allowedRoles?: ('admin' | 'user')[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ children, allowedRoles }) => {
    const isAuthenticated = useAppSelector((state) => state.auth.isAuthenticated);
    const role = useAppSelector((state) => state.auth.role);

    if (!isAuthenticated) {
        return <Navigate to="/login" />;
    }

    if (allowedRoles && !allowedRoles.includes(role!)) {
        if (role === 'admin') {
            return <Navigate to="/" />;
        } else {
            return <Navigate to="/usertest" />;
        }
    }

    return children;
};

export default ProtectedRoute;
