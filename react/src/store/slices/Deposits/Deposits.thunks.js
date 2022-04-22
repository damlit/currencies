import Axios from 'axios';
import { createAsyncThunk } from '@reduxjs/toolkit';

export const getDeposits = createAsyncThunk(
    'deposits/get',
    async (payload) => Axios.get(`/api/v1/pocket/deposit?page=${payload.page}&size=${payload.size}`)
        .then(response => response.data)
);
