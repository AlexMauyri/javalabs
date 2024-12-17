import React from 'react';
import { Navigate } from 'react-router-dom';
import Cookies from 'js-cookie';

interface ProtectedRouteProps {
    component: React.ComponentType;
}

const ProtectedRoute: React.FC<ProtectedRouteProps> = ({ component: Component }) => {
    const token = Cookies.get('authToken');

    if (!token) {
        return <Navigate to="/" replace />;
    }

    return <Component />;
};

export default ProtectedRoute;