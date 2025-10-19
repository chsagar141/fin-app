# 🧾 Spring Boot Backend API Specification

**Base URL:** `http://localhost:8080/api`

---

## A. 🔐 Authentication Endpoints

These endpoints handle user registration and login.

---

### 1. User Signup

**Method:** `POST`  
**URL:** `/auth/signup`  
**Description:** Registers a new user with the application.

#### Headers
| Key | Value |
|------|--------|
| Content-Type | application/json |

#### Request Body
```json
{
  "username": "newuser",
  "email": "newuser@example.com",
  "password": "strongpassword"
}
```

#### ✅ Success Response
**Status:** `201 Created`
```json
{
  "userId": 2,
  "username": "newuser",
  "message": "User registered successfully!"
}
```

#### ❌ Error Responses
**Status:** `400 Bad Request`
```json
{
  "userId": null,
  "username": null,
  "message": "Username already taken!"
}
```
or
```json
{
  "userId": null,
  "username": null,
  "message": "Email already in use!"
}
```

---

### 2. User Login

**Method:** `POST`  
**URL:** `/auth/login`  
**Description:** Authenticates an existing user.  
On successful login, returns the `userId` which must be used in the `X-User-ID` header for subsequent protected requests.

#### Headers
| Key | Value |
|------|--------|
| Content-Type | application/json |

#### Request Body
```json
{
  "username": "testuser",
  "password": "password123"
}
```

#### ✅ Success Response
**Status:** `200 OK`
```json
{
  "userId": 1,
  "username": "testuser",
  "message": "Login successful!"
}
```

#### ❌ Error Response
**Status:** `401 Unauthorized`
```json
{
  "userId": null,
  "username": null,
  "message": "Invalid username or password!"
}
```

---

## B. 💼 Item Management Endpoints

These endpoints allow **authenticated users** to perform CRUD operations on their financial items.  
All endpoints require the `X-User-ID` header.

---

### 1. Get All Items for a User

**Method:** `GET`  
**URL:** `/items`  
**Description:** Retrieves all financial items belonging to the authenticated user.

#### Headers
| Key | Value |
|------|--------|
| X-User-ID | {userId} (e.g., 1) |

#### ✅ Success Response (Items Found)
**Status:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Monthly Rent",
    "price": 1200.00,
    "category": "Housing",
    "dateAdded": "2025-05-01",
    "description": "Apartment rent for May"
  },
  {
    "id": 2,
    "name": "Groceries",
    "price": 350.50,
    "category": "Food",
    "dateAdded": "2025-05-10",
    "description": "Weekly grocery shopping"
  }
]
```

#### ✅ Success Response (No Items Found)
```json
[]
```

#### ❌ Error Response
**Status:** `400 Bad Request`
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/items"
}
```

---

### 2. Get a Specific Item by ID

**Method:** `GET`  
**URL:** `/items/{itemId}`  
**Description:** Retrieves a specific financial item owned by the authenticated user.

#### Headers
| Key | Value |
|------|--------|
| X-User-ID | {userId} |

#### ✅ Success Response
**Status:** `200 OK`
```json
{
  "id": 1,
  "name": "Monthly Rent",
  "price": 1200.00,
  "category": "Housing",
  "dateAdded": "2025-05-01",
  "description": "Apartment rent for May"
}
```

#### ❌ Error Response
**Status:** `404 Not Found`
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "path": "/api/items/1"
}
```

---

### 3. Add a New Item

**Method:** `POST`  
**URL:** `/items`  
**Description:** Adds a new financial item for the authenticated user.  
`dateAdded` can be omitted, and the server will set the current date.

#### Headers
| Key | Value |
|------|--------|
| Content-Type | application/json |
| X-User-ID | {userId} |

#### Request Body
```json
{
  "name": "Gym Membership",
  "price": 45.99,
  "category": "Health",
  "dateAdded": "2025-10-01",
  "description": "Monthly gym fee"
}
```

#### ✅ Success Response
**Status:** `201 Created`
```json
{
  "id": 3,
  "name": "Gym Membership",
  "price": 45.99,
  "category": "Health",
  "dateAdded": "2025-10-01",
  "description": "Monthly gym fee"
}
```

#### ❌ Error Response
**Status:** `400 Bad Request`
```json
{
  "timestamp": "...",
  "status": 400,
  "error": "Bad Request",
  "path": "/api/items"
}
```

---

### 4. Update an Existing Item

**Method:** `PUT`  
**URL:** `/items/{itemId}`  
**Description:** Updates an existing item for the authenticated user.  
The `id` in the URL must match the `id` in the request body.

#### Headers
| Key | Value |
|------|--------|
| Content-Type | application/json |
| X-User-ID | {userId} |

#### Request Body
```json
{
  "id": 1,
  "name": "Monthly Rent (Updated)",
  "price": 1250.00,
  "category": "Housing",
  "dateAdded": "2025-05-01",
  "description": "Updated apartment rent for May"
}
```

#### ✅ Success Response
**Status:** `200 OK`
```json
{
  "id": 1,
  "name": "Monthly Rent (Updated)",
  "price": 1250.00,
  "category": "Housing",
  "dateAdded": "2025-05-01",
  "description": "Updated apartment rent for May"
}
```

#### ❌ Error Responses
**Status:** `404 Not Found`
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "path": "/api/items/1"
}
```

**Status:** `400 Bad Request` (Invalid or missing `X-User-ID` / invalid data)

---

### 5. Delete an Item

**Method:** `DELETE`  
**URL:** `/items/{itemId}`  
**Description:** Deletes a specific item owned by the authenticated user.

#### Headers
| Key | Value |
|------|--------|
| X-User-ID | {userId} |

#### ✅ Success Response
**Status:** `204 No Content`

#### ❌ Error Responses
**Status:** `404 Not Found`
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "path": "/api/items/2"
}
```

**Status:** `400 Bad Request` (Missing or invalid `X-User-ID`)

---

## C. 🤖 AI Recommendation Endpoint

This endpoint triggers the AI service to generate financial recommendations based on the user’s items.

---

### 1. Get AI Recommendation

**Method:** `GET`  
**URL:** `/items/recommendation`  
**Description:** Fetches all items for the user, sends them to the Python AI service, and returns an AI-generated recommendation.

#### Headers
| Key | Value |
|------|--------|
| X-User-ID | {userId} |

#### ✅ Success Response
**Status:** `200 OK`
```json
{
  "recommendation": "Your AI-generated financial advice will appear here. It will analyze your spending patterns and offer tips for saving, budgeting, etc.",
  "generatedAt": "2025-10-10T12:30:00.123456"
}
```

#### ❌ Error Responses
**Status:** `404 Not Found`
```json
{
  "timestamp": "...",
  "status": 404,
  "error": "Not Found",
  "message": "No value present",
  "path": "/api/items/recommendation"
}
```

**Status:** `500 Internal Server Error`
```json
{
  "timestamp": "...",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Error calling Python AI service: Client Error from Python AI Service: 4XX UNPROCESSABLE_ENTITY - {Python service error body}",
  "path": "/api/items/recommendation"
}
```

---

### 🧩 Notes

- `X-User-ID` must be included in all protected endpoints.  
- `dateAdded` is automatically set if omitted.  
- The AI service is expected to return meaningful recommendations based on financial patterns.

---

**Author:** Backend Specification for Spring Boot Financial Tracker  
**Version:** 1.0  
**Last Updated:** 2025-10-14
