# Field Sales Management Software (Field Sales Tracking & Reporting App)

> [!NOTE]
> **Internship Project**: This repository was developed as part of an internship project to design and implement a robust field sales tracking and reporting system.

A modern, robust web application built using **Java 21** and **Spring Boot** to enable field sales scouts, managers, and administrators to track client site visits, manage schedules, and analyze daily field sales performance.

---

## 🚀 Key Features

*   **Secure Authentication & RBAC**: Implementation of Spring Security with Role-Based Access Control (`ADMIN`, `MANAGER`, `EMPLOYEE`). Offers custom local credentials login as well as **OAuth2 (Google login)** integration.
*   **Visit Records Tracker**: Field sales scouts can document detailed client visits, logging parameters such as:
    *   Company information & contact person details.
    *   Visit type (New vs. Revisit).
    *   Nature of business & current software systems in use (Tally, computer setup, serial numbers, etc.).
    *   Sales opportunities identified (New license, Upgrade, TSS, Service, Customization).
    *   Whether a demo was agreed upon, and scheduling revisit dates.
    *   Support for adding dynamic custom fields to visit records.
*   **Automated Revisit Reminders**: An active background scheduler (`ReminderScheduler`) runs a daily cron job to scan scheduled revisit dates and send automated email alerts to the designated scout prior to the revisit.
*   **Daily Analytics & Synchronization**: Automatic aggregation and synchronization of visit records to generate daily reports (`DailyRecord`) for each scout.
*   **Comprehensive Admin Panel**: Administrators have restricted access to:
    *   Monitor all registered employees.
    *   Modify roles or remove employees.
    *   Search and filter across all visit records and aggregated daily stats.

---

## 🛠️ Technology Stack

*   **Backend**: Java 21, Spring Boot (3.5.x), Spring Security, Spring Data JPA, OAuth2 Client, Spring Mail.
*   **Frontend**: Thymeleaf template engine, HTML5, Vanilla JavaScript, Tailwind CSS.
*   **Database**: MySQL.
*   **Environment Configuration**: Environment variables loaded dynamically via `spring-dotenv`.

---

## ⚙️ Configuration & Installation

### 1. Prerequisites
*   **Java Development Kit (JDK) 21** or higher.
*   **Maven** installed locally.
*   **MySQL Server** running.

### 2. Environment Setup
Create a `.env` file in the `SalesManagementSoftware` sub-folder (containing the `pom.xml` file) with the following structure:

```env
APP_NAME=crm
BASE_URL=http://localhost:8080
APP_PROFILE=dev
PROVIDER=127.0.0.1
DB_PORT=3306
DB_NAME=sms
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
DDL_AUTO=update
SHOW=true
EMAIL=your_email@gmail.com
EMAIL_PASSWORD=your_email_app_password
CLIENT=your_google_oauth_client_id
CLIENT_SECRET=your_google_oauth_client_secret
```

### 3. Database Initialisation
Create a MySQL database matching the name in your `.env` file:
```sql
CREATE DATABASE sms;
```

### 4. Running the Application
From the `SalesManagementSoftware` folder containing the project files, execute:
```bash
./mvnw spring-boot:run
```

*Note: On startup, a default admin account is automatically seeded:*
*   **Username**: `admin@gmail.com`
*   **Password**: `admin`

---

## 📁 Repository Structure

```
├── SalesManagementSoftware/      # Main Spring Boot project directory
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/             # Source files (Controllers, Entities, Repositories, Services)
│   │   │   └── resources/        # Static assets, properties, and Thymeleaf templates
│   │   └── test/                 # Test suites
│   ├── pom.xml                   # Maven dependencies & build settings
│   └── .env                      # Application credentials and config (ignored by git)
└── README.md                     # Project documentation
```

---

## 🛡️ Security Mapping & Roles
*   `/user/allEmployees` - `ADMIN` only.
*   `/user/visit` & `/user/visit/allReports` - `ADMIN` only.
*   `/user/visit/allDailyReports` - `ADMIN` only.
*   `/user/editRole/**` & `/user/deleteEmployee/**` - `ADMIN` only.
*   `/user/**` - Any Authenticated User.
