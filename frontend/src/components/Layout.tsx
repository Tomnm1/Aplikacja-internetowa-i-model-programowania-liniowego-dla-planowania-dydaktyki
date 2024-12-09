import Navbar from "./Navbar.tsx";
import Topper from "./Topper.tsx";
import {Outlet} from "react-router-dom";
const Layout = () => {
    return (
        <div className="h-screen flex flex-col">
            <div className="print:hidden">
                <Topper/>
            </div>
            <div className="flex-1 flex overflow-hidden">
                <div className="overflow-auto print:hidden">
                    <Navbar/>
                </div>
                <div className="flex-1 overflow-auto">
                    <Outlet />
                </div>
            </div>
        </div>
    );
};

export default Layout;
