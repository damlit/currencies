import React, { useState, useRef, useEffect } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Login from './Login/Login';
import useToken from './token/useToken';

import FocusLock from 'react-focus-lock';
import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalStyles } from './global.styled';
import { Burger, Menu, useOnClickOutside } from './menu';

import Quotes from './Quotes';
import Profit from './Profit';
import Deposits from './Deposits';
import { isAdminUserRole } from './request/login.request';

const App = () => {

  const [open, setOpen] = useState(false);
  const node = useRef();
  useOnClickOutside(node, () => setOpen(false));
  const menuId = "main-menu";

  const { token, setToken, cleanToken } = useToken();
  const [isAdmin, setIsAdmin] = useState();

  useEffect(() => {
    if (token) {
      isAdminUserRole(token, setIsAdmin);
    }
  }, [token]);

  if (!token) {
    return <ThemeProvider theme={theme}>
      <GlobalStyles />
      <Login setToken={setToken} setIsAdmin={setIsAdmin} />
    </ThemeProvider>;
  }

  return (
    <ThemeProvider theme={theme}>
      <>
        <GlobalStyles />
        <div ref={node}>
          <FocusLock disabled={!open}>
            <Burger open={open} setOpen={setOpen} aria-controls={menuId} />
            <Menu open={open} setOpen={setOpen} id={menuId} logout={cleanToken} isAdmin={isAdmin} token={token} />
          </FocusLock>
        </div>
        <h1>Application</h1>
        <BrowserRouter>
          <Switch>
            <Route path="/deposits">
              {token
                ? <Deposits token={token} />
                : <></>
              }
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

// todo
// 3. Deposits - why alert occurs sometimes/sometimes aint
// 4. CSS in EVERYWHERE
// 5. pages in deposits
// 6. refreshing token
// 7. possibilites to choose quotes list date
// 8. n+1 problem (BE)
// 9. redux (clear after logout)
// 10. add role to redux