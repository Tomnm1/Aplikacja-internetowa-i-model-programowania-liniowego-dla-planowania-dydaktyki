import React, {useEffect} from 'react';
import {useNavigate, useSearchParams} from 'react-router-dom';
import {useDispatch} from "react-redux";
import {loginUser} from "../app/slices/authSlice.ts";
import {useAppSelector} from "../hooks/hooks.ts";
import {CircularProgress} from "@mui/material";

const Login: React.FC = () => {
    const { isAuthenticated, role, user } = useAppSelector((state) => state.auth);
    const dispatch = useDispatch();
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();
    useEffect(() => {
        const token = searchParams.get("access_token");
        console.log('new token:', token);
        if (token) {
            console.log('Extracted accessToken:', token);
            localStorage.setItem('access_token', token);
            if(!isAuthenticated){
                console.log("not authed, dispatching")
                dispatch(loginUser(token));
                r
            }
            else {
                console.log("user", user);
                if (!user) {
                    console.log("no user")
                    return <div>Ładowanie danych użytkownika...</div>;
                }
                console.log('Access token processed and stored.');
                if (role === 'ROLE_ADMIN') {
                    navigate("/")
                    return;
                }
                if (role === 'ROLE_TEACHER') {
                    navigate("/user")
                    return;
                }
            }

        } else {
            const storedToken = localStorage.getItem('access_token');
            console.log('Stored accessToken:', storedToken);

            if (storedToken) {
                if(!isAuthenticated){
                    console.log("not authed, dispatching")
                    dispatch(loginUser(storedToken));
                }
                else {
                    console.log("user", user);
                    if (!user) {
                        console.log("no user")
                        return <div>Ładowanie danych użytkownika...</div>;
                    }
                    console.log('Access token processed and stored.');
                    console.log('role: ', role)
                    if (role === "ROLE_ADMIN") {
                        console.log("ROLE_ADMIN")
                        navigate("/")
                        return;
                    }
                    if (role === "ROLE_TEACHER") {
                        console.log("ROLE_TEACHER")
                        navigate("/user")
                        return;
                    }
                }
            } else {
                const systemId = 'planner-dev.esys.put.poznan.pl';
                window.location.href = `https://elogin.put.poznan.pl/?do=OAuth&response_type=token&client_id=${systemId}`
                console.log('Redirecting to authorization.');
            }
        }
    }, [dispatch, user, role]);

    return (
        <div className={"flex items-center justify-center h-screen"}>
            <CircularProgress/>
        </div>
    );
}
export default Login;
