import {Group, rem, Input, Button, Menu, MenuItem} from '@mantine/core';
import {IconChevronDown, IconSearch} from '@tabler/icons-react';
import classes from './navbar.module.css';
import UniLogo from '../../images/uni.png'
import { useTranslation } from "react-i18next";
import { useNavigate } from 'react-router-dom';
import React, {useContext} from 'react';
import AuthContext from '../../context/AuthContext';


export function Navbar() {
  const { t } = useTranslation();
  const { role } = useContext(AuthContext);


  const links = [
    { link: '/', label: t('Navbar.home') },
    { link: '/curriculum', label: t('Navbar.curriculum') },
    { link: '/builder', label: t('Navbar.semesterbuilder') },
  ];
  const editorLinks = [
    { link: '/degrees', label: t('Navbar.degrees') },
    { link: '/create-subject', label: t('Navbar.createSubject') },
    { link: '/create-degree', label: t('Navbar.createDegree') },
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
            {role === 'EDITOR' &&
                <Menu
                    transitionProps={{ transition: 'pop-top-right' }}
                    position="top-end"
                    width={220}
                    withinPortal
                >
                  <Menu.Target>
                    <Button
                        rightSection={
                          <IconChevronDown style={{ width: rem(18), height: rem(18) }} stroke={1.5} />
                        }
                        pr={12}
                    >
                      {t('Navbar.editorTools')}
                    </Button>
                  </Menu.Target>
                  <Menu.Dropdown>
                    {editorLinks.map((link, index) => (
                        <MenuItem
                            key={index}
                            onClick={() => {
                              navigate(link.link);
                            }}
                        >
                          {link.label}
                        </MenuItem>
                    ))}
                  </Menu.Dropdown>
                </Menu>

            }
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