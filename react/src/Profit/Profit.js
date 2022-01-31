import { useEffect, useState } from "react";
import { getProfit } from "../request/currencies.request";
import { Card } from "../SmallComponents/Card.styled";
import { ProfitLabel } from "./Profit.styles";
import { useTranslation } from "react-i18next";

const Profit = () => {
    const { t } = useTranslation('common');
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(setUserProfit);
    }, []);

    return <Card>
            <span>{t('profit.profitMsg')}:
                {<ProfitLabel>{userProfit.profit} PLN</ProfitLabel>}
            </span>
    </Card>
}

export default Profit;