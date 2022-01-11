import { useEffect, useState } from "react";
import { getLastCurrencies } from "../request/currencies.request";
import PropTypes from 'prop-types';
import { QuotesWrapper } from './Quotes.styled.js';

const Quotes = ({ token }) => {

    const [targetCurrency, setTargetCurrency] = useState('PLN');
    const [lastCurrencies, setLastCurrencies] = useState();

    useEffect(() => {
        getLastCurrencies(token, setLastCurrencies, targetCurrency);
    }, [token, targetCurrency]);

    const handleChangeTargetCurrency = (e) => {
        setTargetCurrency(e.target.value);
    }

    return <QuotesWrapper>
        <div key={"quotesTab"}>
            <span>Choose target currency: </span>
            <select value={targetCurrency} onChange={handleChangeTargetCurrency}>
                <option value="PLN">PLN</option>
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="GBP">GBP</option>
            </select>
        </div>
        <div>
            {lastCurrencies
                ? lastCurrencies.quotes.map(quote =>
                    <div key={quote.id}>
                        {quote.currency} has quote {quote.quote}
                    </div>
                )
                : ""}
        </div>
    </QuotesWrapper>
}

Quotes.propTypes = {
    token: PropTypes.string.isRequired
};

export default Quotes;