# Book Library 
The service for tracking borrowed books and book readers (borrowers).
## How to Run
1. Install Docker and Docker Compose:
  - If not already installed, please follow the official Docker installation instructions available on the [Docker website](https://www.docker.com/get-started/).
2. Navigate to the Directory:
  - Open your terminal or command prompt and navigate to the directory containing docker-compose.yml [file](https://github.com/vr242kj/Library/blob/master/docker-compose.yml).
3. Run Docker Compose:
  - To run in the background: ```docker compose up```
4. Open [swagger-ui](http://localhost:8081/book-library/swagger-ui/index.html) with Swagger documentation.
5. Use collection API from Postman - [click](https://elements.getpostman.com/redirect?entityId=15327265-6cc1306d-08ea-4510-a587-9edaa09d0a57&entityType=collection)
  - Open collection LibraryService and select Library Environment
## Get information about system health, configurations, etc.
- ```http://localhost:8081/book-library/actuator```
- ```http://localhost:8081/book-library/actuator/info```
- ```http://localhost:8081/book-library/actuator/health```
- ```http://localhost:8081/book-library/monitoring```
## Environment variables
- DEFAULT_MAX_BORROW_TIME_IN_DAYS: 14
- MAX_BOOKS_FOR_BORROW: 5
- MIN_AGE_FOR_RESTRICTED_BOOK: 18
