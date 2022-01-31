import { createGlobalStyle } from 'styled-components';
import styled from 'styled-components';

export const GlobalStyles = createGlobalStyle`

  body {
    background: ${({ theme }) => theme.primaryDark};
  }

  div {
    text-align: center;
  }
`

export const GlobalBody = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background: ${({ theme }) => theme.primaryDark};
  color: ${({ theme }) => theme.primaryLight};
  height: 100%;
  width: 100vw;
  text-rendering: optimizeLegibility;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
`

export const Logo = styled.div`
  display: flex;
  background: ${({ theme }) => theme.primaryCard};
  color: ${({ theme }) => theme.primaryLight};
  height: 4rem;
  text-rendering: optimizeLegibility;
  font-family: -apple-system, BlinkMacSystemFont, Papyrus, fantasy; 
`

export const LogoCointainer = styled.div`
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
`;

export const LogoText = styled.h1`
  display: flex;
  padding: 1rem;
  font-size: 2rem;
  font-weight: 600;
  text-align: center;
  text-transform: uppercase;
`
