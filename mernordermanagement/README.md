# Food Order Management System

A full-stack MERN (MongoDB, Express.js, React, Node.js) application for managing food orders. This application allows users to browse menus, place orders, and track their order status in real-time.

## Features

- 🔐 User Authentication (JWT)
- 🍽️ Menu Management
- 🛒 Order Processing
- 📊 Order Tracking
- 👤 User Roles (Customer/Admin)
- 💾 MongoDB Integration
- 🎨 Material-UI v5 Interface

## Tech Stack

### Backend
- Node.js
- Express.js
- MongoDB
- JWT Authentication
- Mongoose ODM

### Frontend
- React 18
- Material-UI v5
- React Router v6
- Axios
- React Toastify

## Prerequisites

- Node.js (v16 or higher)
- MongoDB (v4.4 or higher)
- npm (v7 or higher)

## Installation

### 1. Clone the Repository

\```bash
git clone <repository-url>
cd fullstacklambda
\```

### 2. Install Backend Dependencies

\```bash
cd backend
npm install
\```

### 3. Install Frontend Dependencies

\```bash
cd frontend
npm install
\```

## Configuration

### Backend Configuration

1. Create a `.env` file in the backend directory:

\```env
PORT=5000
MONGODB_URI=mongodb://localhost:27017/food-order-app
JWT_SECRET=your_jwt_secret
\```

### Frontend Configuration

The frontend proxy is configured to connect to the backend at `http://localhost:5000` in `package.json`:

\```json
{
  "proxy": "http://localhost:5000"
}
\```

## Database Setup

1. Start MongoDB service:
\```bash
# Windows (PowerShell as Administrator)
Start-Service MongoDB

# Check MongoDB status
Get-Service MongoDB
\```

2. Initialize the database with test data:
\```bash
cd backend
node testData.js
\```

This will create:
- 8 menu items
- 2 test users (customer and admin)
- 2 sample orders

## Running the Application

### Start Backend Server

\```bash
cd backend
npm start
\```

The backend server will run on http://localhost:5000

### Start Frontend Development Server

\```bash
cd frontend
npm start
\```

The frontend will run on http://localhost:3000

## Testing

### Running Backend Tests

\```bash
cd backend
npm test
\```

This will run tests for:
- User Authentication
- Menu Items API
- Order Management

### Running Frontend Tests

\```bash
cd frontend
npm test
\```

## API Endpoints

### Authentication
- POST /api/users/register - Register new user
- POST /api/users/login - User login

### Menu Items
- GET /api/menu-items - List all menu items

### Orders
- POST /api/orders - Create new order
- GET /api/orders - List user's orders
- PUT /api/orders/:id - Update order status
- DELETE /api/orders/:id - Cancel order

## Common Issues and Troubleshooting

### Port Already in Use

If port 3000 or 5000 is already in use:

\```bash
# Windows (PowerShell)
# For port 3000 (Frontend)
Stop-Process -Id (Get-NetTCPConnection -LocalPort 3000).OwningProcess -Force

# For port 5000 (Backend)
Stop-Process -Id (Get-NetTCPConnection -LocalPort 5000).OwningProcess -Force
\```

### MongoDB Connection Issues

1. Check if MongoDB is running:
\```bash
Get-Service MongoDB
\```

2. Verify MongoDB connection:
\```bash
mongosh --eval "db.serverStatus()"
\```

### Frontend Build Issues

If you encounter dependency conflicts:

\```bash
cd frontend
Remove-Item -Recurse -Force node_modules
Remove-Item package-lock.json
npm install
\```

### Material-UI Version Conflicts

If you see Material-UI related errors:

\```bash
cd frontend
npm uninstall @material-ui/core @material-ui/icons
npm install @mui/material @mui/icons-material @emotion/react @emotion/styled
\```

## Test Users

1. Customer Account:
   - Email: john@example.com
   - Password: password123

2. Admin Account:
   - Email: admin@example.com
   - Password: admin123

## Development Commands

### Backend Development

\```bash
# Start backend with nodemon (auto-reload)
npm run server

# Run backend tests
npm test

# Seed database
node testData.js
\```

### Frontend Development

\```bash
# Start frontend development server
npm start

# Create production build
npm run build

# Run frontend tests
npm test

# Run tests in watch mode
npm run test:watch
\```

## Project Structure

\```
fullstacklambda/
├── backend/
│   ├── models/
│   │   ├── MenuItem.js
│   │   ├── Order.js
│   │   └── User.js
│   ├── tests/
│   │   ├── auth.test.js
│   │   ├── menu.test.js
│   │   └── order.test.js
│   ├── server.js
│   └── testData.js
├── frontend/
│   ├── src/
│   │   ├── components/
│   │   ├── context/
│   │   └── App.js
│   └── package.json
└── package.json
\```

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a new Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
