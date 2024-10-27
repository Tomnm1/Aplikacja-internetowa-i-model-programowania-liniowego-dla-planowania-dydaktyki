import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import HomeIcon from '@mui/icons-material/Home';
import BadgeIcon from '@mui/icons-material/Badge';
import RoomPreferencesIcon from '@mui/icons-material/RoomPreferences';
import ArrowBackIosNewIcon from '@mui/icons-material/ArrowBackIosNew';
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';
import { CalendarMonth, DesignServices } from '@mui/icons-material';
import Popper from '@mui/material/Popper';
import Typography from '@mui/material/Typography';
import Paper from '@mui/material/Paper';

const routes = [
    {
        name: "Menu główne",
        link: "/",
        icon: <HomeIcon />,
    },
    {
        name: "Pracownicy",
        link: "/crew",
        icon: <BadgeIcon />
    },
    {
        name: "Sale",
        link: "/classrooms",
        icon: <RoomPreferencesIcon />,
    },
    {
        name: "Kalendarz",
        link: "/calendar",
        icon: <CalendarMonth />,
    },
    {
        name: "Dezyderaty",
        link: "/desiderata",
        icon: <DesignServices />,
    },
];

const Navbar: React.FC = () => {
    const [showMenu, setShowMenu] = useState<boolean>(true);
    const [anchorEl, setAnchorEl] = useState<HTMLElement | null>(null);
    const [hoveredItem, setHoveredItem] = useState<string | null>(null);
    window.addEventListener('resize', () => {
        if (window.innerWidth <= 600) {
            setShowMenu(false);
        } else {
            setShowMenu(true);
        }
    });

    const handleMenuToggle = () => {
        setShowMenu(!showMenu);
    };

    const handlePopoverOpen = (event: React.MouseEvent<HTMLElement>, name: string) => {
        setAnchorEl(event.currentTarget);
        setHoveredItem(name);
    };

    const handlePopoverClose = () => {
        setAnchorEl(null);
        setHoveredItem(null);
    };

    return (
        <nav className="bg-gray-100 px-5 pt-8 z-50">
            <h2 className={"font-semibold pb-6 flex flex-row items-center gap-4"}>
                <div
                    className="block text-gray-800 focus:outline-none cursor-pointer px-1"
                    onClick={handleMenuToggle}>
                    {showMenu ? <ArrowBackIosNewIcon /> : <ArrowForwardIosIcon />}
                </div>
                <div className={!showMenu ? "hidden" : "block text-put-light text-nowrap"}>
                    Menu główne
                </div>
            </h2>
            <ul className="flex flex-col h-screen">
                {routes.map((route) => (
                    <li
                        className="border-gray-100 border hover:border-gray-400 hover:bg-gray-50 rounded-sm"
                        key={route.name}
                        onMouseEnter={(e) => handlePopoverOpen(e, route.name)}
                        onMouseLeave={handlePopoverClose}
                    >
                        <Link
                            to={route.link}
                            className={"text-gray-800 gap-2 flex flex-row py-3" + (!showMenu ? " px-1" : " pr-20 ml-2")}
                        >
                            {route.icon}
                            <div className={!showMenu ? "hidden" : "block text-put-light text-nowrap"}>
                                {route.name}
                            </div>
                        </Link>
                        {!showMenu && (
                        <Popper open={hoveredItem === route.name} anchorEl={anchorEl} placement="right">
                            <Paper elevation={3}>
                                <Typography sx={{ p: 1 }}>{route.name}</Typography>
                            </Paper>
                        </Popper>)}
                    </li>
                ))}
            </ul>
        </nav>
    );
};

export default Navbar;