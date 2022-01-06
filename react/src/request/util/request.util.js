
export const postRequest = async (endpoint, headers, body) => {
    var loginOptions = {
        method: 'POST',
        headers: headers,
        body: body,
        redirect: 'follow'
    };
    try {
        const response = await fetch(endpoint, loginOptions);
        const textResponse = await response.text();
        return textResponse;
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
        const convertedResponse = json ? response.json() : response.text();
        return convertedResponse;
    } catch (error) {
        return console.log('error', error);
    }
}
