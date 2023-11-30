import { Autocomplete, Group, Burger, rem } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { IconSearch } from '@tabler/icons-react';
import classes from './navbar.module.css';
import UniLogo from '../../images/uni.png'
import { useTranslation } from "react-i18next";
import { useNavigate } from 'react-router-dom';


export function Navbar() {
  const { t } = useTranslation();

  const links = [
    { link: '/', label: t('Navbar.home') },
    { link: '/curriculum', label: t('Navbar.curriculum') },
    { link: '/semesterbuilder', label: t('Navbar.semesterbuilder') },
    { link: '/profile', label: t('Navbar.profile') },
  ];

  const [opened, { toggle }] = useDisclosure(false);
  const navigate = useNavigate();

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
          <Burger opened={opened} onClick={toggle} size="sm" hiddenFrom="sm" />
          <img src={UniLogo} alt="UnimartLogo" className={classes.resize_image}/>
        </Group>

        <Group>
        <Autocomplete
            className={classes.search}
            placeholder="Search"
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