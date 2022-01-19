import { useEffect, useState } from "react";
import { getLastCurrencies } from "../request/currencies.request";
import PropTypes from 'prop-types';
import { QuotesWrapper } from './Quotes.styled.js';
import { getDateFromTimestamp } from "./quotes.utils";
import ChooseCurrency from "../ChooseCurrency";
import { BorderLabel, BorderWrapper } from "../SmallComponents/BorderComponents.styled";

const Quotes = () => {

    const [targetCurrency, setTargetCurrency] = useState('PLN');
    const [lastCurrencies, setLastCurrencies] = useState();

    useEffect(() => {
        getLastCurrencies(setLastCurrencies, targetCurrency);
    }, [targetCurrency]);

    const handleChangeTargetCurrency = (e) => {
        setTargetCurrency(e.target.value);
    }

    return <QuotesWrapper>
        <BorderWrapper key={"quotesTab"}>
            <span>Choose target currency: </span>
            <ChooseCurrency value={targetCurrency} onChangeHandler={handleChangeTargetCurrency} />
        </BorderWrapper>
        <div>
            <BorderWrapper>
                <BorderLabel dotted>
                    <span>Date: {lastCurrencies && getDateFromTimestamp(lastCurrencies.quotesDate)}</span></BorderLabel>
                {lastCurrencies
                    ? lastCurrencies.quotes.map(quote =>
                        <div key={quote.id}>
                            {quote.currency} has quote {quote.quote}
                        </div>
                    )
                    : ""}
            </BorderWrapper>
        </div>
    </QuotesWrapper>
}

Quotes.propTypes = {
    token: PropTypes.string.isRequired
};

export default Quotes;