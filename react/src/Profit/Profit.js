import { useEffect, useState } from "react";
import { getProfit } from "../request/currencies.request";
import { ProfitFrame, ProfitLabel, ProfitWrapper } from "./Profit.styles";

const Profit = () => {
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(setUserProfit);
    }, []);

    return <ProfitWrapper>
        <ProfitFrame>
            <span>If you will sell all currencies today your profit equals:
                {<ProfitLabel>{userProfit.profit} PLN</ProfitLabel>}
            </span>
        </ProfitFrame>
    </ProfitWrapper>
}

export default Profit;