import {useEffect, useState} from "react";
import {getAmountOfDeposits, getProfit} from "../request/currencies.request";
import {Card} from "../SmallComponents/Card.styled";
import {ProfitFrame, ProfitLabel} from "./Profit.styles";
import {useTranslation} from "react-i18next";
import {ButtonGroup, ButtonToggle} from "../SmallComponents/BlueButton.styled";
import {changeNumberToPages} from "../utils/pages.utils";

const Profit = () => {
    const {t} = useTranslation('common');
    const PAGE_LIMIT = 10;

    const [userProfit, setUserProfit] = useState({profit: 0.0, depositsProfits: []});
    const [currentProfitsPage, setCurrentProfitsPage] = useState(0);
    const [amountOfDeposits, setAmountOfDeposits] = useState(0);
    const [profitsPages, setProfitsPages] = useState([]);

    useEffect(() => {
        getProfit(setUserProfit);
        getAmountOfDeposits(setAmountOfDeposits);
        changeNumberToPages(amountOfDeposits, PAGE_LIMIT, setProfitsPages);
    }, [currentProfitsPage, amountOfDeposits]);

    const handleChangePageNumber = (e, pageNumber) => {
        setCurrentProfitsPage(pageNumber - 1);
    }

    return <>
        <Card>
            <span>{t('profit.profitMsg')}:
                {<ProfitLabel>{userProfit.profit} PLN</ProfitLabel>}
            </span>
        </Card>
        <Card>
            {userProfit.depositsProfits.slice(PAGE_LIMIT * currentProfitsPage, PAGE_LIMIT * currentProfitsPage + PAGE_LIMIT).map(depositProfit =>
                <ProfitFrame>{t('profit.depositProfitMsg1')} {depositProfit.depositId}&nbsp;
                    ({depositProfit.soldSum} {depositProfit.soldCurrency} -> {depositProfit.boughtCurrency})&nbsp;
                    {t('profit.depositProfitMsg2')}: {depositProfit.profit} {depositProfit.soldCurrency}
                </ProfitFrame>
            )}
            <div>
                <ButtonGroup>
                    {profitsPages.map(pageNumber =>
                        <ButtonToggle key={'profitPage_' + pageNumber}
                                      onClick={e => handleChangePageNumber(e, pageNumber)}
                                      active={pageNumber === currentProfitsPage + 1}>
                            {pageNumber}
                        </ButtonToggle>
                    )}
                </ButtonGroup>
            </div>
        </Card>
    </>
}

export default Profit;