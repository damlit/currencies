import React from 'react';
import { bool, func } from 'prop-types';
import { StyledMenu } from './Menu.styled';

const Menu = ({ open, logout, ...props }) => {
  
  const isHidden = open ? true : false;
  const tabIndex = isHidden ? 0 : -1;

  return (
    <StyledMenu open={open} aria-hidden={!isHidden} {...props}>
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
    </StyledMenu>
  )
}

Menu.propTypes = {
  open: bool.isRequired,
  logout: func.isRequired,
}

export default Menu;