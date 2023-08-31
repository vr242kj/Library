## Book Library ##
### The service for tracking borrowed books and book readers (borrowers) ###

> How to Run
- Install Docker and Docker Compose:
  If you haven't already, you'll need to install Docker and Docker Compose on your system. You can find installation instructions on the official Docker website.
- Create a Docker Compose File:
  If you already have a Docker Compose file (usually named docker-compose.yml), make sure it's in the directory where you'll be running the Docker Compose commands.
- Navigate to the Directory:
  Open your terminal or command prompt and navigate to the directory containing your docker-compose.yml file.
- Run Docker Compose:
  To run in the background: docker-compose up
- Open http://localhost:8081/book-library/swagger-ui/index.html with Swagger documentation.
- Use collection API from Postman - https://elements.getpostman.com/redirect?entityId=15327265-6cc1306d-08ea-4510-a587-9edaa09d0a57&entityType=collection

> Get information about system health, configurations, etc.
```
http://localhost:8081/book-library/actuator
http://localhost:8081/book-library/actuator/info
http://localhost:8081/book-library/actuator/health
```
> Environment variables
- DEFAULT_MAX_BORROW_TIME_IN_DAYS: 14
- MAX_BOOKS_FOR_BORROW: 5
- MIN_AGE_FOR_RESTRICTED_BOOK: 18
  
