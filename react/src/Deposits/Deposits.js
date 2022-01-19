import { useEffect, useState } from "react";
import { getDeposits, removeDeposit, addDeposit, getAmountOfDeposits } from "../request/currencies.request";
import { DepositsWrapper } from "./Deposits.styled";
import { Deposit } from "./deposit.types";
import ChooseCurrency from "../ChooseCurrency";
import { changeNumberToPages } from "./deposit.utils";
import { BlueButton } from "../Buttons/BlueButton.styled";

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
            <form onSubmit={handleAddDeposit} id="addDepositForm">
                <label>
                    <p>Amount of sold currency</p>
                    <input type="number" onChange={e => setSoldSum(e.target.value)} />
                </label>
                <label>
                    <p>Sold currency</p>
                    <ChooseCurrency value={soldCurrency} onChangeHandler={handleSoldCurrency} />
                </label>
                <label>
                    <p>Quote</p>
                    <input type="number" step="0.0001" onChange={e => setQuote(e.target.value)} />
                </label>
                <label>
                    <p>Bought currency</p>
                    <ChooseCurrency value={boughtCurrency} onChangeHandler={handleBoughtCurrency} />
                </label>
                <div>
                    <BlueButton type="submit">Add</BlueButton>
                </div>
            </form>
        </div>
        <div>
            <span>Your deposits ({amountOfDeposits}):</span>
            {deposits
                ? deposits.map(deposit =>
                    <div key={deposit.id}>
                        {deposit.id}. {deposit.soldSum} {deposit.soldCurrency} has been sold for {deposit.boughtSum} {deposit.boughtCurrency}.
                        <BlueButton onClick={e => handleRemoveDeposit(e, deposit.id)}>Remove</BlueButton>
                    </div>
                )
                : ""}
            {depositsPages
                ? depositsPages.map(pageNumber =>
                    <BlueButton onClick={e => handleChangePageNumber(e, pageNumber)}>{pageNumber}</BlueButton>
                )
                : ""}
        </div>
    </DepositsWrapper>
}

export default Deposits;