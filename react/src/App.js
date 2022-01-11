import React, { useState, useRef } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Login from './Login/Login';
import useToken from './token/useToken';

import FocusLock from 'react-focus-lock';
import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalStyles } from './global.styled';
import { Burger, Menu, useOnClickOutside } from './menu';
import Quotes from './Quotes';
import Profit from './Profit/Profit';

const App = () => {

  const [open, setOpen] = useState(false);
  const node = useRef();
  useOnClickOutside(node, () => setOpen(false));
  const menuId = "main-menu";

  const { token, setToken, cleanToken } = useToken();

  if (!token) {
    return <ThemeProvider theme={theme}>
      <GlobalStyles />
      <Login setToken={setToken} />
    </ThemeProvider>;
  }

  return (
    <ThemeProvider theme={theme}>
      <>
        <GlobalStyles />
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
              {token
                ? <Profit token={token} />
                : <></>
              }
            </Route>
            <Route path="/quotes">
              {token
                ? <Quotes token={token} />
                : <></>
              }
            </Route>
          </Switch>
        </BrowserRouter>
      </>
    </ThemeProvider>
  );
}

export default App;
