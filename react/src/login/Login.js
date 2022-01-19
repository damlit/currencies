import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { getAuthToken, getRegistrationToken, sendConfirmationToken } from '../request/login.request';
import { LoginWrapper } from './Login.styled.js';
import { BlueButton, ButtonToggle } from "../Buttons/BlueButton.styled";
import { ButtonGroup } from 'reactstrap';

const Login = ({ setToken }) => {

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [signUp, setSignUp] = useState(false);
  const [signUpToken, setSignUpToken] = useState('');

  const handleSubmit = async e => {
    e.preventDefault();
    const { status, token } = await getAuthToken(email, password);

    if (status === 200 || status === 201) {
      setToken(token);
    } else {
      setToken(null);
      alert('Something went wrong. Server response with code ' + status + ' (' + token + ').');
    }
  }

  const handleSubmitSignUp = async e => {
    e.preventDefault();
    const { status, confirmationToken } = await getRegistrationToken(email, password);

    if (status === 201) {
      alert('Your confirmation token: ' + confirmationToken);
    } else {
      alert('Something went wrong. Server response with code ' + status + ' (' + confirmationToken + ').');
    }
  }

  const handleConfirmationToken = async e => {
    e.preventDefault();
    const { status, textResponse } = await sendConfirmationToken(signUpToken);

    alert(textResponse);
    if (status === 200) {
      document.getElementById("userForm").reset();
      document.getElementById("confirmationForm").reset();
      setSignUp(false);
    }
  }

  return (
    <LoginWrapper>
      <ButtonGroup>
        <ButtonToggle active={!signUp} onClick={() => setSignUp(false)}>
          login
        </ButtonToggle>
        <ButtonToggle active={signUp} onClick={() => setSignUp(true)}>
          sign up
        </ButtonToggle>
      </ButtonGroup>
      {signUp
        ? <h1>Please Sign Up</h1>
        : <h1>Please Log In</h1>
      }
      <form onSubmit={signUp ? handleSubmitSignUp : handleSubmit} id="userForm">
        <label>
          <p>Email</p>
          <input type="text" onChange={e => setEmail(e.target.value)} />
        </label>
        <label>
          <p>Password</p>
          <input type="password" onChange={e => setPassword(e.target.value)} />
        </label>
        <div>
          <BlueButton type="submit">Submit</BlueButton>
        </div>
      </form>
      {signUp &&
        <form onSubmit={handleConfirmationToken} id="confirmationForm">
          <label>
            <p>Confirmation token</p>
            <input type="text" onChange={e => setSignUpToken(e.target.value)} />
          </label>
          <div>
            <BlueButton type="confirm">Confirm</BlueButton>
          </div>
        </form>
      }
    </LoginWrapper>
  )
}

Login.propTypes = {
  setToken: PropTypes.func.isRequired
};

export default Login;