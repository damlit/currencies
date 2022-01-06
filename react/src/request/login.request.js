import { getRequest, postRequest } from "./util/request.util";
import { getContentJsonHeader } from "./util/headers.utils";

export const getAuthToken = async (email, password) => {
    var body = JSON.stringify({ email, password });

    return postRequest('/api/v1/login', getContentJsonHeader(), body);
}

export const getRegistrationToken = async (email, password) => {
    var body = JSON.stringify({ email, password });

    return postRequest('/api/v1/registration', getContentJsonHeader(), body);
}

export const sendConfirmationToken = async (token) => {
    return getRequest('/api/v1/registration/confirm?token=' + token, new Headers(), false);
}
