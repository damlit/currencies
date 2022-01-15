import { getRequest, postRequest } from "./util/request.util";
import { getAuthHeader, getContentJsonHeader } from "./util/headers.utils";

export const getAuthToken = async (email, password) => {
    var body = JSON.stringify({ email, password });

    const response = await postRequest('/api/v1/login', getContentJsonHeader(), body);
    const status = await response.status;
    const token = await response.text();
    return { status, token };
}

export const getRegistrationToken = async (email, password) => {
    var body = JSON.stringify({ email, password });

    const response = await postRequest('/api/v1/registration', getContentJsonHeader(), body);
    const status = await response.status;
    const confirmationToken = await response.text();
    return { status, confirmationToken };
}

export const sendConfirmationToken = async (token) => {
    const response = await getRequest('/api/v1/registration/confirm?token=' + token, new Headers());

    const status = await response.status;
    const textResponse = await response.text();
    return { status, textResponse };
}

export const isAdminUserRole = async (token) => {
    const response = await getRequest('/api/v1/user/role', getAuthHeader(token));

    const status = await response.status;
    const textResponse = await response.text();
    if (status === 200 && textResponse === '"ADMIN"') {
        return true;
    }
    return false;
}
