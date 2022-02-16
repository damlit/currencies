export const changeNumberToPages = (numbersToChange, pageLimit, setState) => {
    const pageArray = [];
    var currentPage = 1;
    for (var i = 0; i < numbersToChange; i = i + pageLimit) {
        pageArray.push(currentPage);
        currentPage++;
    }
    setState(pageArray);
}