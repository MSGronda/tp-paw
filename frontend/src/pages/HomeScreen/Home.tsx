import { Text } from '@mantine/core';
import {Navbar } from "../../components/navbar/navbar";
import classes from './home.module.css';


export default function Home() {
    return (
        <div className={classes.fullsize}>
        <Navbar/>
        <Text>Welcome to Mantine!</Text>
        </div>
    );
}
