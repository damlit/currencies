import React from 'react';
import { Provider } from 'react-redux'
import store from './redux/store';

import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalBody, GlobalStyles, Logo, LogoCointainer, LogoText } from './global.styled';
import Application from './Application';

import { I18nextProvider } from "react-i18next";
import i18next from "i18next";
import common_en from './translations/en/common.json';
import common_pl from './translations/pl/common.json';

const App = () => {

  i18next.init({
    interpolation: { escapeValue: false },
    react: {
      useSuspense: false,
      wait: true,
    },
    fallbackLng: ['en', 'pl'],
    resources: {
      en: { common: common_en },
      pl: { common: common_pl }
    }
  });

  return (
    <ThemeProvider theme={theme}>
      <Provider store={store}>
        <GlobalStyles />
        <Logo>
          <LogoCointainer>
            <LogoText>Currency</LogoText>
          </LogoCointainer>
        </Logo>
        <GlobalBody>
          <I18nextProvider i18n={i18next}>
            <Application />
          </I18nextProvider>
        </GlobalBody>
      </Provider>
    </ThemeProvider>
  );
}

export default App;