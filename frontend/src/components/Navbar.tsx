// src/Navbar.tsx
import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import BadgeIcon from '@mui/icons-material/Badge';
import RoomPreferencesIcon from '@mui/icons-material/RoomPreferences';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import BusinessIcon from '@mui/icons-material/Business';
import AddToPhotosIcon from '@mui/icons-material/AddToPhotos';
import WindowIcon from '@mui/icons-material/Window';
import AppsOutageIcon from '@mui/icons-material/AppsOutage';
import { CalendarMonth, DesignServices } from "@mui/icons-material";
import { useAppSelector } from '../hooks/hooks';

interface RouteItem {
    name: string;
    link: string;
    icon: JSX.Element;
    roles?: ('admin' | 'user')[];
}

const routes: RouteItem[] = [
    {
        name: "Menu główne",
        link: "/",
        icon: <HomeIcon />,
        roles: ['admin'],
    },
    {
        name: "Pracownicy",
        link: "/employees",
        icon: <BadgeIcon />,
        roles: ['admin'],
    },
    {
        name: "Sale",
        link: "/classrooms",
        icon: <RoomPreferencesIcon />,
        roles: ['admin'],
    },
    {
        name: "Kalendarz",
        link: "/calendar",
        icon: <CalendarMonth />,
        roles: ['admin'],
    },
    {
        name: "Dezyderaty",
        link: "/desiderata",
        icon: <DesignServices />,
        roles: ['admin'],
    },
    {
        name: "Budynki",
        link: "/buildings",
        icon: <BusinessIcon />,
        roles: ['admin'],
    },
    {
        name: "Kierunki studiów",
        link: "/fieldofstudies",
        icon: <WindowIcon />,
        roles: ['admin'],
    },
    {
        name: "Specjalności",
        link: "/specialisations",
        icon: <AppsOutageIcon />,
        roles: ['admin'],
    },
    {
        name: "Sloty",
        link: "/slots",
        icon: <AddToPhotosIcon />,
        roles: ['admin'],
    },
    {
        name: "Sloty dnia",
        link: "/SlotsDays",
        icon: <AddToPhotosIcon />,
        roles: ['admin'],
    },
    {
        name: "User Test",
        link: "/usertest",
        icon: <BadgeIcon />,
        roles: ['user'],
    },
];

const Navbar: React.FC = () => {
    const [showMenu, setShowMenu] = useState<boolean>(true);
    const role = useAppSelector((state) => state.auth.role);

    useEffect(() => {
        const handleResize = () => {
            if (window.innerWidth <= 600) {
                setShowMenu(false);
            } else {
                setShowMenu(true);
            }
        };

        window.addEventListener('resize', handleResize);

        // Initial check
        handleResize();

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    const handleMenuToggle = () => {
        setShowMenu(!showMenu);
    };

    return (
        <nav className="bg-gray-100 px-5 pt-8 z-50">
            <h2 className="font-semibold pb-6 flex flex-row items-center gap-4">
                <div
                    className="block md:hidden text-gray-800 focus:outline-none cursor-pointer px-1"
                    onClick={handleMenuToggle}
                >
                    {showMenu ? <ArrowBackIosNewIcon /> : <ArrowForwardIosIcon />}
                </div>
                <div className={!showMenu ? "hidden md:block" : "text-put-light text-nowrap"}>
                    Menu
                </div>
            </h2>
            <ul className="flex flex-col h-screen">
                {routes
                    .filter(route => {
                        if (!route.roles) return false;
                        if (!role) return false;
                        return route.roles.includes(role);
                    })
                    .map((route) => (
                        <li
                            className="border-gray-100 border hover:border-gray-400 hover:bg-gray-50 rounded-sm"
                            key={route.name}
                        >
                            <Link
                                to={route.link}
                                className={
                                    "text-gray-800 gap-2 flex flex-row py-3" +
                                    (!showMenu ? " px-1" : " pr-20 ml-2")
                                }
                            >
                                {route.icon}
                                <div className={!showMenu ? "hidden md:block" : "text-put-light text-nowrap"}>
                                    {route.name}
                                </div>
                            </Link>
                        </li>
                    ))}
            </ul>
        </nav>
    );
};

export default Navbar;
