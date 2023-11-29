import axios from 'axios'

export default axios.create({
    baseURL: "http://localhost:8080/paw-2023a-06",
    timeout: 30000,
});