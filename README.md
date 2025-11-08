# Newsletter Service

A scalable Spring Boot application that sends scheduled newsletter content to subscribers based on their topic preferences.

## Overview

This service provides a complete newsletter management system with:
- Topic-based subscription management
- Scheduled content delivery
- Email tracking and logging
- RESTful API for all operations
- Automatic newsletter sending via cron scheduler

## Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 17
- **Database**: PostgreSQL (Production) / H2 (Development)
- **ORM**: Spring Data JPA (Hibernate)
- **Email**: Spring Mail with SMTP
- **Scheduler**: Spring Scheduling
- **Build Tool**: Maven
- **Additional**: Lombok, Jakarta Validation

## Architecture

```
┌─────────────┐     ┌──────────────┐     ┌──────────────┐
│   Client    │────▶│  REST API    │────▶│   Service    │
│  (Postman)  │     │ (Controllers)│     │    Layer     │
└─────────────┘     └──────────────┘     └──────────────┘
                                                 │
                    ┌──────────────┐     ┌──────▼──────┐
                    │  Scheduler   │────▶│ Repository  │
                    │   (Cron)     │     │    Layer    │
                    └──────────────┘     └──────┬──────┘
                                                 │
                    ┌──────────────┐     ┌──────▼──────┐
                    │ Email Service│◀────│  Database   │
                    │   (SMTP)     │     │(PostgreSQL) │
                    └──────────────┘     └─────────────┘
```

## Database Schema

### Topics
- `id` (PK)
- `name` (unique)
- `description`
- `created_at`, `updated_at`

### Subscribers
- `id` (PK)
- `name`
- `email`
- `topic_id` (FK to Topics)
- `active`
- `subscribed_at`, `updated_at`
- Unique constraint on (email, topic_id)

### Contents
- `id` (PK)
- `subject`
- `body` (TEXT)
- `topic_id` (FK to Topics)
- `scheduled_time`
- `status` (SCHEDULED, SENT, FAILED, CANCELLED)
- `sent_at`
- `created_at`, `updated_at`

### Email Logs
- `id` (PK)
- `content_id` (FK to Contents)
- `subscriber_id` (FK to Subscribers)
- `recipient_email`
- `status` (SUCCESS, FAILED, RETRY)
- `error_message`
- `sent_at`

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (for production) or use embedded H2 (development)
- Gmail account with App Password (for SMTP)

## Setup Instructions

### 1. Clone the Repository

```bash
git clone <repository-url>
cd newsletter-service
```

### 2. Configure Email Settings

Update `src/main/resources/application.properties`:

```properties
# Gmail SMTP Configuration
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

**To get Gmail App Password:**
1. Go to Google Account Settings
2. Security → 2-Step Verification (enable if not enabled)
3. App Passwords → Generate new password
4. Copy the 16-character password

### 3. Run with H2 Database (Development)

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

H2 Console: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:newsletterdb`
- Username: `sa`
- Password: (leave empty)

### 4. Run with PostgreSQL (Production)

Create database:
```sql
CREATE DATABASE newsletterdb;
```

Update `src/main/resources/application-prod.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/newsletterdb
spring.datasource.username=your_username
spring.datasource.password=your_password
```

Run with production profile:
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

## API Endpoints

### Health Check
```http
GET /api/health
```

### Topics

```http
# Create a topic
POST /api/topics
Content-Type: application/json
{
  "name": "Technology",
  "description": "Tech news and updates"
}

# Get all topics
GET /api/topics

# Get topic by ID
GET /api/topics/{id}

# Update topic
PUT /api/topics/{id}

# Delete topic
DELETE /api/topics/{id}
```

### Subscribers

```http
# Subscribe to a topic
POST /api/subscribers
Content-Type: application/json
{
  "name": "John Doe",
  "email": "john@example.com",
  "topicId": 1
}

# Get all subscribers
GET /api/subscribers

# Get subscriber by ID
GET /api/subscribers/{id}

# Get subscribers by topic
GET /api/subscribers/topic/{topicId}

# Update subscriber
PUT /api/subscribers/{id}

# Unsubscribe
PATCH /api/subscribers/{id}/unsubscribe

# Delete subscriber
DELETE /api/subscribers/{id}
```

### Content

```http
# Create content (scheduled newsletter)
POST /api/content
Content-Type: application/json
{
  "subject": "Weekly Tech Update",
  "body": "Here are this week's top tech stories...",
  "topicId": 1,
  "scheduledTime": "2025-11-09T10:00:00"
}

# Get all content
GET /api/content

# Get content by ID
GET /api/content/{id}

# Get content by topic
GET /api/content/topic/{topicId}

# Get content by status
GET /api/content/status/SCHEDULED

# Update content
PUT /api/content/{id}

# Cancel scheduled content
PATCH /api/content/{id}/cancel

# Send newsletter immediately
POST /api/content/{id}/send-now

# Delete content
DELETE /api/content/{id}
```

## Scheduler

The application runs a scheduled job **every minute** to check for due newsletters and send them automatically.

```java
@Scheduled(cron = "0 * * * * *") // Runs every minute
```

The scheduler:
1. Queries for content with status=SCHEDULED and scheduledTime <= now
2. Fetches active subscribers for each content's topic
3. Sends emails to all subscribers
4. Logs email delivery status
5. Updates content status to SENT

## Testing the Service

### Using Postman

1. **Create a Topic**
   ```
   POST http://localhost:8080/api/topics
   Body: {"name": "Tech News", "description": "Technology updates"}
   ```

2. **Add Subscribers**
   ```
   POST http://localhost:8080/api/subscribers
   Body: {
     "name": "Test User",
     "email": "test@example.com",
     "topicId": 1
   }
   ```

3. **Schedule Content (for immediate delivery)**
   ```
   POST http://localhost:8080/api/content
   Body: {
     "subject": "Test Newsletter",
     "body": "This is a test newsletter",
     "topicId": 1,
     "scheduledTime": "2025-11-08T20:30:00"
   }
   ```

4. **Or Send Immediately**
   ```
   POST http://localhost:8080/api/content/{contentId}/send-now
   ```

### Using cURL

```bash
# Create topic
curl -X POST http://localhost:8080/api/topics \
  -H "Content-Type: application/json" \
  -d '{"name":"Tech News","description":"Technology updates"}'

# Add subscriber
curl -X POST http://localhost:8080/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{"name":"Test User","email":"test@example.com","topicId":1}'

# Create content
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{"subject":"Test","body":"Hello World","topicId":1,"scheduledTime":"2025-11-08T20:30:00"}'
```

## Deployment

### Railway.app (Recommended)

1. Create account at [railway.app](https://railway.app)

2. Install Railway CLI:
   ```bash
   npm i -g @railway/cli
   ```

3. Login and initialize:
   ```bash
   railway login
   railway init
   ```

4. Add PostgreSQL:
   ```bash
   railway add
   # Select PostgreSQL
   ```

5. Set environment variables:
   ```bash
   railway variables set SPRING_PROFILES_ACTIVE=prod
   railway variables set MAIL_USERNAME=your-email@gmail.com
   railway variables set MAIL_PASSWORD=your-app-password
   ```

6. Deploy:
   ```bash
   railway up
   ```

### Render.com

1. Create account at [render.com](https://render.com)

2. Create PostgreSQL database

3. Create new Web Service:
   - Build Command: `mvn clean install`
   - Start Command: `java -jar target/newsletter-service-1.0.0.jar`

4. Add environment variables:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `DATABASE_URL=<postgres-url>`
   - `MAIL_USERNAME=<your-email>`
   - `MAIL_PASSWORD=<your-app-password>`

5. Deploy

## Improvements & Future Enhancements

### Current Implementation
✅ RESTful API with proper validation
✅ Scheduled newsletter sending
✅ Email logging and tracking
✅ Topic-based subscriptions
✅ H2 for quick local development
✅ PostgreSQL support for production
✅ Exception handling
✅ Transactional operations

### Potential Improvements

1. **Security**
   - Add Spring Security with JWT authentication
   - API rate limiting
   - Email verification for subscribers
   - Unsubscribe token generation

2. **Email Enhancements**
   - HTML email templates
   - Attachment support
   - Email preview feature
   - Retry mechanism for failed emails
   - Bulk email optimization

3. **Performance**
   - Async email sending with queues (RabbitMQ/Kafka)
   - Database connection pooling (HikariCP)
   - Caching with Redis
   - Pagination for large datasets

4. **Monitoring & Observability**
   - Prometheus metrics
   - ELK stack for logging
   - Email delivery analytics
   - Dashboard for statistics

5. **Features**
   - Multiple email templates
   - A/B testing for content
   - Subscriber preferences management
   - Timezone-aware scheduling
   - Content draft system
   - Email open tracking
   - Click tracking

6. **Testing**
   - Unit tests with JUnit 5
   - Integration tests
   - Mock email testing
   - API documentation with Swagger/OpenAPI

7. **DevOps**
   - Docker containerization
   - CI/CD pipeline (GitHub Actions)
   - Kubernetes deployment
   - Health checks and monitoring

## Known Limitations

1. **Email Rate Limits**: Gmail has sending limits (500 emails/day for free accounts)
   - Solution: Use SendGrid, AWS SES, or Mailgun for production

2. **Scheduler Precision**: Current cron runs every minute
   - May not be suitable for precise second-level scheduling
   - For high-precision needs, consider Quartz Scheduler

3. **Concurrent Sends**: Large subscriber lists may take time
   - Solution: Implement async processing with message queues

4. **No Email Templates**: Currently sends plain text
   - Solution: Integrate Thymeleaf or FreeMarker for HTML templates

5. **No Retry Logic**: Failed emails are logged but not retried
   - Solution: Implement exponential backoff retry mechanism

## Troubleshooting

### Email not sending
- Check Gmail App Password is correct
- Ensure "Less secure app access" is OFF (use App Password instead)
- Check firewall/network allows SMTP port 587

### Database connection error
- Verify PostgreSQL is running
- Check database credentials
- Ensure database `newsletterdb` exists

### Scheduler not running
- Check `@EnableScheduling` is present on main application class
- Verify cron expression is correct
- Check application logs for errors

## Contributing

This project was built as an assignment. Feel free to fork and enhance!

## License

MIT License

## Contact

For questions or feedback, please reach out via the repository issues.
