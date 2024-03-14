import {  Group, rem, Input, Button } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import classes from './navbar.module.css';
import UniLogo from '../../images/uni.png'
import { useTranslation } from "react-i18next";
import { useNavigate } from 'react-router-dom';
import React, { useContext } from 'react';
import AuthContext from '../../context/AuthContext';


export function Navbar() {
  const { t } = useTranslation();
  const { role } = useContext(AuthContext);

  const links = [
    { link: '/', label: t('Navbar.home') },
    { link: '/curriculum', label: t('Navbar.curriculum') },
    { link: '/builder', label: t('Navbar.semesterbuilder') },
    { link: '/degrees', label: t('Navbar.degrees') },
    { link: '/create-subject', label: t('Navbar.createSubject') },
  ];

  const navigate = useNavigate();
  const [searchValue, setSearchValue] = React.useState('');

  const handleSearchChange = (value: string) => {
    setSearchValue(value);
  };

  const handleSearchSubmit = (event: { key: string; }) => {
    if ( event.key === 'Enter'){
      navigate(`/search?q=${searchValue}&page=1`);
    }
  };

  const handleLogoClick = () => {
    navigate('/');
  }

  // Filter links based on role
  const filteredLinks = role === 'EDITOR' ? links : links.slice(0, -2);

  const items = filteredLinks.map((link, index) => (
    <a
      key={index}
      href={link.link}
      className={classes.link}
      onClick={(event) => {
        event.preventDefault();
        navigate(link.link); // Navigate to the link when the button is clicked
      }}
    >
      {link.label}
    </a>
  ));

  return (
    <header className={classes.header}>
      <div className={classes.inner}>
        <Group>
          <span onClick={handleLogoClick} className={classes.image}>
            <img src={UniLogo} alt="UnimartLogo" className={classes.resize_image}/>
          </span>
        </Group>
        <Group>
        <Input
            className={classes.search}
            placeholder="Search"
            value={searchValue}
            onChange={(event) => handleSearchChange(event.currentTarget.value)}
            onKeyDown={handleSearchSubmit}
            leftSection={<IconSearch style={{ width: rem(16), height: rem(16) }} stroke={1.5} />}
            visibleFrom="xs"
          />
          <Group ml={50} gap={5} className={classes.links} visibleFrom="sm">
            {items}
          </Group>
          <Button
            onClick={(event) => {
              event.preventDefault();
              navigate('/profile');
            }}
          >
            {t('Navbar.profile')}
          </Button>
        </Group>
      </div>
    </header>
  );
}