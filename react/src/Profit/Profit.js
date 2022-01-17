import { useEffect, useState } from "react";
import { getProfit } from "../request/currencies.request";
import { ProfitWrapper } from "./Profit.styles";

const Profit = () => {
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(setUserProfit);
    }, []);

    return <ProfitWrapper>
        <span>If you will sell all currencies today your profit equals: {userProfit.profit} PLN</span>
    </ProfitWrapper>
}

export default Profit;