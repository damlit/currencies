import { useEffect, useState } from "react";
import { Card } from "../SmallComponents/Card.styled";
import { ProfitFrame, ProfitLabel } from "./Profit.styles";
import { useTranslation } from "react-i18next";
import { ButtonGroup, ButtonToggle } from "../SmallComponents/BlueButton.styled";
import { changeNumberToPages } from "../utils/pages.utils";
import { useDispatch, useSelector } from "../store";
import { profitActions } from "../store/slices/Profit";

const PAGE_LIMIT = 10;

const Profit = () => {
    const dispatch = useDispatch();
    const {t} = useTranslation('common');

    const { profit, depositsProfits } = useSelector(state => state.profit?.data);

    const [currentProfitsPage, setCurrentProfitsPage] = useState(0);

    useEffect(() => {
        dispatch(profitActions.getProfit());
    }, [dispatch]);

    const handleChangePageNumber = (e, pageNumber) => {
        setCurrentProfitsPage(pageNumber - 1);
    }

    return <>
        <Card>
            <span>{t('profit.profitMsg')}:
                {<ProfitLabel>{profit} PLN</ProfitLabel>}
            </span>
        </Card>
        <Card>
            {depositsProfits?.slice(PAGE_LIMIT * currentProfitsPage, PAGE_LIMIT * currentProfitsPage + PAGE_LIMIT).map(depositProfit =>
                <ProfitFrame key={`profit_frame_${depositProfit.depositId}`}>{t('profit.depositProfitMsg1')} {depositProfit.depositId}&nbsp;
                    ({depositProfit.soldSum} {depositProfit.soldCurrency} -> {depositProfit.boughtCurrency})&nbsp;
                    {t('profit.depositProfitMsg2')}: {depositProfit.profit} {depositProfit.soldCurrency}
                </ProfitFrame>
            )}
            <div>
                <ButtonGroup>
                    {changeNumberToPages(depositsProfits ? depositsProfits.length : 0, PAGE_LIMIT).map(pageNumber =>
                        <ButtonToggle key={`profitPage_${pageNumber}`}
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