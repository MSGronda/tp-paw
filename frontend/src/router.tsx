import { createBrowserRouter } from 'react-router-dom';
import { HomeScreen } from './pages/HomeScreen/Home';
import Register from './pages/Register/register';
import Login from "./pages/Login/login.tsx";
import Degree from './pages/Degree/Degree.tsx';
import Test from './pages/Test/test.tsx';


const router = createBrowserRouter([
    {
        path: '/',
        element: <HomeScreen/>
    },
    // {
    //     path: '/home',
    //     element: <Home/>
    // },
    {
        path: '/register',
        element: <Register/>
    },
    {
        path: '/login',
        element: <Login/>
    },
    {

        path: '/degree/:id',
        element: <Degree/>
    },
    {
        path: '/test',
        element: <Test/>
    }
]);

export default router;
