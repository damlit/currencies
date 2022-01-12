import { getRequest, postRequest } from "./util/request.util";
import { getAuthHeader, getContentJsonHeaderWithAuthorization } from "./util/headers.utils";

export const getLastCurrencies = async (token, setState, currency) => {
    const response = await getRequest('/api/v1/currencies/last?currency=' + currency, getAuthHeader(token));
    const status = await response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getProfit = async (token, setState) => {
    const response = await getRequest('/api/v1/pocket/profit', getAuthHeader(token));
    const status = await response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const getDeposits = async (token, setState, page) => {
    const response = await getRequest('/api/v1/pocket/deposit/' + page + '/10', getAuthHeader(token));
    const status = await response.status;
    if (status === 200) {
        const jsonResponse = await response.json();
        setState(jsonResponse);
    } else {
        const textResponse = await response.text();
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const removeDeposit = async (token, id) => {
    const response = await getRequest('/api/v1/pocket/deposit/remove/' + id, getAuthHeader(token));
    const status = await response.status;
    const textResponse = await response.text();
    if (status === 200) {
        alert(textResponse);
    } else {
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}

export const addDeposit = async (token, deposit) => {
    const body = JSON.stringify(deposit);
    const response = await postRequest('/api/v1/pocket/deposit/add', getContentJsonHeaderWithAuthorization(token), body);
    const status = await response.status;
    const textResponse = await response.text();
    console.log(textResponse);
    if (status === 200) {
        alert(textResponse);
    } else {
        alert('Something went wrong. Server response with code ' + status + ' (' + textResponse + ').');
    }
}