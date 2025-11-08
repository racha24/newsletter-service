# Newsletter Service - Submission Document

## Project Information

**Project Name:** Newsletter Service
**GitHub Repository:** https://github.com/racha24/newsletter-service
**Deployed Application:** https://newsletter-service-production-7510.up.railway.app
**Deployment Platform:** Railway.app
**Tech Stack:** Java 17, Spring Boot 3.2.0, PostgreSQL, Maven

---

## Overview

A production-ready Spring Boot application that sends scheduled newsletter content to subscribers based on their topic preferences. The service provides a complete RESTful API for managing topics, subscribers, and scheduled content with automatic email delivery.

---

## Features Implemented

### ✅ Core Requirements

1. **Topic Management**
   - Create, read, update, and delete topics
   - Topics serve as categories for content segregation
   - Each topic can have multiple subscribers and content

2. **Subscriber Management**
   - Add subscriber email IDs with names
   - Subscribe to specific topics
   - Active/inactive status management
   - Unsubscribe functionality
   - Unique constraint on (email, topic) combination

3. **Content Management**
   - Create content with subject and body text
   - Schedule content for specific times
   - Content belongs to a specific topic
   - Status tracking (SCHEDULED, SENT, FAILED, CANCELLED)

4. **Automated Email Delivery**
   - Scheduled job runs every minute
   - Automatically sends content to subscribers at scheduled time
   - Email logging for tracking delivery status
   - SMTP integration (Gmail/SendGrid compatible)

5. **Email Tracking**
   - Logs every email sent
   - Tracks success/failure status
   - Records error messages for failed deliveries
   - Maintains audit trail

---

## Deployment Details

### Live Application URL
```
https://newsletter-service-production-7510.up.railway.app
```

### Health Check Endpoint
```
GET https://newsletter-service-production-7510.up.railway.app/api/health
```

**Response:**
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "service": "Newsletter Service",
    "status": "UP",
    "timestamp": "2025-11-08T17:11:36.494221672"
  },
  "timestamp": "2025-11-08T17:11:36.494242726"
}
```

---

## API Endpoints

### Base URL
```
https://newsletter-service-production-7510.up.railway.app/api
```

### 1. Topics API

#### Create Topic
```bash
POST /topics
Content-Type: application/json

{
  "name": "Technology",
  "description": "Latest tech news and updates"
}
```

**Example:**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/topics \
  -H "Content-Type: application/json" \
  -d '{"name":"Technology","description":"Latest tech news and updates"}'
```

#### Get All Topics
```bash
GET /topics
```

#### Get Topic by ID
```bash
GET /topics/{id}
```

#### Update Topic
```bash
PUT /topics/{id}
```

#### Delete Topic
```bash
DELETE /topics/{id}
```

---

### 2. Subscribers API

#### Create Subscriber (Subscribe)
```bash
POST /subscribers
Content-Type: application/json

{
  "name": "John Doe",
  "email": "john@example.com",
  "topicId": 1
}
```

**Example:**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","topicId":1}'
```

#### Get All Subscribers
```bash
GET /subscribers
```

#### Get Subscriber by ID
```bash
GET /subscribers/{id}
```

#### Get Subscribers by Topic
```bash
GET /subscribers/topic/{topicId}
```

#### Update Subscriber
```bash
PUT /subscribers/{id}
```

#### Unsubscribe
```bash
PATCH /subscribers/{id}/unsubscribe
```

#### Delete Subscriber
```bash
DELETE /subscribers/{id}
```

---

### 3. Content API

#### Create Content (Schedule Newsletter)
```bash
POST /content
Content-Type: application/json

{
  "subject": "Weekly Tech Digest",
  "body": "Here are this week's top tech stories...",
  "topicId": 1,
  "scheduledTime": "2025-11-09T10:00:00"
}
```

**Example:**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/content \
  -H "Content-Type: application/json" \
  -d '{"subject":"Weekly Tech Digest","body":"Latest tech news","topicId":1,"scheduledTime":"2025-11-09T10:00:00"}'
```

#### Get All Content
```bash
GET /content
```

#### Get Content by ID
```bash
GET /content/{id}
```

#### Get Content by Topic
```bash
GET /content/topic/{topicId}
```

#### Get Content by Status
```bash
GET /content/status/{status}
# status: SCHEDULED, SENT, FAILED, CANCELLED
```

#### Update Content
```bash
PUT /content/{id}
```

#### Cancel Content
```bash
PATCH /content/{id}/cancel
```

#### Send Newsletter Immediately
```bash
POST /content/{id}/send-now
```

#### Delete Content
```bash
DELETE /content/{id}
```

---

### 4. Health Check API

#### Health Check
```bash
GET /health
```

---

## Testing the Service

### Complete Workflow Test

**1. Create a Topic**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/topics \
  -H "Content-Type: application/json" \
  -d '{"name":"Technology","description":"Tech news"}'
```

**2. Add Subscribers**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{"name":"Alice","email":"alice@example.com","topicId":1}'

curl -X POST https://newsletter-service-production-7510.up.railway.app/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com","topicId":1}'
```

**3. Schedule Content**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/content \
  -H "Content-Type: application/json" \
  -d '{"subject":"Weekly Update","body":"Latest news","topicId":1,"scheduledTime":"2025-11-09T10:00:00"}'
```

**4. View All Data**
```bash
# Get all topics
curl https://newsletter-service-production-7510.up.railway.app/api/topics

# Get all subscribers
curl https://newsletter-service-production-7510.up.railway.app/api/subscribers

# Get all content
curl https://newsletter-service-production-7510.up.railway.app/api/content
```

**5. Send Newsletter Immediately (Optional)**
```bash
curl -X POST https://newsletter-service-production-7510.up.railway.app/api/content/1/send-now
```

---

## Architecture

### Technology Stack

- **Backend Framework:** Spring Boot 3.2.0
- **Language:** Java 17
- **Database:** PostgreSQL (Railway hosted)
- **ORM:** Spring Data JPA (Hibernate)
- **Email Service:** Spring Mail with SMTP
- **Scheduler:** Spring Scheduling (@Scheduled)
- **Build Tool:** Maven
- **Deployment:** Railway.app
- **Containerization:** Docker

### Project Structure

```
newsletter-service/
├── src/
│   └── main/
│       ├── java/com/newsletter/
│       │   ├── NewsletterServiceApplication.java
│       │   ├── controller/
│       │   │   ├── TopicController.java
│       │   │   ├── SubscriberController.java
│       │   │   ├── ContentController.java
│       │   │   └── HealthController.java
│       │   ├── service/
│       │   │   ├── TopicService.java
│       │   │   ├── SubscriberService.java
│       │   │   ├── ContentService.java
│       │   │   ├── EmailService.java
│       │   │   └── NewsletterSchedulerService.java
│       │   ├── repository/
│       │   │   ├── TopicRepository.java
│       │   │   ├── SubscriberRepository.java
│       │   │   ├── ContentRepository.java
│       │   │   └── EmailLogRepository.java
│       │   ├── model/
│       │   │   ├── Topic.java
│       │   │   ├── Subscriber.java
│       │   │   ├── Content.java
│       │   │   └── EmailLog.java
│       │   ├── dto/
│       │   │   ├── TopicDTO.java
│       │   │   ├── SubscriberDTO.java
│       │   │   ├── ContentDTO.java
│       │   │   └── ApiResponse.java
│       │   └── exception/
│       │       └── GlobalExceptionHandler.java
│       └── resources/
│           ├── application.properties
│           └── application-prod.properties
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md
└── API_DOCUMENTATION.md
```

### Database Schema

**Topics Table:**
- id (PK)
- name (unique)
- description
- created_at, updated_at

**Subscribers Table:**
- id (PK)
- name
- email
- topic_id (FK)
- active (boolean)
- subscribed_at, updated_at
- UNIQUE(email, topic_id)

**Contents Table:**
- id (PK)
- subject
- body (TEXT)
- topic_id (FK)
- scheduled_time
- status (ENUM)
- sent_at
- created_at, updated_at

**Email Logs Table:**
- id (PK)
- content_id (FK)
- subscriber_id (FK)
- recipient_email
- status (ENUM)
- error_message
- sent_at

---

## Scheduler Details

### Automatic Newsletter Sending

The application runs a scheduled job **every minute** using Spring's `@Scheduled` annotation:

```java
@Scheduled(cron = "0 * * * * *") // Runs every minute
public void sendScheduledNewsletters()
```

**Process:**
1. Queries database for content with `status=SCHEDULED` and `scheduledTime <= now()`
2. For each due content:
   - Fetches all active subscribers for the topic
   - Sends email to each subscriber
   - Logs delivery status (SUCCESS/FAILED)
   - Updates content status to SENT
   - Records sent timestamp

### Email Configuration

The service supports any SMTP provider:
- **Gmail** (with App Password)
- **SendGrid**
- **AWS SES**
- **Mailgun**
- Any custom SMTP server

---

## Key Features & Highlights

### 1. Production-Ready Architecture
- RESTful API design
- Proper error handling with global exception handler
- Input validation using Jakarta Bean Validation
- Transactional operations for data consistency
- Logging for debugging and monitoring

### 2. Database Design
- Normalized schema with proper foreign keys
- Unique constraints to prevent duplicate subscriptions
- Audit timestamps on all entities
- Cascade operations for data integrity
- Indexed fields for query performance

### 3. Email Tracking & Logging
- Every email delivery is logged
- Success/failure tracking
- Error message storage for debugging
- Audit trail for compliance

### 4. Flexible Deployment
- Docker containerization
- Environment-based configuration
- Supports H2 (development) and PostgreSQL (production)
- Easy SMTP provider switching

### 5. Developer Experience
- Comprehensive API documentation
- Postman-compatible endpoints
- Clear error messages
- Detailed README with setup instructions

---

## Improvements & Future Enhancements

### Current Implementation Strengths
✅ Clean architecture with separation of concerns
✅ RESTful API with proper HTTP methods
✅ Comprehensive error handling
✅ Transaction management
✅ Email delivery tracking
✅ Automated scheduling
✅ Database audit trails
✅ Docker support
✅ Production deployment

### Potential Improvements

**1. Security Enhancements**
- Add Spring Security with JWT authentication
- Implement API rate limiting
- Email verification for new subscribers
- Secure unsubscribe tokens

**2. Email Features**
- HTML email templates (currently plain text)
- File attachments support
- Email preview functionality
- Personalized content variables
- Retry mechanism for failed emails

**3. Performance Optimizations**
- Async email sending with message queues (RabbitMQ/Kafka)
- Database connection pooling (HikariCP is already used)
- Caching with Redis for frequent queries
- Pagination for large datasets
- Batch email processing

**4. Monitoring & Observability**
- Prometheus metrics integration
- ELK stack for centralized logging
- Email delivery analytics dashboard
- Performance monitoring with APM tools

**5. Additional Features**
- Multiple email templates
- A/B testing for content
- Subscriber preferences (frequency, time zones)
- Content draft system
- Email open and click tracking
- Multi-language support
- Webhook notifications

**6. Testing**
- Unit tests with JUnit 5 and Mockito
- Integration tests for APIs
- Mock email testing with GreenMail
- API documentation with Swagger/OpenAPI

**7. DevOps**
- CI/CD pipeline with GitHub Actions
- Kubernetes deployment manifests
- Infrastructure as Code (Terraform)
- Automated database migrations
- Blue-green deployments

---

## Known Limitations

### 1. Email Rate Limits
**Issue:** Gmail free accounts have sending limits (500 emails/day)
**Solution:** Use SendGrid (100/day free), AWS SES, or Mailgun for production

### 2. Scheduler Precision
**Issue:** Cron runs every minute, not suitable for second-level precision
**Solution:** For high-precision needs, implement Quartz Scheduler

### 3. Concurrent Email Sending
**Issue:** Large subscriber lists may cause delays
**Solution:** Implement async processing with message queues

### 4. Email Templates
**Issue:** Currently sends plain text emails
**Solution:** Integrate Thymeleaf or FreeMarker for HTML templates

### 5. Retry Logic
**Issue:** Failed emails are logged but not automatically retried
**Solution:** Implement exponential backoff retry mechanism

### 6. Timezone Support
**Issue:** All times use server timezone
**Solution:** Store user timezone preferences and convert accordingly

---

## Environment Variables

### Required for Production

```bash
# Spring Profile
SPRING_PROFILES_ACTIVE=prod

# Database (Auto-provided by Railway Postgres)
PGHOST=postgres.railway.internal
PGPORT=5432
PGUSER=postgres
PGPASSWORD=<auto-generated>
PGDATABASE=railway

# Email Configuration
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-gmail-app-password
```

### Optional Configuration

```bash
# Server Port (default: 8080)
SERVER_PORT=8080

# Log Level
LOGGING_LEVEL_COM_NEWSLETTER=INFO
```

---

## Setup Instructions

### Local Development

**Prerequisites:**
- Java 17+
- Maven 3.6+
- PostgreSQL (or use H2 for quick start)

**Steps:**
```bash
# Clone repository
git clone https://github.com/racha24/newsletter-service.git
cd newsletter-service

# Run with H2 (in-memory database)
mvn spring-boot:run

# Or run with PostgreSQL
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

**Access:**
- Application: http://localhost:8080
- H2 Console: http://localhost:8080/h2-console

### Docker Deployment

```bash
# Build Docker image
docker build -t newsletter-service .

# Run with docker-compose (includes PostgreSQL)
docker-compose up -d
```

### Railway Deployment

1. Fork/clone repository to GitHub
2. Create Railway account and link GitHub
3. Create new project from GitHub repo
4. Add PostgreSQL database
5. Set environment variables (MAIL_USERNAME, MAIL_PASSWORD)
6. Generate public domain
7. Deploy!

---

## Testing Credentials

For testing email functionality, you need to set up Gmail App Password:

1. Go to Google Account Settings
2. Enable 2-Step Verification
3. Go to Security → App Passwords
4. Generate password for "Mail"
5. Use generated password in `MAIL_PASSWORD` environment variable

**Note:** Never commit credentials to GitHub!

---

## Code Quality & Best Practices

### Followed Practices
- SOLID principles
- Clean Code conventions
- RESTful API design
- Dependency Injection
- Exception handling
- Input validation
- Logging
- Documentation
- Git commit messages

### Code Attribution
Some boilerplate code and best practices were referenced from:
- Spring Boot official documentation
- Baeldung tutorials
- Stack Overflow community solutions

All code has been customized and integrated specifically for this newsletter service use case.

---

## Contact & Support

**GitHub Repository:** https://github.com/racha24/newsletter-service
**Issues:** Please create GitHub issues for bugs or feature requests
**Documentation:** See README.md and API_DOCUMENTATION.md in repository

---

## Submission Checklist

- ✅ Public GitHub repository created
- ✅ Application deployed on Railway.app
- ✅ All core features implemented
- ✅ API endpoints documented
- ✅ README with setup instructions
- ✅ Docker support added
- ✅ Environment variables configured
- ✅ Database schema created
- ✅ Scheduler working (runs every minute)
- ✅ Email integration configured
- ✅ Error handling implemented
- ✅ Regular git commits with proper messages
- ✅ Improvements and limitations documented

---

## Summary

The Newsletter Service is a production-ready Spring Boot application successfully deployed on Railway.app. It provides a complete API for managing topics, subscribers, and scheduled content with automatic email delivery. The application demonstrates enterprise-grade architecture, proper error handling, database design, and deployment practices.

**Live URL:** https://newsletter-service-production-7510.up.railway.app
**GitHub:** https://github.com/racha24/newsletter-service
**Tech Stack:** Java 17, Spring Boot 3.2.0, PostgreSQL, Maven, Docker, Railway.app

Thank you for reviewing this submission!
