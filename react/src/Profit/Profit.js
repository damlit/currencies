import { useEffect, useState } from "react";
import { getProfit } from "../request/currencies.request";
import { BorderLabel, BorderWrapper } from "../SmallComponents/BorderComponents.styled";
import { ProfitWrapper } from "./Profit.styles";

const Profit = () => {
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(setUserProfit);
    }, []);

    return <ProfitWrapper>
        <BorderWrapper>
            <span>If you will sell all currencies today your profit equals:
                {<BorderLabel dotted>{userProfit.profit} PLN</BorderLabel>}
            </span>
        </BorderWrapper>
    </ProfitWrapper>
}

export default Profit;