import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { getAuthToken, getRegistrationToken, sendConfirmationToken } from '../request/login.request';
import { LoginText, LoginWrapper } from './Login.styled.js';
import { BlueButton, ButtonToggle, ButtonGroup } from "../SmallComponents/BlueButton.styled";
import { makeFunctionIfFieldsHasBeenFilled } from '../utils/validationFunctions.utils';
import { useTranslation } from "react-i18next";
import { Card } from '../SmallComponents/Card.styled';

const Login = ({ setToken }) => {
  const { t, i18n } = useTranslation('common');

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [signUp, setSignUp] = useState(false);
  const [signUpToken, setSignUpToken] = useState('');
  const [language, setLanguage] = useState('en');

  const handleSubmitTest = async e => {
    if (signUp) {
      makeFunctionIfFieldsHasBeenFilled(submitSignUp, e, [email, password]);
    } else {
      makeFunctionIfFieldsHasBeenFilled(submitLogIn, e, [email, password]);
    }
  }

  const submitLogIn = async e => {
    e.preventDefault();
    const { status, token } = await getAuthToken(email, password);

    if (status === 200 || status === 201) {
      setToken(token);
    } else {
      setToken(null);
      alert('Something went wrong. Server response with code ' + status + ' (' + token + ').');
    }
  }

  const submitSignUp = async e => {
    e.preventDefault();
    const { status, confirmationToken } = await getRegistrationToken(email, password);

    if (status === 201) {
      alert('Your confirmation token: ' + confirmationToken);
    } else {
      alert('Something went wrong. Server response with code ' + status + ' (' + confirmationToken + ').');
    }
  }

  const handleConfirmationToken = async e => {
    makeFunctionIfFieldsHasBeenFilled(confirmSignUpToken, e, [signUpToken]);
  }

  const confirmSignUpToken = async e => {
    e.preventDefault();
    const { status, textResponse } = await sendConfirmationToken(signUpToken);

    alert(textResponse);
    if (status === 200) {
      document.getElementById("userForm").reset();
      document.getElementById("confirmationForm").reset();
      setSignUp(false);
    }
  }

  const handleChangeLanguage = async (e) => {
    e.preventDefault();
    setLanguage(e.target.value);
    i18n.changeLanguage(e.target.value);
  }

  return (
    <LoginWrapper>
      <Card>
        {t('login.changeLng')}
        <ButtonGroup>
          <ButtonToggle value='en' active={language === 'en'} onClick={(e) => handleChangeLanguage(e)}>
            EN
          </ButtonToggle>
          <ButtonToggle value='pl' active={language === 'pl'} onClick={(e) => handleChangeLanguage(e)}>
            PL
          </ButtonToggle>
        </ButtonGroup>
      </Card>
      <Card>
        {signUp
          ? <h1>{t('login.signUp')}</h1>
          : <h1>{t('login.login')}</h1>
        }
        <ButtonGroup>
          <ButtonToggle active={!signUp} onClick={() => setSignUp(false)}>
            {t('login.login')}
          </ButtonToggle>
          <ButtonToggle active={signUp} onClick={() => setSignUp(true)}>
            {t('login.signUp')}
          </ButtonToggle>
        </ButtonGroup>
        <form onSubmit={handleSubmitTest} id="userForm">
          <label>
            <LoginText>{t('login.email')}</LoginText>
            <input type="text" onChange={e => setEmail(e.target.value)} />
          </label>
          <label>
            <LoginText>{t('login.password')}</LoginText>
            <input type="password" onChange={e => setPassword(e.target.value)} />
          </label>
          <div>
            <BlueButton type="submit">{t('login.submit')}</BlueButton>
          </div>
        </form>
        {signUp &&
          <form onSubmit={handleConfirmationToken} id="confirmationForm">
            <label>
              <LoginText>{t('login.confirmationToken')}</LoginText>
              <input type="text" onChange={e => setSignUpToken(e.target.value)} />
            </label>
            <div>
              <BlueButton type="confirm">{t('login.confirm')}</BlueButton>
            </div>
          </form>
        }
      </Card >
    </LoginWrapper>
  )
}

Login.propTypes = {
  setToken: PropTypes.func.isRequired
};

export default Login;