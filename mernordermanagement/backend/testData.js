const mongoose = require('mongoose');
const MenuItem = require('./models/MenuItem');
const User = require('./models/User');
const Order = require('./models/Order');

const testMenuItems = [
    {
        name: "Margherita Pizza",
        description: "Classic pizza with tomato sauce and mozzarella",
        price: 12.99,
        category: "main",
        isAvailable: true
    },
    {
        name: "Pepperoni Pizza",
        description: "Classic pizza with pepperoni and cheese",
        price: 14.99,
        category: "main",
        isAvailable: true
    },
    {
        name: "Caesar Salad",
        description: "Fresh romaine lettuce with caesar dressing",
        price: 8.99,
        category: "appetizer",
        isAvailable: true
    },
    {
        name: "Greek Salad",
        description: "Fresh vegetables with feta cheese and olives",
        price: 9.99,
        category: "appetizer",
        isAvailable: true
    },
    {
        name: "Chocolate Brownie",
        description: "Warm chocolate brownie with vanilla ice cream",
        price: 6.99,
        category: "dessert",
        isAvailable: true
    },
    {
        name: "Tiramisu",
        description: "Classic Italian coffee-flavored dessert",
        price: 7.99,
        category: "dessert",
        isAvailable: true
    },
    {
        name: "Iced Tea",
        description: "Freshly brewed iced tea",
        price: 3.99,
        category: "beverage",
        isAvailable: true
    },
    {
        name: "Lemonade",
        description: "Fresh squeezed lemonade",
        price: 3.99,
        category: "beverage",
        isAvailable: true
    }
];

const testUsers = [
    {
        name: "John Doe",
        email: "john@example.com",
        password: "password123",
        role: "customer"
    },
    {
        name: "Admin User",
        email: "admin@example.com",
        password: "admin123",
        role: "admin"
    }
];

async function seedDatabase() {
    try {
        console.log('Connecting to MongoDB...');
        await mongoose.connect('mongodb://127.0.0.1:27017/food-order-app', {
            useNewUrlParser: true,
            useUnifiedTopology: true
        });
        console.log('Connected to MongoDB successfully!');
        
        // Clear existing data
        console.log('Clearing existing data...');
        await MenuItem.deleteMany({});
        await User.deleteMany({});
        await Order.deleteMany({});
        console.log('Existing data cleared.');
        
        // Insert test menu items
        console.log('Inserting menu items...');
        const menuItems = await MenuItem.insertMany(testMenuItems);
        console.log(`${menuItems.length} menu items inserted successfully`);
        
        // Insert test users
        console.log('Inserting users...');
        const users = await User.insertMany(testUsers);
        console.log(`${users.length} users inserted successfully`);
        
        // Create some test orders
        console.log('Creating test orders...');
        const testOrders = [
            {
                customerName: "John Doe",
                customerEmail: "john@example.com",
                items: [
                    {
                        menuItem: menuItems[0]._id,
                        quantity: 2,
                        price: menuItems[0].price
                    },
                    {
                        menuItem: menuItems[2]._id,
                        quantity: 1,
                        price: menuItems[2].price
                    }
                ],
                totalAmount: (menuItems[0].price * 2) + menuItems[2].price,
                status: "pending"
            },
            {
                customerName: "John Doe",
                customerEmail: "john@example.com",
                items: [
                    {
                        menuItem: menuItems[1]._id,
                        quantity: 1,
                        price: menuItems[1].price
                    }
                ],
                totalAmount: menuItems[1].price,
                status: "delivered"
            }
        ];
        
        await Order.insertMany(testOrders);
        console.log(`${testOrders.length} test orders inserted successfully`);
        
        console.log('Database seeded successfully!');
        
        // Display some statistics
        const stats = {
            menuItems: await MenuItem.countDocuments(),
            users: await User.countDocuments(),
            orders: await Order.countDocuments()
        };
        console.log('\nDatabase Statistics:');
        console.log('-------------------');
        console.log(`Menu Items: ${stats.menuItems}`);
        console.log(`Users: ${stats.users}`);
        console.log(`Orders: ${stats.orders}`);
        
        await mongoose.connection.close();
        console.log('\nDatabase connection closed.');
    } catch (error) {
        console.error('Error seeding database:', error);
        process.exit(1);
    }
}

// Run the seeding function
seedDatabase();
