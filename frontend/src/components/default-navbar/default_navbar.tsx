import { Container, Group, Burger } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import classes from './default_navbar.module.css';
import UniLogo from '../../images/uni.png'


export function Default_Navbar() {
  const [opened, { toggle }] = useDisclosure(false);


  return (
    <header className={classes.header}>
      <Container size="xl" className={classes.inner}>
        <img src={UniLogo} alt="UnimartLogo" className={classes.resize_image}/>
        <Burger opened={opened} onClick={toggle} hiddenFrom="xs" size="sm" />
      </Container>
    </header>
  );
}