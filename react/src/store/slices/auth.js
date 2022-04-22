import Axios from 'axios';
import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';

const initialState = {
    isAuthenticated: false,
    user: null,
};

const setAccessToken = (accessToken) => {
    if (accessToken) {
        localStorage.setItem('access_token', accessToken);
        Axios.defaults.headers.common.Authorization = `Bearer ${accessToken}`;
    } else {
        localStorage.removeItem('access_token');
        delete Axios.defaults.headers.common.Authorization;
    }
};

export const login = createAsyncThunk('auth/login', async (payload) => {
    const { data: token } = await Axios.post('/api/v1/login', {
        email: payload.email,
        password: payload.password,
    });

    setAccessToken(token);
    const responseUser = await Axios.get('/api/v1/user');
    return responseUser.data;
});

export const logout = createAsyncThunk('auth/logout', () => {
    setAccessToken(null);
});

const slice = createSlice({
    name: 'auth',
    initialState,
    reducers: {},
    extraReducers: (builder) => {
        builder.addCase(login.fulfilled, (state, { payload }) => {
            state.isAuthenticated = true;
            state.user = payload;
        });
        builder.addCase(logout.fulfilled, (state) => {
            state.user = null;
            state.isAuthenticated = false;
        });
    },
});

export const { reducer } = slice;

export default slice;