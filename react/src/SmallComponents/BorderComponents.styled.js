import styled from 'styled-components';

export const BorderLabel = styled.label`
    margin: 5px 15px;  
    padding: 5px 15px;
    border-width: ${props => props.thin ? 'thin' : 'medium'};
    border-style: ${props => props.dotted ? 'dotted' : 'solid'};
`;

export const BorderForm = styled.form`
    margin: 5px 15px;  
    padding: 5px 15px;
    border-width: ${props => props.thin ? 'thin' : 'medium'};
    border-style: ${props => props.dotted ? 'dotted' : 'solid'};
`;

export const BorderWrapper = styled.div`
    margin: 5px 15px;  
    padding: 5px 15px;
    border-width: ${props => props.thin ? 'thin' : 'medium'};
    border-style: ${props => props.dotted ? 'dotted' : 'solid'};
`;