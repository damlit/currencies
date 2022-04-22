import React from 'react';
import { bool, func } from 'prop-types';
import { StyledMenu } from './Menu.styled';
import { updateCurrencies } from '../../request/currencies.request';
import { useSelector } from 'react-redux';
import { useTranslation } from "react-i18next";
import { Link } from 'react-router-dom';

const Menu = ({ open, logout }) => {
  const { t } = useTranslation('common');

  const role = useSelector((state) => state.auth?.user?.userRole);

  const isHidden = !!open;
  const tabIndex = isHidden ? 0 : -1;

  const isAdmin = () => 'ADMIN' === role;

  return (
    <StyledMenu open={open} aria-hidden={!isHidden}>
      <Link to="/deposits" tabIndex={tabIndex}>
        <span aria-hidden="true">💁🏻‍♂️</span>
        {t('menu.deposits')}
      </Link>
      <Link to="/profit" tabIndex={tabIndex}>
        <span aria-hidden="true">💸</span>
        {t('menu.profit')}
      </Link>
      <Link to="/quotes" tabIndex={tabIndex}>
        <span aria-hidden="true">📈</span>
        {t('menu.quotes')}
      </Link>
      <a href="/" tabIndex={tabIndex} onClick={() => logout()}>
        <span aria-hidden="true">👋</span>
        {t('menu.logout')}
      </a>
      {isAdmin() &&
        <Link to="/quotes" tabIndex={tabIndex} onClick={async () => await updateCurrencies()} role='button'>
          <span aria-hidden="true">🔂</span>
          {t('menu.update')}
        </Link>}
    </StyledMenu>
  )
}

Menu.propTypes = {
  open: bool.isRequired,
  logout: func.isRequired,
}

export default Menu;