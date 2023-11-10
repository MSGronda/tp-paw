import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/HomeScreen/Home';
import Landing from './pages/Landing/landing';
import Register from './pages/Register/register';

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
    }
]);

export default router;
