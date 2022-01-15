import PropTypes from 'prop-types';
import { ChooseCurrencyWrapper } from './ChooseCurrency.styled';

const ChooseCurrency = ({ value, onChangeHandler }) => {

    return <ChooseCurrencyWrapper>
        <select value={value} onChange={onChangeHandler}>
            <option value="PLN">PLN</option>
            <option value="USD">USD</option>
            <option value="EUR">EUR</option>
            <option value="GBP">GBP</option>
        </select>
    </ChooseCurrencyWrapper>
}

ChooseCurrency.propTypes = {
    value: PropTypes.string.isRequired,
    onChangeHandler: PropTypes.func.isRequired
};

export default ChooseCurrency;