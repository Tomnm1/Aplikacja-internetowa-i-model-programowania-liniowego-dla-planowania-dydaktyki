import React from 'react';
import { useAppSelector } from '../hooks/hooks';

const UserHome: React.FC = () => {
    const user = useAppSelector((state) => state.auth.user);

    return (
        <div>
            <h1>Witaj, {user?.firstName} {user?.lastName}!</h1>
        </div>
    );
};

export default UserHome;
