import { createGlobalStyle } from 'styled-components';
import styled from 'styled-components';

export const GlobalStyles = createGlobalStyle`
  html, body {
    margin: 0;
    padding: 0;
  }

  *, *::after, *::before {
    box-sizing: border-box;
  }

  h1 {
    font-size: 2rem;
    text-align: center;
    text-transform: uppercase;
  }

  img {
    border-radius: 5px;
    height: auto;
    width: 10rem;
  }

  div {
    text-align: center;
  }

  small {
    display: block;
  }
  
  a {
    color: ${({ theme }) => theme.primaryHover};
    text-decoration: none;
  }
`

export const GlobalBody = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  background: ${({ theme }) => theme.primaryDark};
  color: ${({ theme }) => theme.primaryLight};
  height: 100vh;
  text-rendering: optimizeLegibility;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif, "Apple Color Emoji", "Segoe UI Emoji", "Segoe UI Symbol";
`

export const Logo = styled.div`
  background: ${({ theme }) => theme.primaryHover};
  color: ${({ theme }) => theme.primaryLight};
  height: 3rem;
  text-rendering: optimizeLegibility;
  font-family: -apple-system, BlinkMacSystemFont, Papyrus, fantasy; 
`

export const LogoText = styled.h1`
  padding: 0.5rem;
  font-size: 2rem;
  font-weight: 600;
  text-align: center;
  text-transform: uppercase;
`
