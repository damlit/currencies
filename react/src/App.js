import React, { Component } from 'react';
import logo from './logo.svg';
import './App.css';
import { getAuthToken } from './request/login.request';
import { getLastCurrencies } from './request/currencies.request';

class App extends Component {
  state = {
    isLoading: true,
    lastCurrencies: []
  };

  async componentDidMount() {
    const token = await getAuthToken("damian@test.pl", "test123");
    const currencies = await getLastCurrencies(token);
    this.setState({lastCurrencies: currencies, isLoading: false});
  };

  render() {
    const { lastCurrencies, isLoading } = this.state;

    if (isLoading) {
      return <p>Loading...</p>;
    }

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <div className="App-link">
            <h2>test</h2>
            <div key={lastCurrencies.id}>
              {lastCurrencies.quotes.map(quote =>
                <div key={quote.id}>
                  {quote.currency} has quote {quote.quote}
                </div>
              )}
            </div>
          </div>
        </header>
      </div>
    );
  }
}

export default App;
