import React, {useEffect} from 'react';
import {useLocation, useNavigate, useSearchParams} from 'react-router-dom';
import {useDispatch} from "react-redux";
import {loginUser} from "../app/slices/authSlice.ts";
import {useAppSelector} from "../hooks/hooks.ts";

const Login: React.FC = () => {
    const { isAuthenticated, role, user } = useAppSelector((state) => state.auth);


    // const location = useLocation();
    //
    // const queryParams = new URLSearchParams(location.search);
    //
    // const accessToken = queryParams.get('access_token');
    // const state = queryParams.get('state');
    //
    // React.useEffect(() => {
    //     console.log("AAA")
    //     console.log(queryParams);
    //     if (accessToken) {
    //         console.log('Access Token:', accessToken);
    //     }
    //     if (state) {
    //         console.log('State:', state);
    //     }

    // }, [accessToken, state]);

    const navigate = useNavigate();
    const dispatch = useDispatch();
    const [searchParams, setSearchParams] = useSearchParams();
    useEffect(() => {
        const token = searchParams.get("access_token");
        console.log('location.hash:', token);
        if (token) {
            console.log('Extracted accessToken:', token);
                localStorage.setItem('access_token', token);
                dispatch(loginUser(token));
            if (!isAuthenticated) {
                console.log("not authed")
                navigate("/login");
            }

            if (role === 'ROLE_ADMIN') {
                navigate("/");
            }

            if (role === 'ROLE_TEACHER') {
                navigate("/user");
            }

            if (!user) {
                console.log("no user")
                return <div>Ładowanie danych użytkownika...</div>;
            }
                console.log('Access token processed and stored.');

        } else {
            const storedToken = localStorage.getItem('access_token');
            console.log('Stored accessToken:', storedToken);

            if (storedToken) {
                dispatch(loginUser(storedToken));
                console.log('Initialized Redux state from stored token.');
            } else {
                const systemId = 'planner-dev.esys.put.poznan.pl';
                window.location.href = `https://elogin.put.poznan.pl/?do=OAuth&response_type=token&client_id=${systemId}`
                console.log('Redirecting to authorization.');
            }
        }
    }, [dispatch]);


//     const systemId = 'planner-dev.esys.put.poznan.pl';
//     window.location.href = `https://elogin.put.poznan.pl/?do=OAuth
// &response_type=token&do=Authorize&client_id=${systemId}&scope=ekonto.user`;
//     console.log('Redirecting to authorization.');

    const handleLogin = () => {
        const systemId = 'planner-dev.esys.put.poznan.pl';
        window.location.href = `https://elogin.put.poznan.pl/?do=Authorize&system=${systemId}`;
    };
    return (
        <div>
            <button onClick={handleLogin}>
                Zaloguj się przez eKonto
            </button>
            {/*    <h1>SSO Callback</h1>*/}
            {/*    {accessToken ? (*/}
            {/*        <p>Odebrano access_token: {accessToken}</p>*/}
            {/*    ) : (*/}
            {/*        <p>Brak tokenu w URL</p>*/}
            {/*    )}*/}
            {/*    {state && <p>Dodatkowe dane (state): {state}</p>}*/}
        </div>
    );
}
export default Login;
