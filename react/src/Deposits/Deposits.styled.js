import styled from 'styled-components';

export const DepositsWrapper = styled.div`
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
`;

export const DepositLabel = styled.label`
  width: 100%;
  margin: 5px 15px;  
  padding: 5px 15px;
`;

export const BorderForm = styled.form`
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: space-evenly;
  margin: 5px 15px;  
  padding: 5px 15px;
  border-width: medium;
  border-style: solid;

  @media (max-width: ${({ theme }) => theme.mobile}) {    
    flex-direction: column;
    background-color: pink;
  }
`;

export const FormRow = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  flex-direction: row;
  justify-content: space-evenly;

  @media (max-width: ${({ theme }) => theme.mobile}) {    
    flex-direction: column;
    background-color: gray;
  }
`;

export const FormField = styled.label`
  width: 50%;
  margin: 5px 15px;  
  padding: 5px 15px;
  border-width: medium;
  border-style: dotted;
`;

export const DepositFrame = styled.div`
    margin: 5px 15px;  
    padding: 5px 15px;
    border-width: ${props => props.thin ? 'thin' : 'medium'};
    border-style: ${props => props.dotted ? 'dotted' : 'solid'};
`;