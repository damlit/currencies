import { useState } from 'react';

export const TOKEN_KEY = 'access_token';

const useToken = () => {

    const getToken = () => localStorage.getItem(TOKEN_KEY);

    const [token, setToken] = useState(getToken());

    const saveToken = userToken => {
        localStorage.setItem(TOKEN_KEY, JSON.stringify(userToken));
        setToken(userToken);
    };

    const cleanToken = () => {
        localStorage.removeItem(TOKEN_KEY);
    };

    return {
        setToken: saveToken,
        token,
        cleanToken
    }
}

export default useToken;