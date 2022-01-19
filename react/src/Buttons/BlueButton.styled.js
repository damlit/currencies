import styled from 'styled-components';
import { BlueButtonColor, BlueHoverButtonColor } from '../colors';

export const BlueButton = styled.button`
    background-color: ${BlueButtonColor};
    color: white;
    padding: 5px 15px;
    border-radius: 5px;
    outline: 0;
    text-transform: uppercase;
    margin: 10px 0px;
    cursor: pointer;
    box-shadow: 0px 2px 2px lightgray;
    transition: ease background-color 250ms;
    
    &:hover {
        background-color: ${BlueHoverButtonColor};
    }

    &:disabled {
        cursor: default;
        opacity: 0.7;
    }
`;

export const ButtonToggle = styled(BlueButton)`
    opacity: 0.6;
    
    ${({ active }) =>
        active &&
        `
        opacity: 1;
        `}
`;

const ButtonGroup = styled.div`
    display: flex;
`;