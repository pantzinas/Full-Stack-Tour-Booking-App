# Full-Stack-Tour-Booking-App

## Guided Tour Application - Full-stack

A full-stack web application for managing the bookings of guided tours. This app addresses to both:
- **Customers** looking for new guided tour experiences, and
- **Guides** looking for offering their services to potential clients.

The said repository includes both the frontend and the backend codebases.

---

## Technologies Used

### Backend

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Gradle

### Frontend

- React
- Vite
- TypeScript
- React Hook Form + Zod
- TailwindCSS

### Prerequisites

- Java 17 or later
- Node.js and npm
- MySQL
- Gradle

## Build & Run

### Backend

- Navigate to the backend directory:
```bash
cd spring-rest-back-end
```
- Configure database connection in: src/main/resources/application.properties
- Run the backend server:
```bash
./gradlew bootRun
```

### Frontend

- Navigate to the frontend directory: 
```bash
cd react-front-end
```
- Install dependencies in Bash:
```bash
npm install
```
- Create a .env file and set your backend API_URL as follows: VITE_API_URL=http://localhost:8080/api/
- Run the development server:
```bash
npm run dev
```
