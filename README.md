# 🔐 Spring-Boot Authentication Service

A production-style **Spring Boot Authentication Microservice** that provides:

* JWT-based authentication
* Refresh token mechanism
* Email OTP verification
* OAuth2 login (Google & GitHub)
* Secure logout with token revocation

---

# 🚀 Features

## ✅ Authentication

* Login with username & password
* JWT Access Token generation
* Refresh Token support

## ✅ Email OTP Verification

* Send OTP to email
* OTP expiry (5 minutes)
* OTP verification before registration
* Resend OTP support

## ✅ OAuth2 Login

* Google OAuth2
* GitHub OAuth2

## ✅ Security

* Spring Security (stateless)
* JWT validation filter
* Revoked token blacklist

## ✅ Logout

* Access token invalidation
* Refresh token deletion

---

# 🏗️ Tech Stack

* Java 17+
* Spring Boot
* Spring Security
* Spring Data JPA
* MySQL / H2
* JWT (io.jsonwebtoken)
* ModelMapper

---

# 📁 Project Structure

```
com.financetrackerapp
│
├── controller        # REST APIs
├── service           # Business logic
├── repository        # Database layer
├── entity            # JPA entities
├── dto               # Request/Response DTOs
├── security          # JWT, filters, config
```

---

# ⚙️ Setup Instructions

## 1️⃣ Clone Repository

```bash
git clone https://github.com/SatwikDalei/Authentication-Spring-boot-Microservice.git
cd Authentication-Spring-boot-Microservice
```

---

## 2️⃣ Configure Environment Variables

 Use environment variables:

```properties
spring.mail.username=${MAIL_USERNAME}
spring.mail.password=${MAIL_PASSWORD}

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000

spring.security.oauth2.client.registration.google.client-id=${GOOGLE_CLIENT_ID}
spring.security.oauth2.client.registration.google.client-secret=${GOOGLE_CLIENT_SECRET}

spring.security.oauth2.client.registration.github.client-id=${GITHUB_CLIENT_ID}
spring.security.oauth2.client.registration.github.client-secret=${GITHUB_CLIENT_SECRET}
```

---

## 3️⃣ Run Application

```bash
mvn spring-boot:run
```

---

# 📬 API Endpoints

## 🔹 Authentication

### Login

```http
POST /user/authenticate
```

### Register

```http
POST /user/register
```

---

## 🔹 OTP Flow

### Send OTP

```http
POST /user/send-otp
```

Body:

```json
{
  "email": "user@example.com"
}
```

### Verify OTP

```http
POST /user/verify-otp
```

Body:

```json
{
  "email": "user@example.com",
  "otp": "123456"
}
```

---

## 🔹 Token Management

### Refresh Token

```http
POST /user/refresh
```

### Logout

```http
POST /user/logout
```

---

## 🔹 User Info

```http
GET /user/user
```

---

# 🔐 Security Flow

1. User requests OTP
2. OTP sent to email
3. User verifies OTP
4. User registers
5. User logs in → gets JWT + Refresh Token
6. JWT used for protected APIs
7. Refresh token used for new JWT
8. Logout revokes token

---

# 📧 Email Configuration

Supports any SMTP provider:

### Example (Gmail)

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
```

---

# 🧪 Testing (Postman)

1. Send OTP
2. Verify OTP
3. Register user
4. Authenticate
5. Use JWT in Authorization header

```
Authorization: Bearer <token>
```

---

# 🛡️ Best Practices Implemented

* Password encryption (BCrypt)
* Stateless authentication
* Token revocation
* OTP expiry handling
* Secure secret management

---

# 🚀 Future Enhancements

* Integration of kafka or other messaging service for inter microservice communication.
* Redis for token storage
* Email templates with branding
* Role-based authorization
* Docker support

---

# 👨‍💻 Author

Satwik Bodhisatwa Dalei

---

# ⭐ If you like this project

Give it a ⭐ on GitHub!
