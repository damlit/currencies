import { useEffect, useState } from "react";
import { getDeposits, removeDeposit, addDeposit } from "../request/currencies.request";
import PropTypes from 'prop-types';
import { DepositsWrapper } from "./Deposits.styled";
import { Deposit } from "./deposit.types";

const Deposits = ({ token }) => {

    const [deposits, setDeposits] = useState([]);
    const [depositsPage, setDepositsPage] = useState(0);

    const [soldSum, setSoldSum] = useState(0.0);
    const [soldCurrency, setSoldCurrency] = useState('PLN');
    const [quote, setQuote] = useState(0.0);
    const [boughtCurrency, setBoughtCurrency] = useState('EUR');

    useEffect(() => {
        getDeposits(token, setDeposits, depositsPage);
    }, [token, depositsPage]);

    const handleRemoveDeposit = (e, id) => {
        removeDeposit(token, id);
        getDeposits(token, setDeposits, depositsPage);
    }

    const handleAddDeposit = (e) => {
        const deposit = new Deposit(soldCurrency, boughtCurrency, Number(quote), Number(soldSum));
        addDeposit(token, deposit);
        getDeposits(token, setDeposits, depositsPage);
    }

    const handleSoldCurrency = (e) => {
        setSoldCurrency(e.target.value);
    }

    const handleBoughtCurrency = (e) => {
        setBoughtCurrency(e.target.value);
    }

    return <DepositsWrapper>
        <div>
            <form onSubmit={handleAddDeposit} id="addDepositForm">
                <label>
                    <p>Amount of sold currency</p>
                    <input type="number" step="0.01" onChange={e => setSoldSum(e.target.value)} />
                </label>
                <label>
                    <p>Sold currency</p>
                    <select value={soldCurrency} onChange={handleSoldCurrency}>
                        <option value="PLN">PLN</option>
                        <option value="USD">USD</option>
                        <option value="EUR">EUR</option>
                        <option value="GBP">GBP</option>
                    </select>
                </label>
                <label>
                    <p>Quote</p>
                    <input type="number" step="0.0001" onChange={e => setQuote(e.target.value)} />
                </label>
                <label>
                    <p>Bought currency</p>
                    <select value={boughtCurrency} onChange={handleBoughtCurrency}>
                        <option value="PLN">PLN</option>
                        <option value="USD">USD</option>
                        <option value="EUR">EUR</option>
                        <option value="GBP">GBP</option>
                    </select>
                </label>
                <div>
                    <button type="submit">Add</button>
                </div>
            </form>
        </div>
        <span>Your deposits:</span>
        {deposits
            ? deposits.map(deposit =>
                <>
                    <div key={deposit.id}>
                        {deposit.id}. {deposit.soldSum} {deposit.soldCurrency} has been sold for {deposit.boughtSum} {deposit.boughtCurrency}.
                        <button onClick={ev => handleRemoveDeposit(ev, deposit.id)}>Remove</button>
                    </div>
                </>
            )
            : ""}
    </DepositsWrapper>
}

Deposits.propTypes = {
    token: PropTypes.string.isRequired
};

export default Deposits;