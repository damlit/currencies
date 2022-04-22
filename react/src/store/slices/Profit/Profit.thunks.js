import Axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';

export const getProfit = createAsyncThunk(
    'profit/get',
    () => Axios.get('/api/v1/pocket/profit')
        .then(response => response.data)
);
