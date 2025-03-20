# FootLockerApp
FootLocker Online Shopping Application  

This web application will allow customers to make online purchases of products available for sale at Footlocker. All customers can view all of their past orders, which includes an invoice number and a list of items they purchased to go along with the total price of the order. Owners can also use the application to view the sales performance of each of the stores and the entire company as a whole.

## Features

### User Management
- User registration and authentication
- Role-based access (Customers and Owners)
- Email validation and unique constraints
- Secure password handling

### Product Management
- Product catalog with brands and categories
- Price management
- Product-brand relationships
- Product type categorization

### Store Management
- Multiple store locations
- State and city tracking
- Store-specific inventory
- Store performance metrics

### Inventory Management
- Real-time stock tracking
- Store-specific inventory levels
- Low stock alerts
- Min/max threshold management
- Automatic stock level validation

### Sales and Invoicing
- Complete order tracking
- Invoice generation
- Sales history
- Store-specific sales tracking
- User purchase history

## API Endpoints

### User API
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user details
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user
- `POST /api/users/login` - User login

### Product API
- `GET /api/products` - List all products
- `GET /api/products/{id}` - Get product details
- `GET /api/products/brand/{brandId}` - Get products by brand
- `GET /api/products/type/{typeId}` - Get products by type
- `POST /api/products` - Add new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

### Store API
- `GET /api/stores` - List all stores
- `GET /api/stores/{id}` - Get store details
- `GET /api/stores/state/{state}` - Get stores by state
- `POST /api/stores` - Add new store
- `PUT /api/stores/{id}` - Update store
- `DELETE /api/stores/{id}` - Delete store

### Inventory API
- `GET /api/inventory` - List all inventory
- `GET /api/inventory/{id}` - Get specific inventory
- `GET /api/inventory/store/{storeId}` - Get store's inventory
- `GET /api/inventory/product/{productId}` - Get product inventory
- `GET /api/inventory/lowstock` - Get low stock items
- `GET /api/inventory/lowstock/store/{storeId}` - Get store's low stock
- `POST /api/inventory` - Add new inventory
- `PUT /api/inventory/{id}` - Update inventory
- `PATCH /api/inventory/{id}/quantity` - Update stock quantity
- `PATCH /api/inventory/{id}/thresholds` - Update min/max thresholds
- `DELETE /api/inventory/{id}` - Remove inventory

### Invoice API
- `GET /api/invoices` - List all invoices
- `GET /api/invoices/{id}` - Get invoice details
- `GET /api/invoices/store/{storeId}` - Get store invoices
- `GET /api/invoices/user/{userId}` - Get user invoices
- `GET /api/invoices/date-range` - Get invoices by date range
- `POST /api/invoices` - Create new invoice
- `PUT /api/invoices/{id}` - Update invoice
- `DELETE /api/invoices/{id}` - Delete invoice

## Technical Stack

### Backend
- Spring Boot 3.2.12
- Java 17
- JPA/Hibernate
- PostgreSQL Database
- JUnit 5 for testing
- Spring Security (planned)

### Frontend
- React 17.0.1
- React Scripts 5.0.1
- Modern UI/UX design
- REST API integration

### Database Schema
1. users (user_id, email, password, first_name, last_name, isOwner)
2. brands (brand_id, name)
3. product_types (productType_id, name)
4. products (product_id, productType_id, brand_id, price)
5. stores (store_id, city, state)
6. inventory (inventory_id, store_id, product_id, quantity, min_threshold, max_threshold, low_stock_alert)
7. invoices (invoice_number, store_id, total_price, date, user_id)
8. sales (sale_id, store_id, units, total_price, invoice_number, product_id, user_id)

## Testing Coverage

### Unit Tests (41 tests)
1. User Management Tests
   - User registration validation
   - Login functionality
   - User CRUD operations
   - Email uniqueness

2. Product Tests
   - Product CRUD operations
   - Brand relationships
   - Type categorization
   - Price validation

3. Store Tests
   - Store CRUD operations
   - State code validation
   - Location management
   - Store filtering

4. Inventory Tests
   - Stock level tracking
   - Low stock alerts
   - Threshold management
   - Quantity validation
   - Store-product relationships

5. Invoice Tests
   - Invoice creation
   - Total price calculation
   - Date range queries
   - Store/user filtering

## Getting Started

1. Database Setup:
```bash
# Create PostgreSQL database
createdb footlocker

# Configure application.properties with:
spring.datasource.url=jdbc:postgresql://localhost:5432/footlocker
spring.datasource.username=postgres
spring.datasource.password=postgres
```

2. Backend Setup:
```bash
cd FootlockerApp-BackEnd
./mvnw spring-boot:run
```

3. Frontend Setup:
```bash
cd footlocker-app-frontend
npm install
npm start
```

The application will be available at:
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080
