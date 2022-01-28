import { useEffect, useState } from "react";
import { getLastCurrencies } from "../request/currencies.request";
import { QuotesFrame, QuotesLabel, QuotesWrapper } from './Quotes.styled.js';
import { getDateFromTimestamp } from "./quotes.utils";
import ChooseCurrency from "../ChooseCurrency";

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
        <QuotesFrame key={"quotesTab"}>
            <span>Choose target currency: </span>
            <ChooseCurrency value={targetCurrency} onChangeHandler={handleChangeTargetCurrency} />
        </QuotesFrame>
        <QuotesFrame>
            <QuotesLabel>
                <span>Date: {lastCurrencies && getDateFromTimestamp(lastCurrencies.quotesDate)}</span>
            </QuotesLabel>
            {lastCurrencies
                ? lastCurrencies.quotes.map(quote =>
                    <div key={quote.id}>
                        {quote.currency} has quote {quote.quote}
                    </div>
                )
                : ""}
        </QuotesFrame>
    </QuotesWrapper>
}

export default Quotes;