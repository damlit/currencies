
export const postRequest = async (endpoint, headers, body) => {
    var loginOptions = {
        method: 'POST',
        headers: headers,
        body: body,
        redirect: 'follow'
    };
    try {
        const response = await fetch(endpoint, loginOptions);
        const status = await response.status;
        const textResponse = await response.text();
        if (status >= 200 || status < 300) {
            return textResponse;
        }
        throw new Error('Something went wrong. Server response with code ' + status + '.');
    } catch (error) {
        return console.log('error', error);
    }
}

export const getRequest = async (endpoint, headers, json) => {
    var loginOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };
    try {
        const response = await fetch(endpoint, loginOptions);
        const status = await response.status;
        const convertedResponse = json ? response.json() : response.text();
        if (status >= 200 || status < 300) {
            return convertedResponse;
        }
        throw new Error('Something went wrong. Server response with code ' + status + '.');
    } catch (error) {
        return console.log('error', error);
    }
}
