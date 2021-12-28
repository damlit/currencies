
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

export const getRequest = async (endpoint, headers) => {
    var loginOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };
    try {
        const response = await fetch(endpoint, loginOptions);
        const jsonResponse = await response.json();
        return jsonResponse;
    } catch (error) {
        return console.log('error', error);
    }
}
