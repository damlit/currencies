import { postRequest } from "./util/request.util";
import { getContentJsonHeader } from "./util/headers.utils";

export const getAuthToken = async (email, password) => {
    var body = JSON.stringify({ email, password });

    return postRequest('/api/v1/login', getContentJsonHeader(), body);
}