// Navbar.tsx
import React, {useState} from 'react';
import { Link } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import BadgeIcon from '@mui/icons-material/Badge';
import RoomPreferencesIcon from '@mui/icons-material/RoomPreferences';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import BusinessIcon from '@mui/icons-material/Business';
import {CalendarMonth, DesignServices} from "@mui/icons-material";

const routes = [
    {
        name: "Menu główne",
        link: "/",
        icon: <HomeIcon />,
    },
    {
        name: "Pracownicy",
        link: "/employees",
        icon: <BadgeIcon />
    },
    {
        name: "Sale",
        link: "/classrooms",
        icon: <RoomPreferencesIcon/>,
    },
    {
        name: "Kalendarz",
        link: "/calendar",
        icon: <CalendarMonth/>,
    },    {
        name: "Dezyderaty",
        link: "/desiderata",
        icon: <DesignServices/>,
    },
    {
        name: "Budynki",
        link: "/buildings",
        icon: <BusinessIcon/>,
    },
]
const Navbar: React.FC = () => {
    const [showMenu, setShowMenu] = useState<boolean>(true);

    window.addEventListener('resize', () => {
        if(window.innerWidth <= 600){
            setShowMenu(false);
        }
        else{
            setShowMenu(true);
        }
    })

    const handleMenuToggle = () => {
        setShowMenu(!showMenu);
    };

    return (
        <nav className="bg-gray-100 px-5 pt-8 z-50">
            <h2 className={"font-semibold  pb-6 flex flex-row items-center gap-4 "}>
                <div
                    className="block md:hidden text-gray-800 focus:outline-none cursor-pointer px-1"
                    onClick={handleMenuToggle}>
                    {showMenu ? <ArrowBackIosNewIcon/> : <ArrowForwardIosIcon/>}
                </div>
                <div className={!showMenu ? "hidden md:block" : "text-put-light text-nowrap"}>
                    Menu główne
                </div>
            </h2>
            <ul className="flex flex-col h-screen">
                {routes.map((route) => (
                    <li className="border-gray-100 border hover:border-gray-400 hover:bg-gray-50 rounded-sm" key={route.name}>
                        <Link to={route.link} className={"text-gray-800 gap-2 flex flex-row py-3" + (!showMenu ? " px-1" : " pr-20 ml-2")}>
                            {route.icon}
                            <div className={!showMenu ? "hidden md:block" : "text-put-light text-nowrap"}>{route.name}</div>
                        </Link>
                    </li>
                ))}

            </ul>
        </nav>
    );
};

export default Navbar;
