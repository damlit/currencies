import React, { useState, useRef } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import Login from '../Login/Login';
import useToken from '../token/useToken';

import FocusLock from 'react-focus-lock';
import { Burger, Menu, useOnClickOutside } from '../menu';

import Quotes from '../Quotes';
import Profit from '../Profit';
import Deposits from '../Deposits';
import Axios from 'axios';

const Application = () => {
    Axios.defaults.headers.common.Authorization = `Bearer ${localStorage.getItem('access_token')}`;

    const [open, setOpen] = useState(false);
    const node = useRef();
    useOnClickOutside(node, () => setOpen(false));
    const menuId = "main-menu";

    const { token, setToken, cleanToken } = useToken();

    if (!token) {
        return <Login setToken={setToken}/>;
    }

    return (
        <BrowserRouter>
            <div ref={node}>
                <FocusLock disabled={!open}>
                    <Burger open={open} setOpen={setOpen} aria-controls={menuId}/>
                    <Menu open={open} setOpen={setOpen} id={menuId} logout={cleanToken}/>
                </FocusLock>
            </div>
            <div>
                <Switch>
                    <Route path="/deposits">
                        <Deposits/>
                    </Route>
                    <Route path="/profit">
                        <Profit/>
                    </Route>
                    <Route path="/quotes">
                        <Quotes/>
                    </Route>
                </Switch>
            </div>
        </BrowserRouter>
    );
}

export default Application;

// todo
// 18. send token confirmation link on email
// 19. remove errors on FE
// 21. add sell button and summary of profits in profit section
// 23. add sell button with dialog, and profits from sales
// 24. add dialogs?
