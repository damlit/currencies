import { getRequest } from "./util/request.util";
import { getAuthHeader } from "./util/headers.utils";

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