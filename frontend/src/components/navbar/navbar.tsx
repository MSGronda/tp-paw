import { Autocomplete, Group, Burger, rem, Input } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconSearch } from '@tabler/icons-react';
import classes from './navbar.module.css';
import UniLogo from '../../images/uni.png'
import { useTranslation } from "react-i18next";
import { useNavigate } from 'react-router-dom';
import React from 'react';


export function Navbar() {
  const { t } = useTranslation();

  const links = [
    { link: '/', label: t('Navbar.home') },
    { link: '/curriculum', label: t('Navbar.curriculum') },
    { link: '/semesterbuilder', label: t('Navbar.semesterbuilder') },
    { link: '/profile', label: t('Navbar.profile') },
  ];

  const navigate = useNavigate();
  const [searchValue, setSearchValue] = React.useState('');

  const handleSearchChange = (value: string) => {
    setSearchValue(value);
  };

  const handleSearchSubmit = (event: { key: string; }) => {
    if ( event.key === 'Enter'){
      navigate(`/search?q=${searchValue}`);
    }
  };

  const handleLogoClick = () => {
    navigate('/');
  }

  const items = links.map((link, index) => (
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
        </Group>
      </div>
    </header>
  );
}