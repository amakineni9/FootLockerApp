import React, { useState, useEffect } from "react";
import './App.css';
import LoginForm from './components/LoginForm';
import HomePage from './components/HomePage';

function App() {
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    
    useEffect(() => {
        const user = localStorage.getItem('user');
        if (user) {
            setIsLoggedIn(true);
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('user');
        setIsLoggedIn(false);
    };

    return (
        <div className="app-container">
            {isLoggedIn ? (
                <HomePage onLogout={handleLogout} />
            ) : (
                <div className="login-register-wrapper">
                    <LoginForm onLoginSuccess={() => setIsLoggedIn(true)} />
                </div>
            )}
        </div>
    );
}

export default App;
