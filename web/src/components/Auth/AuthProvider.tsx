import React, { useEffect } from 'react';
import axios from '../../axiosConfig.tsx';
import Cookies from 'js-cookie';

const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
    useEffect(() => {
        const token = Cookies.get('authToken');
        if (token) {
            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
        } else {
            delete axios.defaults.headers.common['Authorization'];
        }
    }, []);

    return <>{children}</>;
};

export default AuthProvider;