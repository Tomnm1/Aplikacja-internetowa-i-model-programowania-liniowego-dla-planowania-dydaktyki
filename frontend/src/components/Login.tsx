import React from 'react';
import {useLocation} from 'react-router-dom';

const Login: React.FC = () => {


    const location = useLocation();

    const queryParams = new URLSearchParams(location.search);

    const accessToken = queryParams.get('access_token');
    const state = queryParams.get('state');

    React.useEffect(() => {
        console.log(queryParams);
        if (accessToken) {
            console.log('Access Token:', accessToken);
        }
        if (state) {
            console.log('State:', state);
        }
    }, [accessToken, state]);


    return (
        <div>
            <h1>SSO Callback</h1>
            {accessToken ? (
                <p>Odebrano access_token: {accessToken}</p>
            ) : (
                <p>Brak tokenu w URL</p>
            )}
            {state && <p>Dodatkowe dane (state): {state}</p>}
        </div>
    );
}
export default Login;
