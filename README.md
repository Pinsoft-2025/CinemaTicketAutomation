# 🎬 Cinema Automation System – ERD Overview

This project models the **Entity-Relationship Diagram (ERD)** for a full-featured **Cinema Ticketing and Reservation Automation System**.

---

## 🔗 ERD Schema  
👉 [**View the ER Diagram on Eraser**] (https://app.eraser.io/workspace/J211ZRI3Dm82Nfm37l2l?origin=share)

---

## 📌 Purpose  
This system aims to manage cinema-related operations such as:

- Movie scheduling and sessions (seanses)  
- Hall and seat management  
- User reservations and ticketing  
- Real-time seat status tracking per session  

The ERD is designed to reflect real-world cinema operations with normalization, session-seat linking, and reservation logic.

---

## 🧹 Tech Stack  

- **Java 17**  
- **Spring Boot 3.4.4**  
- **Spring Data JPA**  
- **PostgreSQL**  
- **Lombok**  
- **Maven**  

🔒 *Spring Security*, 📄 *Swagger/OpenAPI*, and 💳 *Payment integration* will be added in the future.  
💻 This is a **backend-only** project – no frontend layer will be implemented.

---

## 🗂️ Entity Descriptions  

### 🔗 Relationships

| Entity        | Relation |
|---------------|----------|
| users         | < reservations.userId |
| reservations  | < tickets.reservationId |
| tickets       | - seansSeats.id |
| movies        | < seanses.movieId |
| halls         | < seats.hallId |
| halls         | < seanses.hallId |
| seats         | < seansSeats.seatId |
| seanses       | < seansSeats.seansId |

### 🧰 Legend

```
<  One-to-Many  
-  One-to-One  
<> Many-to-Many  
```

---

## 🚧 Work in Progress

I will be implementing:

- Database tables  
- Core business logic  
- RESTful endpoints  

Very soon.

---

## 🔮 Upcoming Features

### 🎭 User and Admin Role Separation  
Admins will have permission to manage movies, sessions, and cancellations.

### 🎟️ Ticket Cancellation Flow  

- Users will be able to request ticket cancellations.  
- An email will be sent to the admin upon request.  
- If the admin **accepts**, the ticket will be cancelled and the user will be notified via email.  
- If the admin **rejects**, the ticket remains active and the user will still receive a status email.

### 🧠 Additional Plans  

- Email integration  
- Automatic seat locking & timeout handling

