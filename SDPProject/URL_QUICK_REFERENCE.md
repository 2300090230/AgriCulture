# 🌾 Quick Reference - Agriculture Platform Backend URLs

## Base URL
```
http://localhost:8080
```

---

## 👨‍🌾 FARMER APIs (`/farmer`)

### Authentication & Registration
```http
POST   /farmer/registration              # Register new farmer
POST   /farmer/checkfarmerlogin          # Login
POST   /farmer/fforgot-password          # Forgot password
POST   /farmer/freset-password           # Reset password
```

### Farmer Management
```http
GET    /farmer/viewallfarmers            # View all farmers
GET    /farmer/pending                   # View pending farmers
PUT    /farmer/updatefarmer              # Update profile
PUT    /farmer/reject/{id}               # Reject farmer
DELETE /farmer/delete?id={id}            # Delete farmer
```

### Farmer Dashboard Analytics
```http
GET    /farmer/{farmerId}/products/count # Total products
GET    /farmer/{farmerId}/orders/count   # Total orders
GET    /farmer/{farmerId}/revenue        # Total revenue
GET    /farmer/{farmerId}/sales-data     # Sales analytics (daily/monthly)
```

---

## 🛒 BUYER APIs (`/buyer`)

```http
POST   /buyer/registration               # Register new buyer
POST   /buyer/checkbuyerlogin            # Login
POST   /buyer/forgot-password            # Forgot password
POST   /buyer/reset-password             # Reset password
```

---

## 👔 ADMIN APIs (`/admin`)

### Authentication
```http
POST   /admin/checkadminlogin            # Admin login
```

### Farmer Management
```http
POST   /admin/addfarmer                  # Add farmer
GET    /admin/viewallfarmers             # View all farmers
POST   /admin/approvefarmer/{id}         # Approve farmer
```

### Platform Management
```http
GET    /admin/viewallbuyers              # View all buyers
```

### Platform Analytics
```http
GET    /admin/farmers/count              # Total farmers
GET    /admin/buyers/count               # Total buyers
GET    /admin/products/count             # Total products
GET    /admin/orders/count               # Total orders
GET    /admin/revenue                    # Total revenue
GET    /admin/sales-data                 # Sales data (daily/monthly)
```

---

## 🌾 PRODUCT APIs (`/product`)

### Product Management
```http
POST   /product/addproduct               # Add new product (multipart/form-data)
PUT    /product/updateproduct            # Update product (multipart/form-data)
DELETE /product/deleteproduct/{id}       # Delete product
```

### Product Queries
```http
GET    /product/viewallproducts          # View all products
GET    /product/getproduct/{id}          # Get product by ID
GET    /product/viewproductsbyfarmer/{fid} # Get farmer's products
GET    /product/categories?category={name}  # Filter by category
GET    /product/displayproductimage?id={id} # Get product image
```

---

## 🛍️ CART APIs (`/cart`)

```http
POST   /cart/add                         # Add to cart
GET    /cart/buyer/{buyerId}             # Get cart items
PUT    /cart/update                      # Update quantity
DELETE /cart/remove/{cartId}             # Remove item
DELETE /cart/clear/{buyerId}             # Clear cart
GET    /cart/count/{buyerId}             # Get cart count
```

---

## 📦 ORDER APIs (`/order`)

```http
GET    /order/buyer/{buyerId}            # Get buyer's orders
GET    /order/farmer/{farmerId}          # Get farmer's orders
```

---

## 💳 PAYMENT APIs (`/payment`)

```http
POST   /payment/create-order             # Create Razorpay order
POST   /payment/verify-payment           # Verify payment & create orders
```

---

## 📍 ADDRESS APIs (`/address`)

```http
POST   /address/add                      # Add new address
GET    /address/buyer/{buyerId}          # Get buyer's addresses
PUT    /address/update/{id}              # Update address
DELETE /address/delete/{id}              # Delete address
```

---

## 🔑 Key Parameter Names

| Entity | ID Parameter | Example |
|--------|--------------|---------|
| Farmer | `fid` or `farmerId` | `/farmer/{farmerId}/revenue` |
| Buyer  | `buyerId` | `/order/buyer/{buyerId}` |
| Product | `id` or `productId` | `/product/getproduct/{id}` |
| Cart | `cartId` | `/cart/remove/{cartId}` |
| Order | `id` or `orderId` | - |
| Address | `id` or `addressId` | `/address/update/{id}` |

---

## 📝 Common Request Bodies

### Farmer Registration
```json
{
  "name": "John Farmer",
  "email": "john@farmer.com",
  "username": "johnfarmer",
  "password": "password123",
  "mobileno": "1234567890",
  "nationalidno": "NAT123456",
  "location": "Karnataka"
}
```

### Buyer Registration
```json
{
  "name": "Jane Buyer",
  "email": "jane@buyer.com",
  "password": "password123",
  "mobileno": "9876543210"
}
```

### Add to Cart
```json
{
  "buyer": { "id": 1 },
  "product": { "id": 10 },
  "quantity": 2
}
```

### Add Address
```json
{
  "buyer": { "id": 1 },
  "street": "123 Main St",
  "city": "Bangalore",
  "state": "Karnataka",
  "postalCode": "560001",
  "country": "India"
}
```

---

## 🎨 Product Categories (Example)

Common agricultural product categories:
- Grains (Rice, Wheat, Corn)
- Vegetables (Tomato, Potato, Onion)
- Fruits (Mango, Banana, Apple)
- Pulses (Lentils, Chickpeas, Beans)
- Spices (Turmeric, Chili, Coriander)
- Dairy (Milk, Ghee, Butter)
- Organic Products

---

## 🔐 Authentication Flow

### Farmer
1. Register → `POST /farmer/registration`
2. Wait for Admin Approval
3. Login → `POST /farmer/checkfarmerlogin`
4. Access Dashboard & Manage Products

### Buyer
1. Register → `POST /buyer/registration`
2. Login → `POST /buyer/checkbuyerlogin`
3. Browse Products & Place Orders

### Admin
1. Login → `POST /admin/checkadminlogin`
2. Approve Farmers → `POST /admin/approvefarmer/{id}`
3. Monitor Platform → `/admin/*/count` endpoints

---

## 📊 Dashboard Analytics

### Farmer Dashboard Data
```http
# Get all dashboard data for a farmer
GET /farmer/{farmerId}/products/count    # Returns: {"farmerId": 1, "totalProducts": 25}
GET /farmer/{farmerId}/orders/count      # Returns: {"farmerId": 1, "totalOrders": 150}
GET /farmer/{farmerId}/revenue           # Returns: {"farmerId": 1, "totalRevenue": 50000.0}
GET /farmer/{farmerId}/sales-data?period=daily  # Returns: Array of sales data
```

### Admin Dashboard Data
```http
# Get platform-wide statistics
GET /admin/farmers/count     # Returns: {"totalFarmers": 50}
GET /admin/buyers/count      # Returns: {"totalBuyers": 200}
GET /admin/products/count    # Returns: {"totalProducts": 500}
GET /admin/orders/count      # Returns: {"totalOrders": 1000}
GET /admin/revenue           # Returns: {"totalRevenue": 500000.0}
GET /admin/sales-data?period=monthly  # Returns: Array of monthly data
```

---

## 🛠️ Form-Data Endpoints

### Add Product
```http
POST /product/addproduct
Content-Type: multipart/form-data

Form Data:
- category: "Grains"
- name: "Organic Rice"
- description: "Premium quality"
- cost: 500
- productimage: [FILE]
- fid: 1
```

### Update Product
```http
PUT /product/updateproduct
Content-Type: multipart/form-data

Form Data:
- id: 10
- category: "Grains"
- name: "Organic Rice"
- description: "Premium quality"
- cost: 550
- productimage: [FILE] (optional)
- fid: 1
```

---

## ⚠️ Error Responses

Common HTTP Status Codes:
- `200 OK` - Success
- `401 Unauthorized` - Invalid credentials
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## 🔄 URL Migration Reference

| Old URL | New URL |
|---------|---------|
| `/seller/registration` | `/farmer/registration` |
| `/seller/checksellerlogin` | `/farmer/checkfarmerlogin` |
| `/admin/addseller` | `/admin/addfarmer` |
| `/admin/viewallsellers` | `/admin/viewallfarmers` |
| `/product/viewproductsbyseller/{sid}` | `/product/viewproductsbyfarmer/{fid}` |
| `/order/seller/{sellerId}` | `/order/farmer/{farmerId}` |

---

**Last Updated:** November 25, 2025  
**Project:** Agriculture E-Commerce Platform
