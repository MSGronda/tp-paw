import { MantineProvider } from '@mantine/core';
import { RouterProvider } from 'react-router-dom';
import router from './router';
// core styles are required for all packages
import '@mantine/core/styles.css';
import { useTranslation } from 'react-i18next';
import { useEffect } from 'react';
import { AuthContextProvider } from './context/AuthContext';
import './App.css'

export default function App() {

  const { i18n } = useTranslation();
  useEffect(() => {
    const lng = navigator.language;
    i18n.changeLanguage(lng);
  }, []);
  
  return (
    <MantineProvider>
      <AuthContextProvider>
      <RouterProvider router={router}/>
      </AuthContextProvider>
    </MantineProvider>
  );
}
