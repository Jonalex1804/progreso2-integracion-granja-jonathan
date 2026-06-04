# Progreso 2 Integracion Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Build the exact minimum Spring Boot, Apache Camel, RabbitMQ, CSV, README, and report deliverables required by the exam PDF for Jonathan Granja.

**Architecture:** A REST controller receives `POST /api/citas`, validates the request, and sends valid appointments to a Camel `direct:` route. Camel writes a billing command to RabbitMQ Point-to-Point, publishes an appointment event to a fanout exchange for Publish/Subscribe, appends a CSV audit row, and records rejected appointments in an error log.

**Tech Stack:** Java 21 target, Spring Boot 3, Apache Camel 4, RabbitMQ, Docker Compose, JUnit 5, Maven.

---

### Task 1: Project Skeleton and Tests

**Files:**
- Create: `pom.xml`
- Create: `src/test/java/edu/udla/integracion/progreso2/service/CitaValidationServiceTest.java`
- Create: `src/test/java/edu/udla/integracion/progreso2/controller/CitaControllerTest.java`

- [x] **Step 1: Write tests first**

Create validation tests for complete, missing, and invalid-value appointments. Create controller tests proving valid requests call Camel and invalid requests return HTTP 400.

- [x] **Step 2: Run tests to verify RED**

Run: `mvn test`
Expected: compilation fails because production classes do not exist yet.

### Task 2: Validation and REST API

**Files:**
- Create: `src/main/java/edu/udla/integracion/progreso2/Progreso2Application.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/model/CitaRequest.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/service/CitaValidationService.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/service/CitaErrorLogger.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/controller/CitaController.java`

- [x] **Step 1: Implement minimal app, model, validation service, error logger, and controller**

The controller accepts JSON at `/api/citas`, logs invalid requests to `data/errors/citas-rechazadas.log`, and sends valid requests to `direct:procesarCita`.

- [x] **Step 2: Run tests to verify GREEN**

Run: `mvn test`
Expected: validation and controller tests pass.

### Task 3: Camel Integration Route

**Files:**
- Create: `src/main/java/edu/udla/integracion/progreso2/model/BillingCommand.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/model/CitaConfirmedEvent.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/routes/CitaIntegrationRoute.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/service/CitaMessageFactory.java`
- Create: `src/main/java/edu/udla/integracion/progreso2/service/CitaCsvFormatter.java`
- Create: `src/main/resources/application.properties`

- [x] **Step 1: Implement route behavior**

The route sends billing command to `billing.queue`, publishes the confirmed event to `appointments.events`, appends CSV to `data/outbox/auditoria-citas.csv`, and logs processing errors.

- [x] **Step 2: Verify tests still pass**

Run: `mvn test`
Expected: all tests pass.

### Task 4: Delivery Files

**Files:**
- Create: `docker-compose.yml`
- Create: `README.md`
- Create: `data/outbox/auditoria-citas.csv`
- Create: `data/errors/citas-rechazadas.log`
- Create: `docs/informe-progreso2-integracion-granja-jonathan.md`

- [x] **Step 1: Add required documentation and initial data files**

README covers student name, technologies, RabbitMQ startup, app execution, endpoint, valid/invalid examples, integration pattern explanations, and evidence checklist.

- [x] **Step 2: Final verification**

Run: `mvn test`
Expected: all tests pass.
