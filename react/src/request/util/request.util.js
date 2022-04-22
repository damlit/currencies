
export const postRequest = async (endpoint, headers, body) => {
    const loginOptions = {
        method: 'POST',
        headers: headers,
        body: body,
        redirect: 'follow'
    };
    try {
        return await fetch(endpoint, loginOptions);
    } catch (error) {
        return console.log('error', error);
    }
}

export const getRequest = async (endpoint, headers) => {
    const loginOptions = {
        method: 'GET',
        headers: headers,
        redirect: 'follow'
    };
    try {
        return await fetch(endpoint, loginOptions);
    } catch (error) {
        return console.log('error', error);
    }
}
