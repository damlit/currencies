import { useDispatch as useReduxDispatch, useSelector as useReduxSelector } from 'react-redux';
import { configureStore } from '@reduxjs/toolkit';
import rootReducer from './rootReducer';

const store = configureStore({
    reducer: rootReducer,
    devTools: true,
});

store.subscribe(() => {
    localStorage.setItem('state', JSON.stringify({}));
});

export const useSelector = useReduxSelector;

export const useDispatch = () => useReduxDispatch();

export default store;
