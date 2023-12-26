import { Container, Burger } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import classes from './default_navbar.module.css';
import UniLogo from '../../images/uni.png'
import { useNavigate } from 'react-router-dom';


export function Default_Navbar() {
  const [opened, { toggle }] = useDisclosure(false);
  const navigate = useNavigate();

  const handleLogoClick = () => {
    navigate('/');
  }


  return (
    <header className={classes.header}>
      <Container size="xl" className={classes.inner}>
        <span onClick={handleLogoClick} className={classes.image}>
          <img src={UniLogo} alt="UnimartLogo" className={classes.resize_image}/>
        </span>
        <Burger opened={opened} onClick={toggle} hiddenFrom="xs" size="sm" />
      </Container>
    </header>
  );
}