import React, { useState } from 'react';

function LoginForm({ onLoginSuccess }) {
    const [formData, setFormData] = useState({
        email: 'john.doe@example.com',
        password: 'password123'
    });
    const [error, setError] = useState('');
    const [success, setSuccess] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData(prevState => ({
            ...prevState,
            [name]: value
        }));
        setError('');
        setSuccess(false);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError('');
        setSuccess(false);
        setIsLoading(true);

        try {
            const response = await fetch('http://localhost:8080/api/users/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(formData)
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || 'Login failed');
            }

            if (!data.success) {
                throw new Error(data.message || 'Invalid credentials');
            }

            setSuccess(true);
            localStorage.setItem('user', JSON.stringify(data.user));
            console.log('Login successful:', data.user);
            
            // Call the success callback to trigger navigation
            onLoginSuccess && onLoginSuccess();
        } catch (err) {
            console.error('Login error:', err.message);
            if (!navigator.onLine) {
                setError('You appear to be offline. Please check your internet connection.');
            } else if (err.message === 'Failed to fetch') {
                setError('Unable to connect to the server. Please make sure the backend server is running.');
            } else {
                setError(err.message || 'Login failed. Please check your credentials and try again.');
            }
        } finally {
            setIsLoading(false);
        }
    };

    const resetForm = () => {
        setFormData({
            email: 'john.doe@example.com',
            password: 'password123'
        });
        setError('');
        setSuccess(false);
    };

    return (
        <div className="login-form">
            <h2>Login</h2>
            <form onSubmit={handleSubmit} noValidate>
                {error && (
                    <div className="error-message">
                        <p>{error}</p>
                        {error.includes('backend server') && (
                            <small>
                                The server should be running on http://localhost:8080
                            </small>
                        )}
                    </div>
                )}
                {success && (
                    <div className="success-message">
                        <p>Login successful! Welcome back.</p>
                        <small>You are now logged in as {formData.email}</small>
                    </div>
                )}
                
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input
                        type="email"
                        id="email"
                        name="email"
                        value={formData.email}
                        onChange={handleChange}
                        placeholder="john.doe@example.com"
                        required
                        disabled={isLoading}
                        autoComplete="email"
                    />
                </div>

                <div className="form-group">
                    <label htmlFor="password">Password</label>
                    <input
                        type="password"
                        id="password"
                        name="password"
                        value={formData.password}
                        onChange={handleChange}
                        placeholder="Enter your password"
                        required
                        disabled={isLoading}
                        autoComplete="current-password"
                    />
                </div>

                <div className="form-actions">
                    <button 
                        type="submit" 
                        className="submit-button"
                        disabled={isLoading || !formData.email || !formData.password}
                    >
                        {isLoading ? 'Logging in...' : 'Login'}
                    </button>

                    <button 
                        type="button" 
                        className="reset-button"
                        onClick={resetForm}
                        disabled={isLoading}
                    >
                        Reset to Default
                    </button>
                </div>

                <div className="login-help">
                    <p>Default Test Account:</p>
                    <code>
                        Email: john.doe@example.com<br/>
                        Password: password123
                    </code>
                </div>
            </form>
        </div>
    );
}

export default LoginForm;