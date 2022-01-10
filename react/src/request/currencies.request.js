import { getRequest } from "./util/request.util";
import { getAuthHeader } from "./util/headers.utils";

export const getLastCurrencies = async (token, setState) => {
    const response = await getRequest('/api/v1/currencies/last', getAuthHeader(token), true);
    setState(response);
}