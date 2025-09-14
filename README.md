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
- React Router
- React Hook Form + Zod (for form validation)
- TailwindCSS (+ tailwind-merge for class management)
- JWT decode (handling tokens)
- Sonner (toast notifications)
- Vite (build tool)
- TypeScript

### Prerequisites

- Java 17 or later
- Node.js and npm
- MySQL
- Gradle

---

## Backend API Documentation

The backend API is documented using **Swagger UI**.

Once the backend server is running, you can access the API documentation at: [Link Text](http://localhost:8080/swagger-ui.html)

---

## Build & Run

### Backend

- Navigate to the backend directory:
```bash
cd spring-rest-back-end
```
- Configure database connection in:
```bash
src/main/resources/application.properties
```
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
- Create a .env file and set your backend API_URL as follows:
```bash
VITE_API_URL=http://localhost:8080/api/
```
- Run the development server:
```bash
npm run dev
```
