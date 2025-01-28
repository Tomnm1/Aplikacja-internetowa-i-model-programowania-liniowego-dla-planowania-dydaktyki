import React from 'react';
import { useAppSelector } from '../hooks/hooks';
import {Outlet, Navigate} from 'react-router-dom';

interface ProtectedRouteProps {
    allowedRoles: ('ROLE_ADMIN' | 'ROLE_TEACHER')[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
    const { isAuthenticated, role, user } = useAppSelector((state) => state.auth);

    if (!isAuthenticated || !role || !user) {
        return <Navigate to={"/login"} replace />
    }

    if(!allowedRoles.includes(role)) {
        return <Navigate to={"/login"} replace />
    }

    return <Outlet />;
};

export default ProtectedRoute;
