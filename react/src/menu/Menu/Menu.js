import React from 'react';
import { bool, func } from 'prop-types';
import { StyledMenu } from './Menu.styled';
import { updateCurrencies } from '../../request/currencies.request';
import { useSelector } from 'react-redux';
import { useTranslation } from "react-i18next";

const Menu = ({ open, logout }) => {
  const { t } = useTranslation('common');

  const role = useSelector((state) => state.user.role);
  const isHidden = open ? true : false;
  const tabIndex = isHidden ? 0 : -1;

  const isAdmin = () => '"ADMIN"' === role;

  return (
    <StyledMenu open={open} aria-hidden={!isHidden}>
      <a href="/deposits" tabIndex={tabIndex}>
        <span aria-hidden="true">💁🏻‍♂️</span>
        {t('menu.deposits')}
      </a>
      <a href="/profit" tabIndex={tabIndex}>
        <span aria-hidden="true">💸</span>
        {t('menu.profit')}
      </a>
      <a href="/quotes" tabIndex={tabIndex}>
        <span aria-hidden="true">📈</span>
        {t('menu.quotes')}
      </a>
      <a href="/" tabIndex={tabIndex} onClick={() => logout()}>
        <span aria-hidden="true">👋</span>
        {t('menu.logout')}
      </a>
      {isAdmin() &&
        <a href="/" tabIndex={tabIndex} onClick={() => updateCurrencies()} role='button'>
          <span aria-hidden="true">🔂</span>
        {t('menu.update')}
        </a>}
    </StyledMenu>
  )
}

Menu.propTypes = {
  open: bool.isRequired,
  logout: func.isRequired,
}

export default Menu;