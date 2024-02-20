import { MantineProvider } from '@mantine/core';
import {
  BrowserRouter as Router,
  Route,
  Routes,
} from "react-router-dom";

// core styles are required for all packages
import '@mantine/core/styles.css';
import { useTranslation } from 'react-i18next';
import { useEffect } from 'react';
import { AuthContextProvider } from './context/AuthContext';
import './App.css'
import { HomeScreen } from './pages/HomeScreen/Home';
import { AnonymousRoute } from './AnonymousRoute';
import Register from './pages/Register/register';
import ConfirmEmail from './pages/ConfirmEmail/confirmEmail';
import Login from './pages/Login/login';
import { PrivateRoute } from './PrivateRoute';
import Search from './pages/Search/search';
import Profile from './pages/Profile/profile';
import SemesterBuilder from './pages/SemesterBuilder/semester_builder';
import FinishSemester from './pages/FinishSemester/finish_semester';
import { SubjectPage } from './pages/Subject/Subject';
import MultiReview from "./pages/MultiReview/multi-review.tsx";
import ReviewSubject from "./pages/ReviewSubject/review";

export default function App() {

  const { i18n } = useTranslation();
  useEffect(() => {
    const lng = navigator.language;
    i18n.changeLanguage(lng);
  }, []);
  
  return (
    <MantineProvider>
      <AuthContextProvider>
      {/* <RouterProvider router={router}/> */}
      <Router basename="/paw-2023a-06">
        <Routes>
          <Route path="/" element={<HomeScreen/>}/>
          <Route path="register" element={<AnonymousRoute component={Register}/>}/>
          <Route path="confirm" element={<AnonymousRoute component={ConfirmEmail}/>}/>
          <Route path="login" element={<AnonymousRoute component={Login}/>}/>
          <Route path="search" element={<PrivateRoute component={Search} roles={['ADMIN', 'USER']}/>}/>
          <Route path="profile" element={<PrivateRoute component={Profile} roles={['ADMIN', 'USER']}/>}/>
          <Route path="builder" element={<PrivateRoute component={SemesterBuilder} roles={['ADMIN', 'USER']}/>}/>
          <Route path="builder/finish" element={<PrivateRoute component={FinishSemester} roles={['ADMIN', 'USER']}/>}/>
          <Route path="subject/:id" element={<PrivateRoute component={SubjectPage} roles={['ADMIN', 'USER']}/>}/>
          <Route path="review/:id" element={<PrivateRoute component={ReviewSubject} roles={['ADMIN', 'USER']}/>}/>
          <Route path="multi-review" element={<PrivateRoute component={MultiReview} roles={['ADMIN', 'USER']}/>}/>
        </Routes>
      </Router>
      </AuthContextProvider>
    </MantineProvider>
  );
}
