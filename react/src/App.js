import React from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import './App.css';
import Login from './login/Login';
import useToken from './token/useToken';

const App = () => {

  const { token, setToken, cleanToken } = useToken();

  if (!token) {
    return < Login setToken={setToken} />;
  }

  return (
    <div className="wrapper">
      <h1>Application</h1>
      <a href="/test1">
        <button>test1</button>
      </a>
      <a href="/test2">
        <button>test2</button>
      </a>
      <BrowserRouter>
        <Switch>
          <Route path="/test1">
            <div>test 1</div>
          </Route>
          <Route path="/test2">
            <div>test 2</div>
          </Route>
        </Switch>
      </BrowserRouter>
      <a href="/">
        <button onClick={() => cleanToken()}>Logout</button>
      </a>
    </div>
  );
}

export default App;
