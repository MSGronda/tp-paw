import { MantineProvider } from '@mantine/core';
import { RouterProvider } from 'react-router-dom';
import router from './router';
// core styles are required for all packages
import '@mantine/core/styles.css';

export default function App() {
  return (
    <MantineProvider>
      <RouterProvider router={router}/>
    </MantineProvider>
  );
}
