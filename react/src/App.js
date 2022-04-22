import React from 'react';
import { Provider } from 'react-redux'
import store from './store';

import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalBody, GlobalStyles, Logo, LogoCointainer, LogoText } from './global.styled';
import Application from './Application';

import { I18nextProvider } from "react-i18next";
import i18next from './i18n';

const App = () => {

  return (
    <I18nextProvider i18n={i18next}>
      <ThemeProvider theme={theme}>
        <Provider store={store}>
          <GlobalStyles />
          <Logo>
            <LogoCointainer>
              <LogoText>Currency</LogoText>
            </LogoCointainer>
          </Logo>
          <GlobalBody>
            <Application />
          </GlobalBody>
        </Provider>
      </ThemeProvider>
    </I18nextProvider>
  );
}

export default App;