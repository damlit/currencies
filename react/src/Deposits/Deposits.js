import { useEffect, useState } from "react";
import { getDeposits, removeDeposit, addDeposit, getAmountOfDeposits } from "../request/currencies.request";
import { DepositsWrapper, DepositLabel, FormRow, FormField, DepositForm, DepositFrame, DepositInput } from "./Deposits.styled";
import { Deposit } from "./deposit.types";
import ChooseCurrency from "../ChooseCurrency";
import { changeNumberToPages } from "./deposit.utils";
import { BlueButton, ButtonGroup, ButtonToggle } from "../SmallComponents/BlueButton.styled";
import { Card } from "../SmallComponents/Card.styled";
import { makeFunctionIfFieldsHasBeenFilled } from "../utils/validationFunctions.utils";
import { useTranslation } from "react-i18next";

const Deposits = () => {
    const { t } = useTranslation('common');

    const [deposits, setDeposits] = useState([]);
    const [currentDepositsPage, setCurrentDepositsPage] = useState(0);
    const [depositsPages, setDepositsPages] = useState([]);
    const [amountOfDeposits, setAmountOfDeposits] = useState([]);

    const [soldSum, setSoldSum] = useState(100);
    const [soldCurrency, setSoldCurrency] = useState('PLN');
    const [quote, setQuote] = useState(1.0);
    const [boughtCurrency, setBoughtCurrency] = useState('EUR');

    useEffect(() => {
        getDeposits(setDeposits, currentDepositsPage);
        getAmountOfDeposits(setAmountOfDeposits);
        changeNumberToPages(amountOfDeposits, 5, setDepositsPages);
    }, [currentDepositsPage, amountOfDeposits]);

    const handleRemoveDeposit = async (e, id) => {
        await removeDeposit(id);
        getDeposits(setDeposits, currentDepositsPage);
    }


    const handleAddDeposit = async (e) => {
        makeFunctionIfFieldsHasBeenFilled(addNewDeposit, e, [soldCurrency, boughtCurrency]);
    }

    const addNewDeposit = async (e) => {
        const deposit = new Deposit(soldCurrency, boughtCurrency, Number(quote), Number(soldSum));
        await addDeposit(deposit);
        getDeposits(setDeposits, currentDepositsPage);
    }

    const handleSoldCurrency = (e) => {
        setSoldCurrency(e.target.value);
    }

    const handleBoughtCurrency = (e) => {
        setBoughtCurrency(e.target.value);
    }

    const handleChangePageNumber = (e, pageNumber) => {
        setCurrentDepositsPage(pageNumber - 1);
    }

    return <DepositsWrapper>
        <Card>
            <DepositForm onSubmit={handleAddDeposit} id="addDepositForm">
                {t('deposits.addDeposit')}:
                <FormRow>
                    <FormField>
                        <p>{t('deposits.soldCurr')}</p>
                        <ChooseCurrency value={soldCurrency} onChangeHandler={handleSoldCurrency} />
                    </FormField>
                    <FormField>
                        <p>{t('deposits.amountOfSoldCurr')}</p>
                        <DepositInput type="number" defaultValue={100} onChange={e => setSoldSum(e.target.value)} />
                    </FormField>
                </FormRow>
                <FormRow>
                    <FormField>
                        <p>{t('deposits.boughtCurr')}</p>
                        <ChooseCurrency value={boughtCurrency} onChangeHandler={handleBoughtCurrency} />
                    </FormField>
                    <FormField>
                        <p>{t('deposits.quote')}</p>
                        <DepositInput type="number" step="0.0001" defaultValue={1.0} onChange={e => setQuote(e.target.value)} />
                    </FormField>
                </FormRow>
                <div>
                    <BlueButton type="submit">{t('deposits.add')}</BlueButton>
                </div>
            </DepositForm>
        </Card>
        <Card>
            <span>{t('deposits.yourDeposits')} ({amountOfDeposits}):</span>
            {deposits
                ? deposits.map(deposit =>
                    <DepositFrame key={'depositNr_' + deposit.id}>
                        <DepositLabel>{deposit.soldSum} {deposit.soldCurrency} {t('deposits.hasBeenSold')} {deposit.boughtSum} {deposit.boughtCurrency} (id={deposit.id}).</DepositLabel>
                        <BlueButton onClick={e => handleRemoveDeposit(e, deposit.id)}>{t('deposits.remove')}</BlueButton>
                    </DepositFrame>
                )
                : ""}
            <div>
                <ButtonGroup>
                    {depositsPages
                        ? depositsPages.map(pageNumber =>
                            <ButtonToggle onClick={e => handleChangePageNumber(e, pageNumber)} active={pageNumber === currentDepositsPage + 1}>
                                {pageNumber}
                            </ButtonToggle>
                        )
                        : ""}
                </ButtonGroup>
            </div>
        </Card>
    </DepositsWrapper>
}

export default Deposits;