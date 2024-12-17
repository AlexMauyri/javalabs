import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import axios from '../../axiosConfig.tsx';
import Cookies from 'js-cookie';
import MatrixBackground from "../MatrixBackground.tsx";
import useFileHandler from "../useFileHandler.tsx";
import { EyeOff, Eye } from 'lucide-react';

const Auth: React.FC = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [rememberMe, setRememberMe] = useState(false);
    const navigate = useNavigate();
    const [showPassword, setShowPassword] = useState(false);
    const server = 'http://localhost:8080/api';

    const { showError, Messages } = useFileHandler('http://localhost:8080'.slice(0, 22));

    useEffect(() => {
        const token = Cookies.get('authToken');
        if (token) {
            navigate('/app');
        }
    }, []);

    const validateInputs = () => {
        if (!username.trim()) {
            showError('Логин не может быть пустым');
            return false;
        }

        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$/;
        if (!password.trim()) {
            showError('Пароль не может быть пустым');
            return false;
        } else if (!passwordRegex.test(password)) {
            showError('Пароль должен содержать минимум 8 символов, включая буквы, цифры и специальные символы');
            return false;
        }

        return true;
    };


    const handleLogin = async (name: string, pass: string) => {
        if (pass !== 'guestpassword' && name !== 'guestlogin' && !validateInputs()) {
            return;
        }
        try {
            const response = await axios.post(`${server}/login`, {username: name, password: pass});
            const token = response.data.token;

            if (rememberMe) {
                Cookies.set('authToken', token, {expires: 3});
            } else {
                Cookies.set('authToken', token);
            }

            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            navigate('/app');
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                console.log(error);
                alert(`Ошибка входа: ${error.response.data}`);
            } else {
                alert('Ошибка входа. Пожалуйста, проверьте логин и пароль.');
            }
        }
    };


    const handleRegister = async () => {
        if (!validateInputs()) {
            return;
        }
        try {
            const response = await axios.post(`${server}/register`, {username, password});
            console.log(response);
            const token = response.data.token;

            if (rememberMe) {
                Cookies.set('authToken', token, {expires: 7});
            } else {
                Cookies.set('authToken', token);
            }

            axios.defaults.headers.common['Authorization'] = `Bearer ${token}`;
            navigate('/app');
        } catch (error) {
            if (axios.isAxiosError(error) && error.response) {
                console.log(error);
                alert(`Ошибка регистрации: ${error.response.data}`);
            } else {
                alert('Ошибка регистрации. Возможно, пользователь с таким именем уже существует.');
            }
        }
    };

    const handleGuestLogin = () => {
        handleLogin('guestlogin', 'guestpassword');
    };

    return (
        <div className="flex items-center justify-center min-h-screen">
            <MatrixBackground/>
            <Messages />
            <div className="flex-col flex items-center w-full space-y-12">
                <h1 className="text-6xl text-white font-extrabold text-center mb-4 font-mono motion-preset-blur-down-lg motion-duration-2000">
                    Function Overdose
                </h1>
                <div
                    className="p-8 rounded-lg shadow-md max-w-md w-full motion-preset-blur-up-lg motion-duration-2000 backdrop-blur-sm bg-transparent">
                    <form onSubmit={(e) => e.preventDefault()}>
                        <div className="mb-4">
                            <label className="block text-gray-200 mb-2 font-mono" htmlFor="username">
                                login_
                            </label>
                            <input
                                type="text"
                                id="username"
                                className={`w-full px-4 py-2 bg-gray-800 border border-transparent rounded-md
                                text-white font-bold
                                focus:outline-none outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-teal-950 focus:border-transparent transition-all duration-300`}

                                value={username}
                                onChange={(e) => setUsername(e.target.value)}
                            />
                        </div>
                        <div className="mb-4 relative">
                            <label className="block text-gray-200 mb-2" htmlFor="password">
                                password_
                            </label>
                            <input
                                type={showPassword ? "text" : "password"}
                                id="password"
                                className="w-full px-4 py-2 bg-gray-800 border border-transparent rounded-md text-white font-bold focus:outline-none outline-none focus:ring-2 focus:ring-teal-600 focus:ring-offset-2 focus:ring-offset-teal-950 focus:border-transparent transition-all duration-300"
                                value={password}
                                onChange={(e) => setPassword(e.target.value)}
                            />
                            <button
                                type="button"
                                className="absolute right-3 pt-2 transform text-gray-400 transition-all hover:text-teal-400 hover:scale-110"
                                onClick={() => setShowPassword(!showPassword)}
                            >
                                {showPassword ? <Eye/> : <EyeOff/>}
                            </button>
                        </div>
                        <div className="flex items-center mb-4">
                            <input
                                type="checkbox"
                                id="rememberMe"
                                className="mr-2 bg-gray-800 text-gray-700"
                                checked={rememberMe}
                                onChange={(e) => setRememberMe(e.target.checked)}
                            />
                            <label htmlFor="rememberMe" className="text-gray-500 ">Запомнить меня</label>
                        </div>
                        <div className="flex items-center justify-between">
                            <button
                                type="button"
                                className="bg-teal-600 text-white px-4 py-2 rounded-md min-w-24 transition-all duration-300 ease-in-out hover:shadow-lg hover:bg-teal-400 hover:scale-125"
                                onClick={() => handleLogin(username, password)}
                            >
                                Войти
                            </button>
                            <button
                                type="button"
                                className="text-teal-600 hover:underline hover:scale-110 transition-all"
                                onClick={handleRegister}
                            >
                                Зарегистрироваться
                            </button>
                        </div>
                    </form>
                    <div className="text-center mt-4">
                        <button
                            type="button"
                            className="text-teal-600 hover:underline hover:scale-110 transition-all"
                            onClick={handleGuestLogin}
                        >
                            Гостевой режим
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default Auth;