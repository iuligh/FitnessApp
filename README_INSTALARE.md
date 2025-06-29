## Prerequisites

- Java 17 or later
- Node.js 16.x or later
- npm 8.x or later
- MySQL 8.0 or later
- Stripe Account (for payment processing)

## Backend Setup

### 1. Database Configuration

1. Create a new MySQL database for the application:
   ```sql
   CREATE DATABASE gym_management; xx
   ```

2. Update the database configuration in `backend/src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/gym_management?useSSL=false&serverTimezone=UTC
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ``` xxxx

### 2. Stripe Configuration

1. Sign up for a Stripe account at https://dashboard.stripe.com/register
2. Get your API keys (Publishable key and Secret key) from the Stripe Dashboard
3. Add the following to your environment variables or `application.properties`:
   ```properties
   stripe.api.key=your_stripe_secret_key
   stripe.webhook.secret=your_webhook_signing_secret
   app.base.url=http://localhost:4200
   ``` xxxx

### 3. Running the Backend

1. Navigate to the backend directory:
   ```bash
   cd backend
   ``` ?????????

2. Build and run the application:
   ```bash
   ./mvnw spring-boot:run ????????????????
   ```
   
   The backend will start on `http://localhost:8080`

## Frontend Setup

### 1. Install Dependencies

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install the required dependencies:
   ```bash
   npm install
   ```

### 2. Environment Configuration

1. Create a new file `src/environments/environment.ts` with the following content:
   ```typescript
   export const environment = {
     production: false,
     apiUrl: 'http://localhost:8080/api',
     stripePublicKey: 'ypk_test_51Rb6YXH8eUv0d48q82CotuBelIVldSRbFQ3ZdGFbp4ppAgkVriZUAZ2quQVNxn7BYJFjWygD2GBgedQkaFzBFyU000ZiXO5ZVH'
   };
   ``` xxx

### 3. Running the Frontend

1. Start the development server:
   ```bash
   ng serve
   ```

2. Open your browser and navigate to `http://localhost:4200`

## Stripe Webhook Setup (for production)

1. Install the Stripe CLI: https://stripe.com/docs/stripe-cli
2. Log in to your Stripe account:
   ```bash
   stripe login
   ```
3. Forward webhook events to your local server:
   ```bash
   stripe listen --forward-to localhost:8080/api/subscriptions/webhook
   ```
4. The CLI will provide a webhook signing secret - add it to your `application.properties`

## Subscription Plans Setup

1. The application comes with three default subscription plans:
   - **Basic**: Limited gym access (3x/week)
   - **Standard**: Full gym access + group classes
   - **Premium**: All features including personal trainer sessions

2. To create these plans in Stripe:
   - Go to the Stripe Dashboard
   - Navigate to Products > Add Product
   - Create each plan with the appropriate pricing and features
   - Update the `stripePriceId` and `stripeProductId` in the database for each plan

## Testing Payments

1. Use the following test card numbers in Stripe test mode:
   - Success: `4242 4242 4242 4242`
   - Requires authentication: `4000 0025 0000 3155`
   - Decline: `4000 0000 0000 0002`

2. Use any future date for the expiry, any 3 digits for CVC, and any postal code

## Development

### Backend API Endpoints

- `GET /api/subscriptions/plans` - Get all active subscription plans
- `POST /api/subscriptions/checkout/{planId}` - Create a checkout session
- `POST /api/subscriptions/webhook` - Stripe webhook handler

### Frontend Routes

- `/subscription/plans` - View and select subscription plans
- `/subscription/success` - Payment success page
- `/subscription/cancel` - Payment cancellation page

## Deployment

### Backend

1. Build the JAR file:
   ```bash
   ./mvnw clean package -DskipTests
   ```

2. Run the JAR file:
   ```bash
   java -jar target/backend-0.0.1-SNAPSHOT.jar
   ```

### Frontend

1. Build for production:
   ```bash
   ng build --configuration production
   ```

2. Deploy the contents of the `dist/frontend` directory to your web server

## Troubleshooting

- If you get CORS errors, ensure the frontend URL is whitelisted in the backend CORS configuration
- Check the browser's developer console for frontend errors
- Check the backend logs for server-side errors
- Ensure all environment variables are properly set

## License


