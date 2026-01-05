# EventMate – AI Based Event Planner and Scheduler (Customized Version)

## Project Ownership

This project is a customized and extended implementation developed by  
**Arun Kumar** as part of an internship project.

Original base concept has been adapted with:
- Custom branding and UI updates
- Modified frontend workflows
- Personalized configuration and documentation
- Independent deployment and testing

GitHub Profile: https://github.com/ArunKalmood


EventMate is a full‑stack, AI‑assisted event management platform designed to streamline event discovery, booking, and organizer operations. The system is built with a clear separation between attendee and organizer workflows, combining rule‑based logic with AI‑driven ranking to improve relevance, safety, and scalability.

This project focuses on real‑world architecture patterns, secure configuration practices, and practical AI integration rather than superficial automation.

---

## Overview

EventMate enables users to discover and book events while providing organizers with tools to create, manage, and monitor events effectively. The platform integrates smart capacity rules, AI‑based recommendations, payment handling, and lost‑and‑found workflows commonly required in real event ecosystems.

---

## Key Features

### Attendee Capabilities

* Event discovery and listing
* Detailed event view with capacity and status
* Event booking and confirmation
* Booking history tracking
* Lost item reporting and tracking
* Personalized AI‑curated event recommendations

### Organizer Capabilities

* Organizer onboarding flow
* Event creation and management
* Organizer dashboard
* Booking overview per event
* Event‑level lost and found management
* Review and notification handling

### AI and Smart Logic

* AI‑based event ranking using configurable models
* Hybrid recommendation system:

  * Rule‑based filtering and safety constraints
  * AI‑driven relevance ranking
* Smart capacity thresholds
* Booking velocity monitoring to reduce overcrowding

---

## Technology Stack

### Frontend

* React (Vite)
* JavaScript
* Tailwind CSS
* Axios

### Backend

* Spring Boot (Java)
* Spring Security
* RESTful APIs
* Maven

### Data and Infrastructure

* MySQL
* Redis (rule evaluation and performance support)
* Cloudinary and firebase Storage (media handling)

### AI and Payments

* Groq AI (configurable LLM models)
* PayPal Sandbox integration
* QR‑based ticket validation

---

## Project Structure

```
EventMate_AI_Based_Event_Management_System/
│
├── backend/
│   ├── src/main/java/com/springboard/eventmate/
│   │   ├── controller/
│   │   ├── model/
│   │   ├── service/
│   │   └── EventmateApplication.java
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── pom.xml
│   └── mvnw
│
├── frontend/
│   ├── src/
│   │   ├── pages/
│   │   ├── services/
│   │   ├── layouts/
│   │   └── components/
│   ├── public/
│   ├── package.json
│   └── vite.config.js
│
├── README.md
└── .gitignore
```

---

## Backend Architecture

### Entry Point

* `EventmateApplication.java` — Spring Boot application entry

### Controllers

The backend exposes REST APIs through the following controllers:

* Authentication and access control
* Event discovery and details
* Booking and confirmation
* Organizer event management
* Payment processing (PayPal sandbox)
* Ticket validation (QR‑based)
* Lost and found workflows
* Notifications and reviews

This controller‑driven structure ensures modularity and separation of concerns.

---

## Frontend Architecture

### Pages

#### Attendee Pages

* Home
* Events List
* Event Details
* Curated Events
* Login
* Booking Confirmation
* My Bookings
* My Lost Items
* Report Lost Item

#### Organizer Pages

* Organizer Onboarding
* Organizer Home
* Create Event
* Organizer Dashboard
* Organizer Bookings
* Organizer Lost and Found
* Event‑specific Lost Items

### Service Layer

Frontend communication with backend APIs is handled through dedicated service modules:

* API configuration
* Event services
* Organizer services
* AI recommendation services
* AI chat interaction endpoints

---

## Configuration and Security

Configuration values are externalized and managed securely.

### application.properties

The repository contains only placeholder values:

```
groq.api.key=YOUR_GROQ_API_KEY
paypal.client.id=YOUR_PAYMENT_GATEWAY_CLIENT_ID
paypal.client.secret=YOUR_PAYMENT_GATEWAY_CLIENT_SECRET
```

### Local Configuration

Real secrets are stored in `application-local.properties`, which is excluded from version control. This ensures:

* No secrets are committed
* GitHub secret scanning compliance
* Safe collaboration and deployment

---

## Running the Project Locally

### Backend

```
cd backend
./mvnw spring-boot:run
```

The backend runs on:

```
http://localhost:8080
```

### Frontend

```
cd frontend
npm install
npm run dev
```

The frontend runs on:

```
http://localhost:5173
```

---

## Design Principles

* Clear separation of attendee and organizer workflows
* Minimal and purposeful AI usage
* Rule‑based safety for capacity and booking control
* Modular frontend and controller‑based backend
* Security‑first configuration management

---

## Project Status

* Status: Actively developed
* Scope: Full‑stack internship and academic project
* Focus: Clean architecture, real‑world patterns, and practical AI integration

---
## Internship Details

- Developer: Arun Kumar  
- Project Type: Full-Stack Web Application  
- Purpose: Internship Submission  
- Institution: Infoys Springboard Virtual Internship 6.0


This repository represents my personal implementation and learning
of full-stack architecture, Spring Boot REST APIs, and React-based UI.


## License

This project is intended for educational and demonstration purposes.
