import { REQUEST_USER_ROLE, RECEIVE_USER_ROLE, FAILED_USER_ROLE } from "../types/user.types";

const getInitialState = () => ({
    role: '',
    loading: false
});

const requestUserRole = (state) => ({
    ...state,
    loading: true
});

const receiveUserRole = (action) => {
    return {
        role: action.role,
        loading: false
    }
}

const failedUserRole = (state) => ({
    ...state,
    loading: false
})

const userRoleReducer = (state = getInitialState(), action) => {
    switch (action.type) {
        case REQUEST_USER_ROLE:
            return requestUserRole(state);
        case RECEIVE_USER_ROLE:
            return receiveUserRole(action);
        case FAILED_USER_ROLE:
            return failedUserRole(state);
        default:
            return getInitialState();
    }
};

export default userRoleReducer;