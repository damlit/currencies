import React, { useState, useRef, useEffect } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';
import { useDispatch } from 'react-redux'
import Login from '../Login/Login';
import useToken from '../token/useToken';

import FocusLock from 'react-focus-lock';
import { Burger, Menu, useOnClickOutside } from '../menu';

import Quotes from '../Quotes';
import Profit from '../Profit';
import Deposits from '../Deposits';
import { fetchUserRole } from '../redux/actions/user.action';

const Application = () => {
    const [open, setOpen] = useState(false);
    const node = useRef();
    useOnClickOutside(node, () => setOpen(false));
    const menuId = "main-menu";

    const { token, setToken, cleanToken } = useToken();
    const dispatch = useDispatch();

    useEffect(() => {
        if (token) {
            dispatch(fetchUserRole(dispatch));
        }
    }, [token, dispatch]);

    if (!token) {
        return <Login setToken={setToken}/>;
    }

    return (
        <div>
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
                        <Deposits/>
                    </Route>
                    <Route path="/profit">
                        <Profit/>
                    </Route>
                    <Route path="/quotes">
                        <Quotes/>
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    );
}

export default Application;

// todo
// 4. CSS in EVERYWHERE
// 6. refreshing token
// 7. possibilites to choose quotes list date
// 8. n+1 problem (BE)
// 9. redux (clear after logout)
// 10. add role to redux
// 11. hasRole in update BE
// 