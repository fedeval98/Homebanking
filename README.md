# HomebankingAPI

## Description
This homebanking application provides comprehensive management of bank accounts, cards, transfers, and loans, as well as PDF report generation. Users can create up to three bank accounts, manage up to six cards in different categories (Silver, Gold, and Titanium), and perform both internal and external transfers. Password recovery is handled via an email with a token, which expires in 15 minutes.

The application includes an admin panel for loan management and other resources.

## Technologies

#### Back-End
- **Language:** Java 17
- **Framework:** Spring Boot 3.2.0
- **Build Tool:** Gradle
- **Database:** PostgreSQL

#### Front-End
- **Languages:** HTML, CSS, JavaScript
- **Frameworks:** Vue.js, Tailwind

- **Containers:** Docker is used to create a program image.

## Dependencies

- **Spring Security:** User management and security (without JWT or OAuth).
- **Spring Web:** REST API creation.
- **Spring Data JPA:** Server and database connection.
- **Apache PDFBox:** PDF generation for account summaries.
- **Spring Mail:** Email sending for password recovery.
- **JUnit:** System unit testing.
- **Axios:** Client-server requests in the Front-End.

## Features

- **Account Management:** Each user can create up to three accounts, delete, or create them again as long as the active limit is not exceeded.
- **Password Recovery:** Users can request an email with a recovery token, valid for 15 minutes. This token is invalidated after use or if it expires without being used.
- **Card Management:** Users can request up to six cards, with one credit and one debit card for each category: Silver, Gold, and Titanium.
- **Transfers:** Users can transfer funds between their accounts or to external accounts.
- **Loans:** Request for loans with monthly installment payments.
- **Transaction Report:** Generation of a PDF with account transactions within a specified date range.
- **Admin Panel:** Loan creation and management by administrators.

## Endpoints

### Administrators

#### GET
- `GET /api/clients` - Retrieves a list of all clients.
- `GET /api/clients/{id}` - Retrieves information of a specific client by ID.

#### POST
- `POST /api/loans/create` - Allows the admin to create a new loan.

---

### Users

#### GET
- `GET /api/clients/current` - Retrieves information of the logged-in user.
- `GET /api/accounts/{id}` - Retrieves information of an account belonging to the authenticated user.
- `GET /api/accounts/{id}/transactions` - Retrieves transactions of the specified account.
- `GET /api/accounts/{id}/transactions/pdf` - Generates a PDF with transaction summaries.
- `GET /api/clients/current/cards` - Retrieves the list of cards for the logged-in user.
- `GET /api/loans` - Retrieves a list of loans available to the user.

#### POST
- `POST /api/clients` - Sends data to create a new user.
- `POST /api/clients/emailSend` - Sends token for password recovery.
- `POST /api/clients/passwordRecovery` - Allows password change using the received token.
- `POST /api/clients/current/accounts` - Creates a new bank account (maximum of 3 per user).
- `POST /api/clients/current/accounts/first` - Creates the initial account for a new client.
- `POST /api/clients/current/cards` - Creates a new card for the logged-in user.
- `POST /api/cards/payments` - Allows payments with a credit or debit card.
- `POST /api/loans` - Allows the user to apply for a loan.
- `POST /api/transactions` - Makes a transaction between accounts.

#### PATCH
- `PATCH /api/clients/current/account/remove` - Hides an account from the user without deleting it from the database.
- `PATCH /api/clients/current/cards/remove` - Hides a card from the user without deleting it from the database.

---

## JSON Format for User Creation
```json
{
  "firstName": "Name",
  "lastName": "Lastname",
  "email": "your@mail",
  "password": "password",
  "type": "account_type"
}
```

## Security

This application uses Spring Security for authenticating users. For this reason, login information can only be sent as URL parameters in the following format: `/api/login?email=your@email.com&password=yourpassword`.

## Future Plans

I'm working on a new user interface using Vue CLI instead of the Vue CDN with templates inserted in HTML. Additionally, I'm looking to use Vue Router to create a Single Page Application (SPA) to streamline URL navigation for users. Instead of navigating through multiple HTML files (e.g., `localhost/web/pages/accounts.html`), I aim for cleaner URLs, like `localhost/accounts`.

You can also see my planned additions for the application by following this Notion link [clicking here](https://shadow-parka-4f4.notion.site/35c9305029054d14add7a4c17b4959e5?v=a06d115ec3c9456ab0f0d897800e0679).

## Installation

Clone this repository:

```bash
git clone https://github.com/yourusername/HomebankingAPI.git
```

Navigate to the project directory:

```bash
cd homebanking
```

Build the project with Gradle:
```bash
./gradlew clean build
```
If you are using Windows, use gradlew.bat instead of ./gradlew.

## License
This project is licensed under the MIT License.

## 
>[!NOTE]
>Los usuarios autenticados son:
>
>Melba
>usuario: melba@mindhub.com
>contraseña: melba123456
>
>Fede
>usuario: fedeval@gmail.com
>constraseña: Fede123456!
>
>Admin
>usuario: admin@admin.com
>contraseña: admin
