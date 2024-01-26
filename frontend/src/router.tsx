import { createBrowserRouter } from 'react-router-dom';
import Home, { HomeScreen } from './pages/HomeScreen/Home';
import Register from './pages/Register/register';
import Login from "./pages/Login/login.tsx";
import Degree from './pages/Degree/Degree.tsx';
import Test from './pages/Test/test.tsx';
import { AnonymousRoute } from './AnonymousRoute.tsx';
import { PrivateRoute } from './PrivateRoute.tsx';
import Profile from './pages/Profile/profile.tsx';
import Search from './pages/Search/search.tsx';
import SemesterBuilder from "./pages/SemesterBuilder/semester_builder.tsx";
import {SubjectInfo, SubjectScreen} from "./pages/Subject/Subject.tsx";



const router = createBrowserRouter([
    {
        path: '/',
        element: <HomeScreen/>
    },
    {
         path: '/home',
         element: <Home/>
    },
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
        path: '/search',
        element: <PrivateRoute component={Search} roles={['ADMIN', 'USER']}/>,
    },
    {
        path: '/test',
        element: <Test/>
    },
    {
        path: '/profile',
        element: <Profile/>
    },
    {
        path: '/builder',
        element: <SemesterBuilder/>
    },
    {
        path: '/subject/:id',
        element: <SubjectScreen/>
    }
]);

export default router;
