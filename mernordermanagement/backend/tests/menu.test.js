const request = require('supertest');
const mongoose = require('mongoose');
const { MongoMemoryServer } = require('mongodb-memory-server');
const app = require('../server');
const MenuItem = require('../models/MenuItem');

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
    await MenuItem.deleteMany({});
});

describe('Menu Item API', () => {
    const sampleMenuItem = {
        name: "Test Pizza",
        description: "Test Description",
        price: 10.99,
        category: "main",
        isAvailable: true
    };

    test('GET /api/menu-items should return empty array initially', async () => {
        const response = await request(app).get('/api/menu-items');
        expect(response.status).toBe(200);
        expect(response.body).toEqual([]);
    });

    test('GET /api/menu-items should return menu items after adding', async () => {
        await MenuItem.create(sampleMenuItem);
        const response = await request(app).get('/api/menu-items');
        expect(response.status).toBe(200);
        expect(response.body.length).toBe(1);
        expect(response.body[0].name).toBe(sampleMenuItem.name);
    });

    test('GET /api/menu-items should return multiple menu items', async () => {
        await MenuItem.create(sampleMenuItem);
        await MenuItem.create({
            ...sampleMenuItem,
            name: "Test Pasta"
        });
        
        const response = await request(app).get('/api/menu-items');
        expect(response.status).toBe(200);
        expect(response.body.length).toBe(2);
    });
});
