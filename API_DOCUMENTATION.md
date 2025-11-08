# API Documentation

## Base URL
```
http://localhost:8080/api
```

## Common Response Format

All API responses follow this format:

```json
{
  "success": true,
  "message": "Operation description",
  "data": { ... },
  "timestamp": "2025-11-08T20:00:00"
}
```

## Error Response Format

```json
{
  "success": false,
  "message": "Error description",
  "data": null,
  "timestamp": "2025-11-08T20:00:00"
}
```

---

## 1. Topics API

### Create Topic
**POST** `/topics`

**Request Body:**
```json
{
  "name": "Technology",
  "description": "Tech news and updates"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Topic created successfully",
  "data": {
    "id": 1,
    "name": "Technology",
    "description": "Tech news and updates"
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

### Get All Topics
**GET** `/topics`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Topics fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "Technology",
      "description": "Tech news and updates"
    },
    {
      "id": 2,
      "name": "Sports",
      "description": "Sports highlights"
    }
  ],
  "timestamp": "2025-11-08T20:00:00"
}
```

### Get Topic by ID
**GET** `/topics/{id}`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Topic fetched successfully",
  "data": {
    "id": 1,
    "name": "Technology",
    "description": "Tech news and updates"
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

### Update Topic
**PUT** `/topics/{id}`

**Request Body:**
```json
{
  "name": "Technology Updates",
  "description": "Latest tech news"
}
```

**Response:** `200 OK`

### Delete Topic
**DELETE** `/topics/{id}`

**Response:** `200 OK`

---

## 2. Subscribers API

### Create Subscriber
**POST** `/subscribers`

**Request Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "topicId": 1
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Subscriber created successfully",
  "data": {
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "topicId": 1,
    "topicName": "Technology",
    "active": true
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

**Validation Rules:**
- `name`: Required, not blank
- `email`: Required, valid email format
- `topicId`: Required, must exist

**Error Example (Duplicate Subscription):**
```json
{
  "success": false,
  "message": "Subscriber already exists for this topic",
  "data": null,
  "timestamp": "2025-11-08T20:00:00"
}
```

### Get All Subscribers
**GET** `/subscribers`

**Response:** `200 OK`

### Get Subscriber by ID
**GET** `/subscribers/{id}`

**Response:** `200 OK`

### Get Subscribers by Topic
**GET** `/subscribers/topic/{topicId}`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Subscribers fetched successfully",
  "data": [
    {
      "id": 1,
      "name": "John Doe",
      "email": "john@example.com",
      "topicId": 1,
      "topicName": "Technology",
      "active": true
    }
  ],
  "timestamp": "2025-11-08T20:00:00"
}
```

### Update Subscriber
**PUT** `/subscribers/{id}`

**Request Body:**
```json
{
  "name": "John Updated",
  "email": "john.new@example.com",
  "topicId": 1,
  "active": true
}
```

**Response:** `200 OK`

### Unsubscribe
**PATCH** `/subscribers/{id}/unsubscribe`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Unsubscribed successfully",
  "data": null,
  "timestamp": "2025-11-08T20:00:00"
}
```

### Delete Subscriber
**DELETE** `/subscribers/{id}`

**Response:** `200 OK`

---

## 3. Content API

### Create Content
**POST** `/content`

**Request Body:**
```json
{
  "subject": "Weekly Tech Update",
  "body": "Here are this week's top tech stories...",
  "topicId": 1,
  "scheduledTime": "2025-11-09T10:00:00"
}
```

**Response:** `201 Created`
```json
{
  "success": true,
  "message": "Content created successfully",
  "data": {
    "id": 1,
    "subject": "Weekly Tech Update",
    "body": "Here are this week's top tech stories...",
    "topicId": 1,
    "topicName": "Technology",
    "scheduledTime": "2025-11-09T10:00:00",
    "status": "SCHEDULED",
    "sentAt": null
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

**Validation Rules:**
- `subject`: Required, not blank
- `body`: Required, not blank
- `topicId`: Required, must exist
- `scheduledTime`: Required, must be in future

**Content Status Values:**
- `SCHEDULED`: Content is scheduled but not sent
- `SENT`: Content has been sent
- `FAILED`: Sending failed
- `CANCELLED`: Content was cancelled

### Get All Content
**GET** `/content`

**Response:** `200 OK`

### Get Content by ID
**GET** `/content/{id}`

**Response:** `200 OK`

### Get Content by Topic
**GET** `/content/topic/{topicId}`

**Response:** `200 OK`

### Get Content by Status
**GET** `/content/status/{status}`

**Path Parameters:**
- `status`: SCHEDULED | SENT | FAILED | CANCELLED

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Content fetched successfully",
  "data": [
    {
      "id": 1,
      "subject": "Weekly Tech Update",
      "body": "Content body...",
      "topicId": 1,
      "topicName": "Technology",
      "scheduledTime": "2025-11-09T10:00:00",
      "status": "SCHEDULED",
      "sentAt": null
    }
  ],
  "timestamp": "2025-11-08T20:00:00"
}
```

### Update Content
**PUT** `/content/{id}`

**Request Body:**
```json
{
  "subject": "Updated Subject",
  "body": "Updated content...",
  "topicId": 1,
  "scheduledTime": "2025-11-09T15:00:00"
}
```

**Response:** `200 OK`

**Note:** Cannot update content that has status=SENT

### Cancel Content
**PATCH** `/content/{id}/cancel`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Content cancelled successfully",
  "data": null,
  "timestamp": "2025-11-08T20:00:00"
}
```

**Note:** Cannot cancel content that has status=SENT

### Send Newsletter Immediately
**POST** `/content/{id}/send-now`

This endpoint triggers immediate sending of the newsletter, bypassing the scheduler.

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Newsletter sent successfully",
  "data": null,
  "timestamp": "2025-11-08T20:00:00"
}
```

**Process:**
1. Validates content exists and is not already sent/cancelled
2. Fetches all active subscribers for the content's topic
3. Sends email to each subscriber
4. Logs each email delivery
5. Updates content status to SENT

### Delete Content
**DELETE** `/content/{id}`

**Response:** `200 OK`

---

## 4. Health Check API

### Health Check
**GET** `/health`

**Response:** `200 OK`
```json
{
  "success": true,
  "message": "Service is healthy",
  "data": {
    "status": "UP",
    "timestamp": "2025-11-08T20:00:00",
    "service": "Newsletter Service"
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

---

## Validation Errors

When validation fails, you'll receive a `400 Bad Request` with detailed field errors:

```json
{
  "success": false,
  "message": "Validation failed",
  "data": {
    "email": "Invalid email format",
    "name": "Name is required"
  },
  "timestamp": "2025-11-08T20:00:00"
}
```

---

## Complete Example Workflow

### 1. Create a Topic
```bash
curl -X POST http://localhost:8080/api/topics \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Technology",
    "description": "Tech news and updates"
  }'
```

### 2. Add Subscribers
```bash
curl -X POST http://localhost:8080/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice",
    "email": "alice@example.com",
    "topicId": 1
  }'

curl -X POST http://localhost:8080/api/subscribers \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Bob",
    "email": "bob@example.com",
    "topicId": 1
  }'
```

### 3. Schedule Content
```bash
curl -X POST http://localhost:8080/api/content \
  -H "Content-Type: application/json" \
  -d '{
    "subject": "This Week in Tech",
    "body": "AI breakthroughs, new gadgets, and more!",
    "topicId": 1,
    "scheduledTime": "2025-11-09T10:00:00"
  }'
```

### 4. Send Immediately (Optional)
```bash
curl -X POST http://localhost:8080/api/content/1/send-now
```

### 5. Check Content Status
```bash
curl http://localhost:8080/api/content/status/SENT
```

---

## Rate Limiting & Best Practices

1. **Email Limits**: Be aware of SMTP provider limits
   - Gmail: 500 emails/day
   - SendGrid Free: 100 emails/day

2. **Batch Operations**: For large subscriber lists, consider:
   - Breaking into smaller batches
   - Using async processing
   - Implementing retry logic

3. **Timezone Handling**: All times are in server timezone
   - Consider converting to UTC
   - Store user timezone preferences

4. **Error Handling**: Always check response status
   - `success: true` = operation succeeded
   - `success: false` = operation failed

---

## Testing with Postman

Import this collection to get started quickly:

1. Create new collection "Newsletter Service"
2. Set base URL variable: `{{base_url}}` = `http://localhost:8080/api`
3. Import the example requests above
4. Test the complete workflow

---

## Database Query Examples

If you need to check data directly in the database:

```sql
-- View all topics
SELECT * FROM topics;

-- View subscribers for a topic
SELECT s.*, t.name as topic_name
FROM subscribers s
JOIN topics t ON s.topic_id = t.id
WHERE t.id = 1 AND s.active = true;

-- View scheduled content
SELECT c.*, t.name as topic_name
FROM contents c
JOIN topics t ON c.topic_id = t.id
WHERE c.status = 'SCHEDULED'
AND c.scheduled_time > NOW();

-- View email logs for a content
SELECT el.*, s.email, s.name, c.subject
FROM email_logs el
JOIN subscribers s ON el.subscriber_id = s.id
JOIN contents c ON el.content_id = c.id
WHERE c.id = 1;
```

---

## Support

For issues or questions, please refer to the main README.md or create an issue in the repository.
