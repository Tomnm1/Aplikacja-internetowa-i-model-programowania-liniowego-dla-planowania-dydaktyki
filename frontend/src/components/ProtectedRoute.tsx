import React from 'react';
import { useAppSelector } from '../hooks/hooks';
import { useNavigate, Outlet } from 'react-router-dom';

interface ProtectedRouteProps {
    allowedRoles: ('ROLE_ADMIN' | 'ROLE_TEACHER')[];
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ allowedRoles }) => {
    const { isAuthenticated, role, user } = useAppSelector((state) => state.auth);

    return <Outlet />;
};

export default ProtectedRoute;
