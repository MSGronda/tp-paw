import axios from 'axios'

let apiUrl = "http://localhost:8080/paw-2023a-06/api";

if(import.meta.env.VITE_UNI_ENV === 'prod')
    apiUrl = "http://old-pawserver.it.itba.edu.ar/paw-2023a-06/api";

export const API_URL = apiUrl;

const axiosInstance = axios.create({
    baseURL: API_URL,
    timeout: 30000,
});

export default axiosInstance
