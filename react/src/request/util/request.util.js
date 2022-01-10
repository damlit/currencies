
export const postRequest = async (endpoint, headers, body) => {
    var loginOptions = {
        method: 'POST',
        headers: headers,
        body: body,
        redirect: 'follow'
    };
    try {
        const response = await fetch(endpoint, loginOptions);
        return response;
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
        return response;
    } catch (error) {
        return console.log('error', error);
    }
}
