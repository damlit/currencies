import styled from 'styled-components';

export const Card = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 90%;
  margin: ${props => props.margin ? props.margin : '2rem 2rem'};
  padding: 1rem;
  border-radius: 15px;
  background: ${({ theme }) => theme.primaryCard};
  
  -webkit-box-shadow: 5px 3px 50px 10px rgba(62, 62, 68, 1);
  -moz-box-shadow: 5px 3px 50px 10px rgba(62, 62, 68, 1);
  box-shadow: 5px 3px 50px 10px rgba(62, 62, 68, 1);
`;