import React, {useEffect, useState} from 'react';
import {NavLink} from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import BadgeIcon from '@mui/icons-material/Badge';
import RoomPreferencesIcon from '@mui/icons-material/RoomPreferences';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import BusinessIcon from '@mui/icons-material/Business';
import AddToPhotosIcon from '@mui/icons-material/AddToPhotos';
import WindowIcon from '@mui/icons-material/Window';
import AppsOutageIcon from '@mui/icons-material/AppsOutage';
import {CalendarMonth, DesignServices} from "@mui/icons-material";
import CalendarTodayIcon from '@mui/icons-material/CalendarToday';
import {useAppSelector} from '../hooks/hooks';

interface RouteItem {
    name: string;
    link: string;
    icon: JSX.Element;
    roles?: ('ROLE_ADMIN' | 'ROLE_TEACHER')[];
}

const routes: RouteItem[] = [{
    name: "Strona główna", link: "/", icon: <HomeIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Pracownicy", link: "/employees", icon: <BadgeIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Import danych", link: "/import", icon: <AddToPhotosIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Sale", link: "/classrooms", icon: <RoomPreferencesIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Kalendarz", link: "/calendar", icon: <CalendarMonth/>, roles: ['ROLE_ADMIN'],
},{
    name: "Plany", link: "/plans", icon: <CalendarMonth/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Budynki", link: "/buildings", icon: <BusinessIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Kierunki studiów", link: "/fieldofstudies", icon: <WindowIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Specjalności", link: "/specialisations", icon: <AppsOutageIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Sloty", link: "/slots", icon: <AddToPhotosIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Sloty dnia", link: "/SlotsDays", icon: <AddToPhotosIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Semestry", link: "/semesters", icon: <AddToPhotosIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Przedmioty", link: "/subjects", icon: <AddToPhotosIcon/>, roles: ['ROLE_ADMIN'],
}, {
    name: "Strona główna", link: "/user", icon: <BadgeIcon/>, roles: ['ROLE_TEACHER'],
}, {
    name: "Mój plan", link: "/user-calendar", icon: <CalendarTodayIcon/>, roles: ['ROLE_TEACHER'],
}, {
    name: "Dezyderaty", link: "/user-desiderata", icon: <DesignServices/>, roles: ['ROLE_TEACHER'],
},];

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

        handleResize();

        return () => {
            window.removeEventListener('resize', handleResize);
        };
    }, []);

    const handleMenuToggle = () => {
        setShowMenu(!showMenu);
    };

    return (<nav className="bg-gray-100 px-5 pt-8 z-50">
        <h2 className={`font-semibold pb-6 flex items-center gap-4 ${showMenu ? 'justify-start' : 'justify-center'}`}>
            <div
                className="block text-gray-800 focus:outline-none cursor-pointer px-1"
                onClick={handleMenuToggle}
            >
                {showMenu ? <ArrowBackIosNewIcon/> : <ArrowForwardIosIcon/>}
            </div>
            <div className={!showMenu ? "hidden" : "text-put-light text-nowrap"}>
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
                .map((route) => (<li
                    className="border border-transparent hover:border-gray-400 hover:bg-gray-50 rounded-sm"
                    key={route.name}
                >
                    <NavLink
                        to={route.link}
                        className={({isActive}) => "text-gray-800 gap-2 flex flex-row py-3" + (!showMenu ? " px-2" : " pl-2 pr-10") + " transition-all duration-200 " + (isActive ? " border border-gray-400 bg-gray-50 text-put-light" : "")}
                    >
                        {route.icon}
                        <div className={!showMenu ? "hidden" : "text-put-light text-nowrap"}>
                            {route.name}
                        </div>
                    </NavLink>
                </li>))}
        </ul>
    </nav>);
};

export default Navbar;
