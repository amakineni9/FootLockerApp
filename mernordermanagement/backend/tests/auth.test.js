const request = require('supertest');
const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');
const app = require('../server');
const User = require('../models/User');

let mongoServer;

beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();
    await mongoose.connect(mongoUri);
});

afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
});

beforeEach(async () => {
    await User.deleteMany({});
});

describe('Authentication API', () => {
    const sampleUser = {
        name: "Test User",
        email: "test@example.com",
        password: "password123"
    };

    test('POST /api/users/register should create new user', async () => {
        const response = await request(app)
            .post('/api/users/register')
            .send(sampleUser);

        expect(response.status).toBe(201);
        expect(response.body.user.name).toBe(sampleUser.name);
        expect(response.body.user.email).toBe(sampleUser.email);
        expect(response.body.token).toBeDefined();
    });

    test('POST /api/users/login should authenticate user', async () => {
        // Register user first
        await request(app)
            .post('/api/users/register')
            .send(sampleUser);

        // Try to login
        const response = await request(app)
            .post('/api/users/login')
            .send({
                email: sampleUser.email,
                password: sampleUser.password
            });

        expect(response.status).toBe(200);
        expect(response.body.user.email).toBe(sampleUser.email);
        expect(response.body.token).toBeDefined();
    });

    test('POST /api/users/login should fail with wrong password', async () => {
        // Register user first
        await request(app)
            .post('/api/users/register')
            .send(sampleUser);

        // Try to login with wrong password
        const response = await request(app)
            .post('/api/users/login')
            .send({
                email: sampleUser.email,
                password: 'wrongpassword'
            });

        expect(response.status).toBe(400);
    });

    test('POST /api/users/register should fail with duplicate email', async () => {
        // Register first user
        await request(app)
            .post('/api/users/register')
            .send(sampleUser);

        // Try to register same email
        const response = await request(app)
            .post('/api/users/register')
            .send(sampleUser);

        expect(response.status).toBe(400);
    });
});
