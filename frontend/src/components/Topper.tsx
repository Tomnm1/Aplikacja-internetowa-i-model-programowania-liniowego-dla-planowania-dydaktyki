import PersonIcon from '@mui/icons-material/Person';
import put_logo_text from "../assets/put_logo_text.png"
import put_logo from "../assets/put_logo.png"

const Topper = () => {
    return (
        <div className={"sticky top-0 w-full flex flex-nowrap flex-row justify-between bg-put-dark items-center p-4"}>
            <div className={"flex flex-row gap-5 items-center"}>
                <picture>
                    <source media="(min-width: 768px)" srcSet={put_logo_text}/>
                    <img src={put_logo} alt="PUT"/>
                </picture>
                <div className={"flex flex-col "}>
                    <h2 className={"text-white font-semibold"}>
                        ePlanner
                    </h2>
                    <h3 className={"text-xs text-white font-semibold"}>
                        Planner zajęć 4.0
                    </h3>
                </div>
            </div>
            <div className={"hidden flex-row items-center gap-5 sm:flex"}>
                <div className={"flex flex-col"}>
                    <h2 className={"text-xl text-white"}>
                        imie.nazwisko
                    </h2>
                    <h3 className={"text-md text-gray-400"}>
                        @put.poznan.pl
                    </h3>

                </div>
                <div className={"bg-white p-3.5"}>
                    <div className={"text-gray-500"}>
                        <PersonIcon/>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Topper;