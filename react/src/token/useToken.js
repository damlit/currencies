import { useState } from 'react';

export const TOKEN_KEY = 'token';

const useToken = () => {

    const getToken = () => {
        const tokenString = localStorage.getItem(TOKEN_KEY);
        const userToken = JSON.parse(tokenString);
        return userToken;
    };

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