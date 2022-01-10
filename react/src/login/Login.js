import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { getAuthToken, getRegistrationToken, sendConfirmationToken } from '../request/login.request';
import { LoginWrapper } from './Login.styled.js';

const Login = ({ setToken }) => {
  const [email, setEmail] = useState();
  const [password, setPassword] = useState();
  const [signUp, setSignUp] = useState();
  const [signUpToken, setSignUpToken] = useState();

  const handleSubmit = async e => {
    e.preventDefault();
    const token = await getAuthToken(email, password);
    setToken(token);
  }

  const handleSubmitSignUp = async e => {
    e.preventDefault();
    const confirmationToken = await getRegistrationToken(email, password);
    alert('Your confirmation token: ' + confirmationToken);
    document.getElementById("userForm").reset();
  }

  const handleConfirmationToken = async e => {
    e.preventDefault();
    const response = await sendConfirmationToken(signUpToken);
    alert(response);
    document.getElementById("userForm").reset();
    document.getElementById("confirmationForm").reset();
    setSignUp(false);
  }

  return (
    <LoginWrapper>
      <button onClick={() => setSignUp(false)}>login</button>
      <button onClick={() => setSignUp(true)}>sign up</button>
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
          <button type="submit">Submit</button>
        </div>
      </form>
      {signUp &&
        <form onSubmit={handleConfirmationToken} id="confirmationForm">
          <label>
            <p>Confirmation token</p>
            <input type="text" onChange={e => setSignUpToken(e.target.value)} />
          </label>
          <div>
            <button type="confirm">Confirm</button>
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