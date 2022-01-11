import { useEffect, useState } from "react";
import PropTypes from 'prop-types';
import { getProfit } from "../request/currencies.request";
import { ProfitWrapper } from "./Profit.styles";

const Profit = ({ token }) => {
    const [userProfit, setUserProfit] = useState({ profit: 0.0 });

    useEffect(() => {
        getProfit(token, setUserProfit);
    }, [token]);

    return <ProfitWrapper>
        <span>If you will sell all currencies today your profit equals: {userProfit.profit} PLN</span>
    </ProfitWrapper>
}

Profit.propTypes = {
    token: PropTypes.string.isRequired
};

export default Profit;