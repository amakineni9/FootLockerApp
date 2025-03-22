const request = require('supertest');
const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');
const app = require('../server');
const Order = require('../models/Order');
const User = require('../models/User');
const MenuItem = require('../models/MenuItem');
const jwt = require('jsonwebtoken');

let mongoServer;
let testUser;
let testMenuItem;
let authToken;

beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();
    await mongoose.connect(mongoUri);

    // Create test user
    testUser = await User.create({
        name: "Test User",
        email: "test@example.com",
        password: "password123",
        role: "customer"
    });

    // Create test menu item
    testMenuItem = await MenuItem.create({
        name: "Test Pizza",
        description: "Test Description",
        price: 10.99,
        category: "main",
        isAvailable: true
    });

    // Create auth token
    authToken = jwt.sign({ userId: testUser._id }, 'your_jwt_secret');
});

afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
});

beforeEach(async () => {
    await Order.deleteMany({});
});

describe('Order API', () => {
    const createSampleOrder = () => ({
        items: [
            {
                menuItem: testMenuItem._id,
                quantity: 2,
                price: testMenuItem.price
            }
        ],
        totalAmount: testMenuItem.price * 2,
        status: "pending"
    });

    test('POST /api/orders should create new order', async () => {
        const orderData = createSampleOrder();
        const response = await request(app)
            .post('/api/orders')
            .set('Authorization', `Bearer ${authToken}`)
            .send(orderData);

        expect(response.status).toBe(201);
        expect(response.body.customerName).toBe(testUser.name);
        expect(response.body.customerEmail).toBe(testUser.email);
        expect(response.body.totalAmount).toBe(orderData.totalAmount);
    });

    test('GET /api/orders should return user orders', async () => {
        const orderData = createSampleOrder();
        await request(app)
            .post('/api/orders')
            .set('Authorization', `Bearer ${authToken}`)
            .send(orderData);

        const response = await request(app)
            .get('/api/orders')
            .set('Authorization', `Bearer ${authToken}`);

        expect(response.status).toBe(200);
        expect(response.body.length).toBe(1);
        expect(response.body[0].customerEmail).toBe(testUser.email);
    });

    test('DELETE /api/orders/:id should cancel order', async () => {
        // Create an order first
        const orderData = createSampleOrder();
        const createResponse = await request(app)
            .post('/api/orders')
            .set('Authorization', `Bearer ${authToken}`)
            .send(orderData);

        const orderId = createResponse.body._id;

        // Try to cancel the order
        const response = await request(app)
            .delete(`/api/orders/${orderId}`)
            .set('Authorization', `Bearer ${authToken}`);

        expect(response.status).toBe(200);
        expect(response.body.status).toBe('cancelled');
    });

    test('PUT /api/orders/:id should modify pending order', async () => {
        // Create an order first
        const orderData = createSampleOrder();
        const createResponse = await request(app)
            .post('/api/orders')
            .set('Authorization', `Bearer ${authToken}`)
            .send(orderData);

        const orderId = createResponse.body._id;

        // Modify the order
        const updateData = {
            items: [
                {
                    menuItem: testMenuItem._id,
                    quantity: 3,
                    price: testMenuItem.price
                }
            ],
            totalAmount: testMenuItem.price * 3
        };

        const response = await request(app)
            .put(`/api/orders/${orderId}`)
            .set('Authorization', `Bearer ${authToken}`)
            .send(updateData);

        expect(response.status).toBe(200);
        expect(response.body.totalAmount).toBe(updateData.totalAmount);
    });
});
