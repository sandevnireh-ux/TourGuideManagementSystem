## Features 
- Guide Management (CRUD)
- Tourist Management (CRUD)
- Guide Scheduling
- Tourist Assignments
- Ratings & Reviews
- GPS Tracking (offline map with location points)
- JasperReports (2 reports)
- Email Notifications (Gmail SMTP)

## Technologies Used
- Java Swing (UI)
- MySQL (Database)
- JasperReports (Reporting)
- Gmail SMTP (Email API)
- MVC Architecture

## Database Setup
1. Install MySQL 
2. Create database "tour_guide_db"
3. Update personal MySQL credentials

## Email Notification Setup
- Application sends an automatic email confirmation when a tourist is assigned to a tour.
- To setup:
1. Use a valid Gmail account
2. Generate an App Password 
4. Open file "src/util/EmailUtil.java"
5. Update personal Gmail credentials
6. Update tourist's email in the database to the valid email
7. Assign a tourist to a tour in the Assignments panel to receive notification

Note: No personal Email credentials are included in this repository for security reasons.
