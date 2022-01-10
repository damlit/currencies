import React, { useEffect, useState, useRef } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Login from './Login/Login';
import { getLastCurrencies } from './request/currencies.request';
import useToken from './token/useToken';

import FocusLock from 'react-focus-lock';
import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalStyles } from './global.styled';
import { Burger, Menu, useOnClickOutside } from './menu';

const App = () => {

  const [open, setOpen] = useState(false);
  const node = useRef();
  useOnClickOutside(node, () => setOpen(false));
  const menuId = "main-menu";

  const { token, setToken, cleanToken } = useToken();
  const [lastCurrencies, setLastCurrencies] = useState();

  useEffect(() => {
    getLastCurrencies(token, setLastCurrencies)
  }, [token]);

  if (!token) {
    return <ThemeProvider theme={theme}>
      <GlobalStyles/>
      < Login setToken={setToken} />
      </ThemeProvider>;
  }

  return (
    <ThemeProvider theme={theme}>
      <>
        <GlobalStyles/>
        <div ref={node}>
          <FocusLock disabled={!open}>
            <Burger open={open} setOpen={setOpen} aria-controls={menuId} />
            <Menu open={open} setOpen={setOpen} id={menuId} logout={cleanToken} />
          </FocusLock>
        </div>
      <h1>Application</h1>
      <BrowserRouter>
        <Switch>
          <Route path="/deposits">
            <div>Deposits</div>
          </Route>
          <Route path="/profit">
            <div>Profit</div>
          </Route>
          <Route path="/quotes">
            <div>Quotes</div>
            <div key={"test2"}>
              {lastCurrencies
                ? lastCurrencies.quotes.map(quote =>
                  <div key={quote.id}>
                    {quote.currency} has quote {quote.quote}
                  </div>
                )
                : ""}
            </div>
          </Route>
        </Switch>
      </BrowserRouter>
      </>
    </ThemeProvider>
  );
}

export default App;
