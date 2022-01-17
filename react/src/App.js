import React from 'react';
import { Provider } from 'react-redux'
import store from './redux/store';

import { ThemeProvider } from 'styled-components';
import { theme } from './theme.styles';
import { GlobalStyles } from './global.styled';
import Application from './Application';

const App = () => {

  return (
    <ThemeProvider theme={theme}>
      <Provider store={store}>
        <GlobalStyles />
        <Application/>
      </Provider>
    </ThemeProvider>
  );
}

export default App;

// todo
// 4. CSS in EVERYWHERE
// 5. pages in deposits
// 6. refreshing token
// 7. possibilites to choose quotes list date
// 8. n+1 problem (BE)
// 9. redux (clear after logout)
// 10. add role to redux