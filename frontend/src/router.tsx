import { createBrowserRouter } from 'react-router-dom';
import Home from './pages/HomeScreen/Home';
import Landing from './pages/Landing/landing';

const router = createBrowserRouter([
    {
        path: '/',
        element: <Landing/>
    },
    {
        path: '/home',
        element: <Home/>
    }
]);

export default router;
