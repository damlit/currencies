import { getAuthHeader } from "../../request/util/headers.utils";
import { getRequest } from "../../request/util/request.util";
import { REQUEST_USER_ROLE, RECEIVE_USER_ROLE, FAILED_USER_ROLE } from "../types/user.types";

export const fetchUserRole = () => async (dispatch) => {
    dispatch(requestUserRole());
    
    const response = await getRequest('/api/v1/user/role', getAuthHeader());
    const status = await response.status;
    const textResponse = await response.text();

    if (status === 200) {
        dispatch(receiveUserRole(textResponse));
    } else {
        dispatch(requestUserRoleFailed());
    }
};

const requestUserRole = () => ({ type: REQUEST_USER_ROLE });

const receiveUserRole = (role) => ({
    type: RECEIVE_USER_ROLE,
    role
});

const requestUserRoleFailed = () => ({ type: FAILED_USER_ROLE });