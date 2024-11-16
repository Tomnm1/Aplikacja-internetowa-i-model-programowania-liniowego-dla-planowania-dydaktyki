import Navbar from "./Navbar.tsx";
import {ReactNode} from "react";
import Topper from "./Topper.tsx";

interface Props {
    children: ReactNode;
}
const Layout = ({children } : Props) => {
    return (
        <div className="h-screen flex flex-col">
            <Topper/>
            <div className="flex-1 flex overflow-hidden">
                <div className="overflow-auto">
                    <Navbar/>
                </div>
                <div className="flex-1 overflow-hidden">
                    {children}
                </div>
            </div>
        </div>
    );
};

export default Layout;