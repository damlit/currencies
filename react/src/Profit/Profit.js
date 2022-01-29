import { useEffect, useState } from "react";
import { getProfit } from "../request/currencies.request";
import { Card } from "../SmallComponents/Card.styled";
import { ProfitLabel } from "./Profit.styles";

const Profit = () => {
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(setUserProfit);
    }, []);

    return <Card>
            <span>If you will sell all currencies today your profit equals:
                {<ProfitLabel>{userProfit.profit} PLN</ProfitLabel>}
            </span>
    </Card>
}

export default Profit;