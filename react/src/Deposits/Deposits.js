import { useEffect, useState } from "react";
import { getDeposits, removeDeposit, addDeposit, getAmountOfDeposits } from "../request/currencies.request";
import { DepositsWrapper, DepositLabel } from "./Deposits.styled";
import { Deposit } from "./deposit.types";
import ChooseCurrency from "../ChooseCurrency";
import { changeNumberToPages } from "./deposit.utils";
import { BlueButton, ButtonGroup, ButtonToggle } from "../SmallComponents/BlueButton.styled";
import { BorderLabel, BorderForm, BorderWrapper } from "../SmallComponents/BorderComponents.styled";

const Deposits = () => {

    const [deposits, setDeposits] = useState([]);
    const [currentDepositsPage, setCurrentDepositsPage] = useState(0);
    const [depositsPages, setDepositsPages] = useState([]);
    const [amountOfDeposits, setAmountOfDeposits] = useState([]);

    const [soldSum, setSoldSum] = useState(0.0);
    const [soldCurrency, setSoldCurrency] = useState('PLN');
    const [quote, setQuote] = useState(0.0);
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
        <div>
            <BorderForm onSubmit={handleAddDeposit} id="addDepositForm">
                <BorderLabel dotted>
                    <p>Amount of sold currency</p>
                    <input type="number" onChange={e => setSoldSum(e.target.value)} />
                </BorderLabel>
                <BorderLabel dotted>
                    <p>Sold currency</p>
                    <ChooseCurrency value={soldCurrency} onChangeHandler={handleSoldCurrency} />
                </BorderLabel>
                <BorderLabel dotted>
                    <p>Quote</p>
                    <input type="number" step="0.0001" onChange={e => setQuote(e.target.value)} />
                </BorderLabel>
                <BorderLabel dotted>
                    <p>Bought currency</p>
                    <ChooseCurrency value={boughtCurrency} onChangeHandler={handleBoughtCurrency} />
                </BorderLabel>
                <div>
                    <BlueButton type="submit">Add</BlueButton>
                </div>
            </BorderForm>
        </div>
        <BorderWrapper>
            <span>Your deposits ({amountOfDeposits}):</span>
            <BorderWrapper dotted>
                {deposits
                    ? deposits.map(deposit =>
                        <BorderWrapper key={'depositNr_' + deposit.id} thin>
                            <DepositLabel>{deposit.soldSum} {deposit.soldCurrency} has been sold for {deposit.boughtSum} {deposit.boughtCurrency} (id={deposit.id}).</DepositLabel>
                            <BlueButton onClick={e => handleRemoveDeposit(e, deposit.id)}>Remove</BlueButton>
                        </BorderWrapper>
                    )
                    : ""}
            </BorderWrapper>
            <DepositsWrapper>
                <ButtonGroup>
                    {depositsPages
                        ? depositsPages.map(pageNumber =>
                            <ButtonToggle onClick={e => handleChangePageNumber(e, pageNumber)} active={pageNumber === currentDepositsPage + 1}>
                                {pageNumber}
                            </ButtonToggle>
                        )
                        : ""}
                </ButtonGroup>
            </DepositsWrapper>
        </BorderWrapper>
    </DepositsWrapper>
}

export default Deposits;