import { useEffect, useMemo, useState } from 'react';
import { removeDeposit, addDeposit } from '../request/currencies.request';
import {
    DepositsWrapper,
    DepositLabel,
    FormRow,
    FormField,
    DepositForm,
    DepositFrame,
    DepositInput
} from './Deposits.styled';
import { Deposit } from './deposit.types';
import ChooseCurrency from '../ChooseCurrency';
import { BlueButton, ButtonGroup, ButtonToggle } from '../SmallComponents/BlueButton.styled';
import { Card } from '../SmallComponents/Card.styled';
import { makeFunctionIfFieldsHasBeenFilled } from '../utils/validationFunctions.utils';
import { useTranslation } from 'react-i18next';
import { changeNumberToPages } from '../utils/pages.utils';
import { useDispatch, useSelector } from '../store';
import { depositsActions } from '../store/slices/Deposits';

const PAGE_LIMIT = 5;

const Deposits = () => {
    const dispatch = useDispatch();
    const {t} = useTranslation('common');
    const { depositsByPage } = useSelector(state => state.deposits);

    const [currentDepositsPage, setCurrentDepositsPage] = useState(0);

    const [soldSum, setSoldSum] = useState(100);
    const [soldCurrency, setSoldCurrency] = useState('PLN');
    const [quote, setQuote] = useState(1.0);
    const [boughtCurrency, setBoughtCurrency] = useState('EUR');

    const getDepositsParams = useMemo(() => ({ page: currentDepositsPage, size: PAGE_LIMIT }), [currentDepositsPage]);

    useEffect(() => {
        dispatch(depositsActions.getDeposits(getDepositsParams));
    }, [dispatch, getDepositsParams]);

    const handleRemoveDeposit = async (e, id) => {
        await removeDeposit(id);
        dispatch(depositsActions.getDeposits(getDepositsParams));
    }

    const handleAddDeposit = async (e) => {
        await makeFunctionIfFieldsHasBeenFilled(addNewDeposit, e, [soldCurrency, boughtCurrency]);
        dispatch(depositsActions.getDeposits(getDepositsParams));
    }

    const addNewDeposit = async (e) => {
        const deposit = new Deposit(soldCurrency, boughtCurrency, Number(quote), Number(soldSum));
        await addDeposit(deposit);
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
                        <ChooseCurrency value={soldCurrency} onChangeHandler={handleSoldCurrency}/>
                    </FormField>
                    <FormField>
                        <p>{t('deposits.amountOfSoldCurr')}</p>
                        <DepositInput type="number" defaultValue={100} onChange={e => setSoldSum(e.target.value)}/>
                    </FormField>
                </FormRow>
                <FormRow>
                    <FormField>
                        <p>{t('deposits.boughtCurr')}</p>
                        <ChooseCurrency value={boughtCurrency} onChangeHandler={handleBoughtCurrency}/>
                    </FormField>
                    <FormField>
                        <p>{t('deposits.quote')}</p>
                        <DepositInput type="number" step="0.0001" defaultValue={1.0}
                                      onChange={e => setQuote(e.target.value)}/>
                    </FormField>
                </FormRow>
                <div>
                    <BlueButton type="submit">{t('deposits.add')}</BlueButton>
                </div>
            </DepositForm>
        </Card>
        <Card>
            <span>{t('deposits.yourDeposits')} ({depositsByPage.totalElements}):</span>
            {depositsByPage.content
                ? depositsByPage.content.map(deposit =>
                    <DepositFrame key={'depositNr_' + deposit.id}>
                        <DepositLabel>{deposit.soldSum} {deposit.soldCurrency} {t('deposits.hasBeenSold')} {deposit.boughtSum} {deposit.boughtCurrency} (id={deposit.id}).</DepositLabel>
                        <BlueButton
                            onClick={e => handleRemoveDeposit(e, deposit.id)}>{t('deposits.remove')}</BlueButton>
                    </DepositFrame>
                )
                : 'Loading...'}
            <div>
                <ButtonGroup>
                    {depositsByPage.totalElements
                        ? changeNumberToPages(depositsByPage.totalElements, PAGE_LIMIT).map(pageNumber =>
                            <ButtonToggle key={'depositPage_' + pageNumber}
                                          onClick={e => handleChangePageNumber(e, pageNumber)}
                                          active={pageNumber === currentDepositsPage + 1}>
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