import React, { useState, useEffect } from 'react';

function HomePage({ onLogout }) {
    const [products, setProducts] = useState([]);
    const [orders, setOrders] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const user = JSON.parse(localStorage.getItem('user'));

    useEffect(() => {
        if (!user) {
            window.location.href = '/';
            return;
        }

        const fetchData = async () => {
            try {
                // Fetch products
                const productsResponse = await fetch('http://localhost:8080/api/products', {
                    headers: {
                        'Accept': 'application/json'
                    }
                });
                const productsData = await productsResponse.json();

                // Fetch invoices (orders)
                const invoicesResponse = await fetch('http://localhost:8080/api/invoices', {
                    headers: {
                        'Accept': 'application/json'
                    }
                });
                const invoicesData = await invoicesResponse.json();

                setProducts(productsData);
                setOrders(invoicesData);
            } catch (err) {
                console.error('Error fetching data:', err);
                setError('Failed to load data. Please try again later.');
            } finally {
                setLoading(false);
            }
        };

        fetchData();
    }, [user]);

    return (
        <div className="home-page">
            <header className="home-header">
                <h1>Welcome, {user.first_name}!</h1>
                <button 
                    className="logout-button"
                    onClick={() => {
                        localStorage.removeItem('user');
                        onLogout && onLogout();
                    }}
                >
                    Logout
                </button>
            </header>

            {error && (
                <div className="error-message">
                    <p>{error}</p>
                </div>
            )}
            
            <div className="dashboard-grid">
                <div className="dashboard-card">
                    <h2>Your Profile</h2>
                    <div className="info-card">
                        <div className="info-row">
                            <label>Name:</label>
                            <span>{user.first_name} {user.last_name}</span>
                        </div>
                        <div className="info-row">
                            <label>Email:</label>
                            <span>{user.email}</span>
                        </div>
                        <div className="info-row">
                            <label>Role:</label>
                            <span>{user.owner ? 'Owner' : 'Staff Member'}</span>
                        </div>
                    </div>
                </div>

                <div className="dashboard-card">
                    <h2>Recent Orders</h2>
                    <div className="data-card">
                        {loading ? (
                            <div className="loading">Loading orders...</div>
                        ) : orders.length > 0 ? (
                            <div className="orders-list">
                                {orders.slice(0, 5).map(order => (
                                    <div key={order.invoice_number} className="order-item">
                                        <div className="order-header">
                                            <span className="order-id">Order #{order.invoice_number}</span>
                                            <span className="order-date">
                                                {new Date(order.date).toLocaleDateString()}
                                            </span>
                                        </div>
                                        <div className="order-details">
                                            <span className="store">Store: {order.store_id}</span>
                                            <span className="total">Total: ${order.total_price.toFixed(2)}</span>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="no-data">No recent orders found</div>
                        )}
                    </div>
                </div>

                <div className="dashboard-card">
                    <h2>Available Products</h2>
                    <div className="data-card">
                        {loading ? (
                            <div className="loading">Loading products...</div>
                        ) : products.length > 0 ? (
                            <div className="products-grid">
                                {products.slice(0, 6).map(product => (
                                    <div key={product.product_id} className="product-card">
                                        <div className="product-info">
                                            <span className="product-type">
                                                {product.product_type_id}
                                            </span>
                                            <span className="product-brand">
                                                {product.brand_id}
                                            </span>
                                            <span className="product-price">
                                                ${product.price.toFixed(2)}
                                            </span>
                                        </div>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="no-data">No products available</div>
                        )}
                    </div>
                </div>
            </div>
        </div>
    );
}

export default HomePage;
