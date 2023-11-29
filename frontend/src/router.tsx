import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/HomeScreen/Home';
import Landing from './pages/Landing/landing';
import Register from './pages/Register/register';
import Login from "./pages/Login/login.tsx";
import Degree from './pages/Degree/Degree.tsx';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Landing/>
    },
    {
        path: '/home',
        element: <Home/>
    },
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
    }
]);

export default router;
