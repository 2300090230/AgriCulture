# 🚀 Quick API Reference Card
**Agriculture E-Commerce Platform - Frontend Developer Quick Guide**

---

## ⚡ Quick Start

**Base URL:** `http://localhost:2004`

---

## 🔑 Authentication Flow

```javascript
// 1. Login
const response = await fetch('http://localhost:2004/farmer/checkfarmerlogin', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username, password })
});

const user = await response.json();
localStorage.setItem('farmer', JSON.stringify(user));
localStorage.setItem('farmerId', user.id);
```

---

## 📋 All 49 API Endpoints at a Glance

### 🚜 FARMER (13)
```
POST   /farmer/registration                    - Register farmer
POST   /farmer/checkfarmerlogin                - Login
GET    /farmer/pending                         - View pending farmers
PUT    /farmer/reject/{id}                     - Reject farmer
DELETE /farmer/delete?id={id}                  - Delete farmer
PUT    /farmer/updatefarmer                    - Update profile
GET    /farmer/viewallfarmers                  - List all farmers
GET    /farmer/{farmerId}/products/count       - Product count
GET    /farmer/{farmerId}/orders/count         - Order count
GET    /farmer/{farmerId}/revenue              - Total revenue
GET    /farmer/{farmerId}/sales-data?period    - Sales analytics
POST   /farmer/fforgot-password?email          - Forgot password
POST   /farmer/freset-password?token&newPassword - Reset password
```

### 🛒 BUYER (4)
```
POST   /buyer/registration                     - Register buyer
POST   /buyer/checkbuyerlogin                  - Login
POST   /buyer/forgot-password?email            - Forgot password
POST   /buyer/reset-password?token&newPassword - Reset password
```

### 👨‍💼 ADMIN (11)
```
POST   /admin/checkadminlogin                  - Admin login
POST   /admin/addfarmer                        - Add farmer
GET    /admin/viewallfarmers                   - List farmers
GET    /admin/viewallbuyers                    - List buyers
POST   /admin/approvefarmer/{id}               - Approve farmer
GET    /admin/farmers/count                    - Farmer count
GET    /admin/buyers/count                     - Buyer count
GET    /admin/products/count                   - Product count
GET    /admin/orders/count                     - Order count
GET    /admin/revenue                          - Total revenue
GET    /admin/sales-data?period                - Platform analytics
```

### 🌾 PRODUCT (8)
```
POST   /product/addproduct                     - Add (multipart/form-data)
PUT    /product/updateproduct                  - Update (multipart/form-data)
GET    /product/viewallproducts                - List all
GET    /product/getproduct/{id}                - Get by ID
GET    /product/displayproductimage?id         - Display image
GET    /product/viewproductsbyfarmer/{fid}     - Filter by farmer
DELETE /product/deleteproduct/{id}             - Delete
GET    /product/categories?category            - Filter by category
```

### 🛒 CART (6)
```
POST   /cart/add                               - Add to cart
GET    /cart/buyer/{buyerId}                   - Get cart items
GET    /cart/count/{buyerId}                   - Cart count
DELETE /cart/remove/{cartId}                   - Remove item
DELETE /cart/clear/{buyerId}                   - Clear cart
PUT    /cart/update?buyerId&productId&quantity - Update quantity
```

### 📦 ORDER (2)
```
GET    /order/buyer/{buyerId}                  - Buyer orders
GET    /order/farmer/{farmerId}                - Farmer orders
```

### 💳 PAYMENT (2)
```
POST   /payment/create-order                   - Create Razorpay order
POST   /payment/verify-payment                 - Verify & create orders
```

### 📍 ADDRESS (3)
```
POST   /address/add/{buyerId}                  - Add address
GET    /address/buyer/{buyerId}                - Get addresses
DELETE /address/delete/{id}                    - Delete address
```

---

## 🎯 Most Common Frontend Use Cases

### 1. User Registration & Login
```javascript
// Register
fetch('http://localhost:2004/farmer/registration', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(farmerData)
});

// Login
fetch('http://localhost:2004/farmer/checkfarmerlogin', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ username, password })
});
```

### 2. Display Products with Images
```javascript
// Get products
const products = await fetch('http://localhost:2004/product/viewallproducts')
  .then(r => r.json());

// Display with images
products.forEach(product => {
  const img = document.createElement('img');
  img.src = `http://localhost:2004/product/displayproductimage?id=${product.id}`;
});
```

### 3. Add Product with Image
```javascript
const formData = new FormData();
formData.append('category', 'Vegetables');
formData.append('name', 'Tomatoes');
formData.append('description', 'Fresh tomatoes');
formData.append('cost', 50);
formData.append('productimage', fileInput.files[0]);
formData.append('fid', farmerId);

fetch('http://localhost:2004/product/addproduct', {
  method: 'POST',
  body: formData  // No Content-Type header needed
});
```

### 4. Cart Management
```javascript
// Add to cart
fetch('http://localhost:2004/cart/add', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    buyer: { id: buyerId },
    product: { id: productId },
    quantity: 1
  })
});

// Get cart
const cart = await fetch(`http://localhost:2004/cart/buyer/${buyerId}`)
  .then(r => r.json());

// Update cart count badge
const count = await fetch(`http://localhost:2004/cart/count/${buyerId}`)
  .then(r => r.json());
document.getElementById('cartBadge').textContent = count;
```

### 5. Payment Flow (Razorpay)
```javascript
// Step 1: Create order
const orderData = await fetch('http://localhost:2004/payment/create-order', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({ buyerId, addressId })
}).then(r => r.json());

// Step 2: Open Razorpay
const options = {
  key: orderData.key,
  amount: orderData.amount * 100,
  currency: orderData.currency,
  order_id: orderData.orderId,
  handler: async function(response) {
    // Step 3: Verify payment
    await fetch('http://localhost:2004/payment/verify-payment', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        razorpay_order_id: response.razorpay_order_id,
        razorpay_payment_id: response.razorpay_payment_id,
        razorpay_signature: response.razorpay_signature,
        buyerId,
        addressId
      })
    });
  }
};
new Razorpay(options).open();
```

### 6. Dashboard Analytics
```javascript
// Get farmer stats
const stats = await Promise.all([
  fetch(`http://localhost:2004/farmer/${farmerId}/products/count`).then(r => r.json()),
  fetch(`http://localhost:2004/farmer/${farmerId}/orders/count`).then(r => r.json()),
  fetch(`http://localhost:2004/farmer/${farmerId}/revenue`).then(r => r.json())
]);

document.getElementById('products').textContent = stats[0].totalProducts;
document.getElementById('orders').textContent = stats[1].totalOrders;
document.getElementById('revenue').textContent = `₹${stats[2].totalRevenue}`;

// Get sales chart data
const salesData = await fetch(
  `http://localhost:2004/farmer/${farmerId}/sales-data?period=daily`
).then(r => r.json());

// salesData = [{ date: "2025-11-26", totalSales: 5000, orderCount: 15 }, ...]
```

---

## 📝 Request Body Templates

### Farmer Registration
```json
{
  "name": "Farmer Name",
  "email": "farmer@example.com",
  "username": "farmername",
  "password": "password123",
  "mobileno": "9876543210",
  "nationalidno": "AADH123456",
  "location": "Punjab, India"
}
```

### Buyer Registration
```json
{
  "name": "Buyer Name",
  "email": "buyer@example.com",
  "password": "password123",
  "mobileno": "9876543211"
}
```

### Login (Farmer)
```json
{
  "username": "farmername",
  "password": "password123"
}
```

### Login (Buyer)
```json
{
  "email": "buyer@example.com",
  "password": "password123"
}
```

### Add to Cart
```json
{
  "buyer": { "id": 1 },
  "product": { "id": 5 },
  "quantity": 2
}
```

### Add Address
```json
{
  "houseNumber": "Building 5A",
  "street": "123 Main Street",
  "city": "Delhi",
  "state": "Delhi",
  "pincode": "110001"
}
```

### Create Payment Order
```json
{
  "buyerId": 1,
  "addressId": 1
}
```

---

## ⚠️ Common Mistakes to Avoid

❌ **DON'T:**
```javascript
// Wrong - Setting Content-Type for multipart/form-data
fetch('http://localhost:2004/product/addproduct', {
  method: 'POST',
  headers: { 'Content-Type': 'multipart/form-data' }, // ❌ WRONG
  body: formData
});

// Wrong - Not storing user data
const response = await loginAPI();
// ❌ Not saving to localStorage

// Wrong - Image URL without parameter
<img src="http://localhost:2004/product/displayproductimage/1" /> // ❌
```

✅ **DO:**
```javascript
// Correct - Let browser set Content-Type
fetch('http://localhost:2004/product/addproduct', {
  method: 'POST',
  body: formData  // ✅ No Content-Type header
});

// Correct - Store user data
const user = await response.json();
localStorage.setItem('farmer', JSON.stringify(user)); // ✅

// Correct - Image URL with query parameter
<img src="http://localhost:2004/product/displayproductimage?id=1" /> // ✅
```

---

## 🔒 LocalStorage Keys

```javascript
// Store after login
localStorage.setItem('farmer', JSON.stringify(farmerObject));
localStorage.setItem('farmerId', farmer.id);

localStorage.setItem('buyer', JSON.stringify(buyerObject));
localStorage.setItem('buyerId', buyer.id);

localStorage.setItem('admin', JSON.stringify(adminObject));

// Retrieve
const farmer = JSON.parse(localStorage.getItem('farmer'));
const farmerId = localStorage.getItem('farmerId');

// Check if logged in
if (!localStorage.getItem('farmer')) {
  window.location.href = '/farmer-login';
}

// Logout
localStorage.removeItem('farmer');
localStorage.removeItem('farmerId');
```

---

## 🎨 Response Status Codes

| Code | Meaning | Action |
|------|---------|--------|
| 200 | Success | Process data |
| 400 | Bad Request | Show error message |
| 401 | Unauthorized | Redirect to login |
| 404 | Not Found | Show "not found" |
| 500 | Server Error | Show "try again" |

---

## 🧪 Quick Test Commands (cURL)

```bash
# Test if server is running
curl http://localhost:2004/buyer/

# Register farmer
curl -X POST http://localhost:2004/farmer/registration \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@test.com","username":"test","password":"test123","mobileno":"9876543210","nationalidno":"AADH123","location":"Test"}'

# View all products
curl http://localhost:2004/product/viewallproducts

# Get cart count
curl http://localhost:2004/cart/count/1
```

---

## 📊 Database Tables

```
farmer_table    - Farmers (status: Pending/Approved/Rejected)
buyer_table     - Buyers
admin_table     - Admins
product_table   - Products (with BLOB images)
cart_table      - Shopping cart items
order_table     - Orders (with Razorpay IDs)
address_table   - Delivery addresses
```

---

## 🎯 Frontend Page Structure

```
/farmer-register      → POST /farmer/registration
/farmer-login         → POST /farmer/checkfarmerlogin
/farmer-dashboard     → GET /farmer/{id}/products/count, orders/count, revenue
/farmer-products      → GET /product/viewproductsbyfarmer/{fid}
/add-product          → POST /product/addproduct
/farmer-orders        → GET /order/farmer/{farmerId}

/buyer-register       → POST /buyer/registration
/buyer-login          → POST /buyer/checkbuyerlogin
/products             → GET /product/viewallproducts
/cart                 → GET /cart/buyer/{buyerId}
/checkout             → POST /payment/create-order
/my-orders            → GET /order/buyer/{buyerId}

/admin-login          → POST /admin/checkadminlogin
/admin-dashboard      → GET /admin/farmers/count, /buyers/count, etc.
/approve-farmers      → POST /admin/approvefarmer/{id}
```

---

## 💡 Pro Tips

1. **Always check response.ok before parsing JSON**
```javascript
if (response.ok) {
  const data = await response.json();
} else {
  const error = await response.text();
  alert(error);
}
```

2. **Use async/await with try-catch**
```javascript
try {
  const response = await fetch(url, options);
  // handle response
} catch (error) {
  console.error('Network error:', error);
}
```

3. **Update cart count after every cart operation**
```javascript
async function updateCartBadge() {
  const buyerId = localStorage.getItem('buyerId');
  const count = await fetch(`http://localhost:2004/cart/count/${buyerId}`)
    .then(r => r.json());
  document.getElementById('cartBadge').textContent = count;
}

// Call after add/remove/clear
updateCartBadge();
```

4. **Handle image loading errors**
```html
<img 
  src="http://localhost:2004/product/displayproductimage?id=${product.id}" 
  onerror="this.src='/placeholder.jpg'"
  alt="${product.name}"
/>
```

5. **Validate before submitting**
```javascript
if (quantity < 1 || quantity > 10) {
  alert('Quantity must be between 1 and 10');
  return;
}
```

---

## 🚀 Ready to Build?

**Complete Documentation:**
- `BACKEND_API_REFERENCE.md` - Full API specs
- `API_TESTING_GUIDE.md` - Testing examples
- `BACKEND_CODE_REVIEW.md` - Code quality report
- `QUICK_API_REFERENCE.md` - This file (quick guide)

**All 49 APIs Tested & Working ✅**

**Start Building Your Frontend Now! 🎉**

---

**Last Updated:** November 26, 2025  
**Backend Version:** 1.0 Production Ready
