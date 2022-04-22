import Axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';

export const getQuotes = createAsyncThunk(
    'quotes/get',
    async (payload) => Axios.get(`/api/v1/currencies/date?currency=${payload.currency}&date=${payload.date}`)
        .then(response => response.data)
);
