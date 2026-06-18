Automated Viva Examination System

1.Overview

An AI-powered web platform that automates the viva voce examination process using **GPT-3.5-Turbo**. The system generates dynamic questions from PDF study materials and evaluates student responses across **three dimensions**:
- **Semantic Correctness** (60%)
- **Linguistic Confidence** (25%)
- **Completeness** (15%)

**Key Benefits:**
- Eliminates examiner bias
- Provides instant, personalized feedback
- Reduces faculty workload
- Scales to large student populations



2. Tech Stack

| Component | Technology | Version |
|-----------|------------|---------|
| Backend | Spring Boot | 3.2.5 |
| Language | Java | 17 |
| Database | PostgreSQL | 16 |
| Security | Spring Security + JWT | 0.12.5 |
| AI | OpenRouter (GPT-3.5-Turbo) | - |
| PDF Parsing | Apache PDFBox | 2.0.30 |
| Email | JavaMailSender | - |
| Build Tool | Maven | - |
| Frontend | HTML, CSS, JavaScript | - |


3. System Architecture
┌─────────────────┐ ┌─────────────────────┐ ┌─────────────────┐
│ Presentation │────▶│ Business Logic │────▶│ Intelligence │
│ (Frontend) │ │ (Spring Boot) │ │ (GPT-3.5) │
└─────────────────┘ └──────────┬──────────┘ └─────────────────┘
│
▼
┌─────────────────┐
│ Data Tier │
│ (PostgreSQL) │
└─────────────────┘


4. Quick Start

### Prerequisites
- Java 17
- PostgreSQL 16
- Maven
- OpenRouter API Key

### Installation

bash
# 1. Clone
git clone https://github.com/Divya-123-priya/Automated_Viva_Exam.git
cd Automated_Viva_Exam/VivaBot

# 2. Create database
CREATE DATABASE viva_db;

# 3. Configure application.properties
# Add database credentials, OpenRouter API key, email settings

# 4. Build & Run
mvn clean install
mvn spring-boot:run


5. Key Environment Variables
spring.datasource.url=jdbc:postgresql://localhost:5432/viva_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD
openrouter.api.key=YOUR_API_KEY
jwt.secret=YOUR_JWT_SECRET

6. Core Modules

Module	              |        Purpose
Authentication	      |        JWT-based login with role management (Student/Faculty)
Document Processing	  |        PDF text extraction using PDFBox
Question Generation	  |        AI-powered question creation from study material
Response Evaluation	  |        Multi-dimensional scoring (Correctness + Confidence + Completeness)
Analytics Dashboard	  |        Performance visualization for faculty
Email Notification	  |        Automated feedback reports via JavaMailSender

7. API Endpoints

Method	 |   Endpoint	                          |    Description
GET	     |   /api/viva/question	                |    Generate question by subject & difficulty
GET	     |   /api/viva/questionFromMaterial     |    FromMaterial	Generate question from PDF material
POST	   |   /api/viva/answer	                  |    Submit answer & get AI evaluation
POST	   |   /api/auth/login	                  |    User login
POST	   |   /api/auth/register	                |    User registration

