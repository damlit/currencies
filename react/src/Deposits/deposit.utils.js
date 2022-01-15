export const changeNumberToPages = (numberOfDeposits, pageLimit, setState) => {
    const pageArray = [];
    var currentPage = 1;
    for(var i = 0; i < numberOfDeposits; i = i + pageLimit) {
        pageArray.push(currentPage);
        currentPage++;
    }
    setState(pageArray);
}