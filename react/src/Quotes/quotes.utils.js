
export const getDateFromTimestamp = (timestamp) => {
    const date = new Date(timestamp);
    const formattedDate = date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getUTCFullYear();
    return formattedDate;
}