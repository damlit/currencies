import { createSlice } from '@reduxjs/toolkit';
import buildDepositsExtraReducers from './Deposits.extraReducers';
import * as depositsThunks from './Deposits.thunks';

const initialState = {
    loading: false,
    depositsByPage: {},
}

export const slice = createSlice({
    name: 'deposits',
    initialState,
    reducers: {},
    extraReducers: buildDepositsExtraReducers,
});

export const depositsReducer = slice.reducer;

export const depositsSelector = (state) => state.depositsByPage;

export const depositsActions = {
    ...slice.actions,
    ...depositsThunks,
}