import { Text } from '@mantine/core';
import {Navbar } from "../../components/default-navbar/default_navbar";
import classes from './home.module.css';


export default function Home() {
    return (
        <div className={classes.fullsize}>
        <Navbar/>
        <Text>Welcome to Mantine!</Text>
        </div>
    );
}
