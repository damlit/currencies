import { configureStore } from '@reduxjs/toolkit';
import userRoleReducer from './reducers/user.reducer';

export default configureStore({
    reducer: {
        user: userRoleReducer,
    }
});
