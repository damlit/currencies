import { useEffect, useState } from "react";
import { getLastCurrencies } from "../request/currencies.request";
import { QuotesLabel, QuotesWrapper } from './Quotes.styled.js';
import { getDateFromTimestamp } from "./quotes.utils";
import ChooseCurrency from "../ChooseCurrency";
import { Card } from "../SmallComponents/Card.styled";

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
        <Card key={"quotesTab"} margin={"2rem 2rem 1rem 2rem"}>
            <span>Choose target currency: </span>
            <ChooseCurrency value={targetCurrency} onChangeHandler={handleChangeTargetCurrency} />
        </Card>
        <Card margin={"1rem 2rem 2rem 2rem"}>
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
        </Card>
    </QuotesWrapper>
}

export default Quotes;