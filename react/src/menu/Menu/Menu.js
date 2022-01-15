import React from 'react';
import { bool, func } from 'prop-types';
import { StyledMenu } from './Menu.styled';
import { updateCurrencies } from '../../request/currencies.request';

const Menu = ({ open, logout, isAdmin, token }) => {

  const isHidden = open ? true : false;
  const tabIndex = isHidden ? 0 : -1;

  return (
    <StyledMenu open={open} aria-hidden={!isHidden}>
      <a href="/deposits" tabIndex={tabIndex}>
        <span aria-hidden="true">💁🏻‍♂️</span>
        Deposits
      </a>
      <a href="/profit" tabIndex={tabIndex}>
        <span aria-hidden="true">💸</span>
        Profit
      </a>
      <a href="/quotes" tabIndex={tabIndex}>
        <span aria-hidden="true">📈</span>
        Quotes
      </a>
      <a href="/" tabIndex={tabIndex} onClick={() => logout()}>
        <span aria-hidden="true">👋</span>
        Logout
      </a>
      {isAdmin &&
        <a href="/" tabIndex={tabIndex} onClick={() => updateCurrencies(token)}>
          <span aria-hidden="true">🔂</span>
          Update
        </a>}
    </StyledMenu>
  )
}

Menu.propTypes = {
  open: bool.isRequired,
  logout: func.isRequired,
}

export default Menu;