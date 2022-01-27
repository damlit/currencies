import React from 'react';
import { Provider } from 'react-redux'
import store from './redux/store';

import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalBody, GlobalStyles, Logo, LogoText } from './global.styled';
import Application from './Application';

const App = () => {

  return (
    <ThemeProvider theme={theme}>
      <Provider store={store}>
        <GlobalStyles />
        <Logo>
          <LogoText>Currency</LogoText>
        </Logo>
        <GlobalBody>
          <Application />
        </GlobalBody>
      </Provider>
    </ThemeProvider>
  );
}

export default App;