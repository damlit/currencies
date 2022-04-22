import { useEffect, useState } from 'react';
import { QuotesLabel, QuotesWrapper } from './Quotes.styled.js';
import { getDateFromTimestamp } from './quotes.utils';
import ChooseCurrency from '../ChooseCurrency';
import { Card } from '../SmallComponents/Card.styled';
import { useTranslation } from 'react-i18next';
import DatePicker from 'react-datepicker';

import 'react-datepicker/dist/react-datepicker.css';
import { useDispatch, useSelector } from '../store';
import { quotesActions } from '../store/slices/Quotes';

const Quotes = () => {
    const dispatch = useDispatch();
    const {t} = useTranslation('common');

    const { data: currencies } = useSelector(state => state.quotes);

    const [targetCurrency, setTargetCurrency] = useState('PLN');
    const [quotesDate, setQuotesDate] = useState(new Date());

    useEffect(() => {
        dispatch(quotesActions.getQuotes({currency: targetCurrency, date: quotesDate.toLocaleDateString()}));
    }, [dispatch, targetCurrency, quotesDate]);

    const handleChangeTargetCurrency = (e) => {
        setTargetCurrency(e.target.value);
    }

    const getCurrenciesDate = currencies?.quotesDate
        ? getDateFromTimestamp(currencies.quotesDate)
        : getDateFromTimestamp(quotesDate);

    return <QuotesWrapper>
        <Card key={"quotesTab"} margin={"2rem 2rem 1rem 2rem"}>
            <span>{t('quotes.chooseTarget')}: </span>
            <ChooseCurrency value={targetCurrency} onChangeHandler={handleChangeTargetCurrency}/>
            <span>{t('quotes.chooseDate')}:</span>
            <DatePicker selected={quotesDate} onChange={(date) => setQuotesDate(date)}/>
        </Card>
        <Card margin={"1rem 2rem 2rem 2rem"}>
            <QuotesLabel>
                <span>{t('quotes.date')}: {getCurrenciesDate}</span>
            </QuotesLabel>
            {currencies?.quotes
                ? currencies.quotes.map(quote =>
                    <div key={quote.id}>
                        {quote.currency} {t('quotes.hadQuote')} {quote.quote}
                    </div>
                )
                : <div>{t('quotes.notAvailable')}.</div>}
        </Card>
    </QuotesWrapper>
}

export default Quotes;