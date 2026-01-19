# E-Commerce Backend API Documentation

## 📋 Overview

This is a complete e-commerce backend built with Spring Boot and MongoDB that handles product management, shopping cart, order processing, and payment integration.

**Base URL:** `http://localhost:8080`

---

## 🚀 Getting Started

### Prerequisites
- Java 21
- MongoDB running on `localhost:27017`
- Maven

### Running the Application

```bash
# Navigate to project directory
cd /Users/tanishqjain/Desktop/E-com_backend/BUYIT

# Build the project
./mvnw clean install

# Run the application
./mvnw spring-boot:run
```

The application will start on port **8080**.

---

## 📌 API Endpoints

### **1. Product APIs**

#### Create Product
```http
POST /api/products
Content-Type: application/json

{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}
```

**Response:**
```json
{
  "id": "generated_id",
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}
```

#### Get All Products
```http
GET /api/products
```

**Response:**
```json
[
  {
    "id": "prod_id",
    "name": "Laptop",
    "description": "Gaming Laptop",
    "price": 50000.0,
    "stock": 10
  }
]
```

---

### **2. Cart APIs**

#### Add to Cart
```http
POST /api/cart/add
Content-Type: application/json

{
  "userId": "user123",
  "productId": "prod_id",
  "quantity": 2
}
```

**Response:**
```json
{
  "id": "cart_item_id",
  "userId": "user123",
  "productId": "prod_id",
  "quantity": 2
}
```

#### Get Cart Items
```http
GET /api/cart/{userId}
```

**Response:**
```json
[
  {
    "id": "cart_item_id",
    "productId": "prod_id",
    "quantity": 2,
    "product": {
      "id": "prod_id",
      "name": "Laptop",
      "price": 50000.0,
      "stock": 8
    }
  }
]
```

#### Clear Cart
```http
DELETE /api/cart/{userId}/clear
```

**Response:**
```json
{
  "message": "Cart cleared successfully"
}
```

---

### **3. Order APIs**

#### Create Order
```http
POST /api/orders
Content-Type: application/json

{
  "userId": "user123"
}
```

**Response:**
```json
{
  "id": "order_id",
  "userId": "user123",
  "totalAmount": 100000.0,
  "status": "CREATED",
  "createdAt": "2026-01-19T10:12:30.123Z",
  "items": [
    {
      "id": "item_id",
      "productId": "prod_id",
      "productName": "Laptop",
      "quantity": 2,
      "price": 50000.0
    }
  ]
}
```

#### Get Order
```http
GET /api/orders/{orderId}
```

**Response:**
```json
{
  "id": "order_id",
  "userId": "user123",
  "totalAmount": 100000.0,
  "status": "PAID",
  "createdAt": "2026-01-19T10:12:30.123Z",
  "items": [...]
}
```

---

### **4. Payment APIs**

#### Create Payment
```http
POST /api/payments/create
Content-Type: application/json

{
  "orderId": "order_id",
  "amount": 100000.0
}
```

**Response:**
```json
{
  "id": "payment_id",
  "orderId": "order_id",
  "amount": 100000.0,
  "status": "PENDING",
  "paymentId": "pay_xxxxx",
  "createdAt": "2026-01-19T10:12:33.456Z"
}
```

**Note:** After 3 seconds, the mock payment service will automatically trigger the webhook.

---

### **5. Webhook Endpoint**

#### Payment Webhook (Internal)
```http
POST /api/webhooks/payment
Content-Type: application/json

{
  "orderId": "order_id",
  "paymentId": "pay_xxxxx",
  "status": "SUCCESS"
}
```

**Response:**
```json
{
  "message": "Webhook processed successfully",
  "orderId": "order_id",
  "status": "SUCCESS"
}
```

---

## 🧪 Testing Flow with Postman

### Step 1: Create Products
Create 2-3 products using `POST /api/products`:

```json
// Product 1
{
  "name": "Laptop",
  "description": "Gaming Laptop",
  "price": 50000.0,
  "stock": 10
}

// Product 2
{
  "name": "Mouse",
  "description": "Wireless Mouse",
  "price": 1000.0,
  "stock": 50
}
```

**Save the product IDs** returned in the response.

### Step 2: Add Items to Cart
Use `POST /api/cart/add` with product IDs:

```json
{
  "userId": "user123",
  "productId": "{{productId1}}",
  "quantity": 2
}
```

### Step 3: View Cart
Use `GET /api/cart/user123` to verify items are in cart.

### Step 4: Create Order
Use `POST /api/orders`:

```json
{
  "userId": "user123"
}
```

**Save the orderId** from the response.

### Step 5: Create Payment
Use `POST /api/payments/create`:

```json
{
  "orderId": "{{orderId}}",
  "amount": 100000.0
}
```

### Step 6: Wait and Check Order Status
**Wait 3-4 seconds** for the mock payment to process, then use `GET /api/orders/{{orderId}}`.

The order status should now be **"PAID"**.

---

## 🔄 Complete Flow Diagram

1. **Create Products** → Save product IDs
2. **Add to Cart** → Use product IDs  
3. **View Cart** → Verify items
4. **Create Order** → Cart is cleared, order created with status "CREATED"
5. **Create Payment** → Payment initiated with status "PENDING"
6. **Wait 3 seconds** → Mock payment processes
7. **Webhook Called** → Order status updated to "PAID"
8. **Check Order** → Verify status is "PAID"

---

## 📦 Postman Collection Variables

Set these variables in Postman:

- `baseUrl`: `http://localhost:8080`
- `userId`: `user123`
- `productId1`: (from product creation)
- `productId2`: (from product creation)
- `orderId`: (from order creation)

---

## ⚠️ Error Handling

All endpoints return consistent error responses:

```json
{
  "timestamp": "2026-01-19T10:12:30.123Z",
  "error": "Bad Request",
  "message": "Insufficient stock for product: Laptop",
  "status": 400
}
```

### Common Errors

- **Insufficient Stock**: When adding to cart or creating order
- **Empty Cart**: When trying to create order with empty cart
- **Invalid Order Status**: When trying to pay for non-CREATED order
- **Not Found**: When product/order ID doesn't exist

---

## 📊 Database Collections

The following MongoDB collections are created:

- `users` - User information
- `products` - Product catalog
- `cart_items` - Shopping cart items
- `orders` - Customer orders
- `payments` - Payment transactions

---

## 🎯 Assignment Completion Checklist

- ✅ Product CRUD APIs
- ✅ Cart management (add, view, clear)
- ✅ Order creation from cart
- ✅ Payment integration with mock service
- ✅ Webhook handling for payment status
- ✅ Order status updates
- ✅ Stock management
- ✅ Input validation
- ✅ Error handling
- ✅ Complete e-commerce flow

---

## 💡 Tips

1. **Always save IDs**: Save product IDs, order IDs in Postman variables
2. **Test incrementally**: Test each endpoint before moving to the next
3. **Check logs**: Application logs show payment processing and webhook calls
4. **Wait for webhook**: Allow 3-4 seconds after payment creation
5. **Stock tracking**: Notice how product stock decreases after order creation
