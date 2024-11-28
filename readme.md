# HandyShare

HandyShare is a comprehensive web application designed to facilitate the sharing and renting of household items. Built with a robust backend using Spring Boot MVC and a dynamic frontend with React.js, HandyShare ensures a seamless user experience for both item owners and renters.

## Table of Contents
- [Dependencies](#dependencies)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Build Documentation](#build-documentation)
  - [Backend Build](#backend-build)
  - [Frontend Build](#frontend-build)
  - [Docker](#docker)
- [User Scenarios](#user-scenarios)
  - [1. User Registration and Email Verification](#1-user-registration-and-email-verification)
  - [2. User Authentication and Login](#2-user-authentication-and-login)
  - [3. Product Management](#3-product-management)
  - [4. Payment Processing](#4-payment-processing)
  - [5. Lending Items](#5-lending-items)
- [Contributing](#contributing)
- [License](#license)

## Dependencies

### Backend

HandyShare's backend is built using Spring Boot with the following key dependencies:

**Spring Boot Starters:**
- `spring-boot-starter-data-jpa` - For database interactions using JPA.
- `spring-boot-starter-web` - To build RESTful web services.
- `spring-boot-starter-security` - For implementing security features.
- `spring-boot-starter-mail` - For email functionalities.
- `spring-boot-starter-validation` - To handle input validations.
- `spring-boot-starter-oauth2-client` - For OAuth2 client support.

**Database:**
- `mysql-connector-java` - MySQL database connector.

**Authentication and Security:**
- `jjwt` - For JWT-based authentication.

**Other Libraries:**
- `lombok` - To reduce boilerplate code.
- `firebase-admin` - For integrating Firebase services.
- `stripe-java` - For payment processing with Stripe.

**Testing:**
- `spring-boot-starter-test` - For testing Spring Boot applications.
- `mockito-core` - For mocking in tests.
- `spring-security-test` - For testing security aspects.

### Frontend

The frontend leverages React.js along with several modern libraries:

**UI Libraries:**
- `antd` - Ant Design for streamlined UI components.
- `@fortawesome/fontawesome-free` - For iconography.
- `@radix-ui/react-avatar` - For avatar components.
- `react-icons` - For additional icons.

**State Management and Routing:**
- `react-router-dom` - To handle client-side routing.

**Form and Validation:**
- `react-input-mask` - For input masking in forms.

**HTTP Requests:**
- `axios` - To manage HTTP communications with the backend.

**Date Handling:**
- `react-datepicker` - For date selection components.
- `date-fns` - For date utility functions.
- `dayjs` - For lightweight date manipulation.

**Animation and Effects:**
- `framer-motion` - For animations.
- `react-shimmer-effects` - For loading animations.

**Payment Integration:**
- `@stripe/react-stripe-js` & `@stripe/stripe-js` - For Stripe payment processing.

**Other Libraries:**
- `js-cookie` - For handling cookies.
- `tailwindcss` - For utility-first CSS styling.
- `leaflet` & `react-leaflet` - For interactive maps.
- `lucide-react` - For scalable vector icons.

## Build Documentation

### Backend Build

**Prerequisites:**
- **Java 17** installed.
- **Maven** installed.
- **MySQL** database setup.
- **Firebase** project configured.
- **Stripe** account for payment processing.

**Configuration:**
1. Update the `application.properties` file located at `Backend/src/main/resources/application.properties` with your database credentials, Firebase configurations, and Stripe secret key.
   ```properties
   spring.datasource.url=jdbc:mysql://db-5308.cs.dal.ca:3306/CSCI5308_2_DEVINT
   spring.datasource.username=CSCI5308_2_DEVINT_USER
   spring.datasource.password=Shoh7Aeki9
   STRIPE_SECRET_KEY=your_stripe_secret_key
   spring.security.oauth2.client.registration.google.client-id=your_google_client_id
   spring.security.oauth2.client.registration.google.client-secret=your_google_client_secret
   ```
   
**Building the Project:**
```bash
cd Backend
mvn clean install
```

**Running the Application:**
```bash
mvn spring-boot:run
```
The backend server will start on port `8080`.

### Frontend Build

**Prerequisites:**
- **Node.js** (v14 or higher) and **npm** installed.

**Configuration:**
1. Update any necessary API endpoints in the frontend configuration files if applicable.

**Installing Dependencies:**
```bash
cd Frontend
npm install
```

**Running the Application:**
```bash
npm start
```
The frontend server will start on port `3000`.

### Docker

HandyShare utilizes Docker to containerize both the frontend and backend applications.

**Prerequisites:**
- **Docker** installed on your machine.

**Building Docker Images:**

- **Backend:**
  ```bash
  cd Backend
  mvn clean package
  docker build -t handyshare-backend .
  ```

- **Frontend:**
  ```bash
  cd Frontend
  docker build -t handyshare-frontend .
  ```

**Running Docker Containers:**
```bash
# Run Backend
docker run -d -p 8080:8080 handyshare-backend

# Run Frontend
docker run -d -p 3000:3000 handyshare-frontend
```

## User Scenarios

### 1. User Registration and Email Verification

**Feature**: New users can register by providing their details. Upon registration, an email is sent to verify their account.

**Scenario**:
1. **Registration**: A user navigates to the registration page and fills in their name, email, and password.
2. **Email Verification**: After submitting, the user receives an email with a verification link.
3. **Account Activation**: Clicking the verification link activates the user's account, allowing them to log in.

![User Registration Screenshot](assets/registration_screenshot.png)

### 2. User Authentication and Login

**Feature**: Registered users can log in using their credentials to access personalized features.

**Scenario**:
1. **Login Page**: The user navigates to the login page.
2. **Authentication**: The user enters their email and password.
3. **Access Granted**: Upon successful authentication, the user is redirected to the dashboard.

![User Login Screenshot](assets/login_screenshot.png)

### 3. Product Management

**Feature**: Users can add, view, and delete products they wish to share or rent out.

**Scenario**:
1. **Add Product**: The user navigates to the "Add Product" page, fills in product details, and uploads an image.
2. **View Products**: The user can view a list of all their added products.
3. **Delete Product**: The user can delete any of their products if they no longer wish to share them.

![Add Product Screenshot](assets/add_product_screenshot.png)

### 4. Payment Processing

**Feature**: Users can process payments securely using Stripe integration.

**Scenario**:
1. **Select Product**: The renter selects a product they wish to rent.
2. **Proceed to Payment**: Upon selection, the user proceeds to the payment page.
3. **Complete Payment**: The user enters their payment details and completes the transaction.
4. **Confirmation**: A confirmation message is displayed upon successful payment.

![Payment Screenshot](assets/payment_screenshot.png)

### 5. Lending Items

**Feature**: Users can lend out items and manage lending status.

**Scenario**:
1. **Add Lent Item**: The user adds an item they wish to lend.
2. **View Lent Items**: The user can view all items they are currently lending out.
3. **Manage Status**: The user can update the status of any lent item as needed.

![Lending Items Screenshot](assets/lending_items_screenshot.png)

---

## Contributing

Contributions are welcome! Please fork the repository and create a pull request with your changes. Ensure that your code follows the project's coding standards and includes appropriate tests.

### Steps to Contribute:
1. **Fork the Repository**
2. **Clone your Fork**
   ```bash
   git clone https://github.com/your-username/handyShare.git
   ```
3. **Create a Feature Branch**
   ```bash
   git checkout -b feature/YourFeature
   ```
4. **Commit your Changes**
   ```bash
   git commit -m "Add feature X"
   ```
5. **Push to your Fork**
   ```bash
   git push origin feature/YourFeature
   ```
6. **Open a Pull Request**

