import axios from 'axios';

const API = axios.create({
    //baseURL: 'http://localhost:8080',
    baseURL: '/api', // Changed from 'http://localhost:8080'
});

API.interceptors.response.use(
    (response) => response,
    (error) => {
        if (error.response) {
            console.error('API Error:', {
                status: error.response.status,
                data: error.response.data,
            });
        }
        return Promise.reject(error);
    }
);

export default API;
