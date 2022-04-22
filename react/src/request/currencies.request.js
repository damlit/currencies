import { getRequest, postRequest } from './util/request.util';
import { getAuthHeader, getContentJsonHeaderWithAuthorization } from './util/headers.utils';

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