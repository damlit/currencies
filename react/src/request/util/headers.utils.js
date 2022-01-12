
export const getAuthHeader = (token) => {
    var headers = new Headers();
    headers.append("Authorization", "Bearer " + token);
    return headers;
};

export const getContentJsonHeader = () => {
    var headers = new Headers();
    headers.append("Content-Type", "application/json");
    return headers;
};

export const getContentJsonHeaderWithAuthorization = (token) => {
    var headers = new Headers();
    headers.append("Content-Type", "application/json");
    headers.append("Authorization", "Bearer " + token);
    return headers;
};
