import { useEffect, useState } from "react";
import { getLastCurrencies } from "../request/currencies.request";
import PropTypes from 'prop-types';
import { QuotesWrapper } from './Quotes.styled.js';
import { getDateFromTimestamp } from "./quotes.utils";
import ChooseCurrency from "../ChooseCurrency";

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
            <ChooseCurrency value={targetCurrency} onChangeHandler={handleChangeTargetCurrency} />
        </div>
        <div>
            <span>Date: {lastCurrencies && getDateFromTimestamp(lastCurrencies.quotesDate)}</span>
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