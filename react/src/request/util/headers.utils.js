import { TOKEN_KEY } from "../../token/useToken";

export const getAuthHeader = () => {
    var headers = new Headers();
    const tokenString = localStorage.getItem(TOKEN_KEY);
    headers.append("Authorization", "Bearer " + tokenString);
    return headers;
};

export const getContentJsonHeader = () => {
    var headers = new Headers();
    headers.append("Content-Type", "application/json");
    return headers;
};

export const getContentJsonHeaderWithAuthorization = () => {
    var headers = getAuthHeader();
    headers.append("Content-Type", "application/json");
    return headers;
};
