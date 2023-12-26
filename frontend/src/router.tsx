import { createBrowserRouter } from 'react-router-dom';
import { HomeScreen } from './pages/HomeScreen/Home';
import Register from './pages/Register/register';
import Login from "./pages/Login/login.tsx";
import Degree from './pages/Degree/Degree.tsx';
import Test from './pages/Test/test.tsx';
<<<<<<< Updated upstream
import { AnonymousRoute } from './AnonymousRoute.tsx';
import { PrivateRoute } from './PrivateRoute.tsx';
=======
import Profile from './pages/Profile/profile.tsx';
>>>>>>> Stashed changes


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
        element: <AnonymousRoute component={Register}/>
    },
    {
        path: '/login',
        element: <AnonymousRoute component={Login}/>
    },
    {

        path: '/degree/:id',
        element: <PrivateRoute component={Degree} roles={['ADMIN']}/>
    },
    {
        path: '/test',
        element: <Test/>
    },
    {
        path: '/profile',
        element: <Profile/>
    }
]);

export default router;
