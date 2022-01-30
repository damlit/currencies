
export const makeFunctionIfFieldsHasBeenFilled = async (fun, event, fields) => {
    if (hasNotEmptyFields(fields)) {
        await fun(event);
    } else {
        alert('Not all required fields have been filled!');
    }
}

const hasNotEmptyFields = (fields) => {
    var returnValue = true;
    fields.forEach(field => {
        if (field === '') {
            returnValue = false;
        }
    });
    return returnValue;
}