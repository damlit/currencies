import {getRequest, postRequest} from "./util/request.util";
import {getAuthHeader, getContentJsonHeaderWithAuthorization} from "./util/headers.utils";

export const getLastCurrencies = async (setState, currency) => {
    const response = await getRequest('/api/v1/currencies/last?currency=' + currency, getAuthHeader());
    const status = response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getCurrenciesByDate = async (setState, currency, date) => {
    const response = await getRequest('/api/v1/currencies/date?date=' + date.toLocaleDateString() + '&currency=' + currency, getAuthHeader());
    const status = response.status;
    if (status === 200) {
        try {
            const jsonResponse = await response.json();
            setState(jsonResponse);
        } catch (e) {
            setState(null);
        }
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getProfit = async (setState) => {
    const response = await getRequest('/api/v1/pocket/profit', getAuthHeader());
    const status = response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getDeposits = async (setState, page) => {
    const response = await getRequest('/api/v1/pocket/deposit?page=' + page + '&size=5', getAuthHeader());
    const status = response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getAmountOfDeposits = async (setState) => {
    const response = await getRequest('/api/v1/pocket/deposit/count', getAuthHeader());
    const status = response.status;
    const textResponse = await response.text();
    if (status === 200) {
        const numberOfDeposits = Number(textResponse);
        setState(numberOfDeposits);
    } else {
        setState(0);
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const removeDeposit = async (id) => {
    const response = await getRequest('/api/v1/pocket/deposit/remove/' + id, getAuthHeader());
    const status = response.status;
    const textResponse = await response.text();
    if (status === 200) {
        alert(textResponse);
    } else {
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const addDeposit = async (deposit) => {
    const body = JSON.stringify(deposit);
    const response = await postRequest('/api/v1/pocket/deposit/add', getContentJsonHeaderWithAuthorization(), body);
    const status = response.status;
    const textResponse = await response.text();
    if (status === 200) {
        alert(textResponse);
    } else {
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const updateCurrencies = async () => {
    const response = await getRequest('/api/v1/currencies/update', getAuthHeader());
    const status = response.status;
    const textResponse = await response.text();
    if (status === 200) {
        alert(textResponse);
    } else {
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}