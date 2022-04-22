import { createSlice } from '@reduxjs/toolkit';
import buildQuotesExtraReducers from './Quotes.extraReducers';
import * as userThunks from './Quotes.thunks';

const initialState = {
    loading: false,
    data: {},
}

export const slice = createSlice({
    name: 'quotes',
    initialState,
    reducers: {},
    extraReducers: buildQuotesExtraReducers,
});

export const quotesReducer = slice.reducer;

export const quotesSelector = (state) => state.quotes;

export const quotesActions = {
    ...slice.actions,
    ...userThunks,
}