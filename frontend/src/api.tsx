import axios from 'axios'

export const API_URL = "http://localhost:8080/paw-2023a-06"

const axiosInstance = axios.create({
    baseURL: API_URL,
    timeout: 30000,
});

export default axiosInstance
