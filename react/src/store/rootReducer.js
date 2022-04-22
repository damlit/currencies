import { combineReducers } from '@reduxjs/toolkit';
import { depositsReducer as deposits } from './slices/Deposits';
import { profitReducer as profit } from './slices/Profit';
import { quotesReducer as quotes } from './slices/Quotes';
import { reducer as auth } from './slices/auth';

const rootReducer = combineReducers({
    auth,
    deposits,
    profit,
    quotes,
});

export default rootReducer;