import { createSlice } from '@reduxjs/toolkit';
import buildProfitExtraReducers from './Profit.extraReducers';
import * as userThunks from './Profit.thunks';

const initialState = {
    loading: false,
    data: {},
}

export const slice = createSlice({
    name: 'profit',
    initialState,
    reducers: {},
    extraReducers: buildProfitExtraReducers,
});

export const profitReducer = slice.reducer;

export const profitSelector = (state) => state.profit;

export const profitActions = {
    ...slice.actions,
    ...userThunks,
}