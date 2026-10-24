# Kongu Engineering College (R2024) Student Academic & Continuous Evaluation System

## Overview
A console‑based Java application that uses **JDBC** to connect to a **MySQL** database (XAMPP). It supports:
- Student and course registration
- Semester enrollment and attendance tracking
- Incremental marks entry (CIA‑1, CIA‑2, CIA‑3, ESE)
- Automatic grading according to KEC R2024 rules
- Generation of a semester mark‑sheet and an attendance defaulter report

## Prerequisites
1. **XAMPP** installed with MySQL running.
2. Create a MySQL database named `kec_student_system` (instructions below).
3. **JDK 11+** installed.
4. **VS Code** with the **Java Extension Pack** (or any Java IDE).

## Setup Steps
```bash
# 1. Open a terminal as Administrator (for XAMPP MySQL)
# 2. Log into MySQL and create the database
mysql -u root -p
> CREATE DATABASE kec_student_system;
> EXIT;

# 3. Execute the schema script (adjust path if needed)
mysql -u root -p kec_student_system < E:/KonguStudentEval/kec_schema.sql

# 4. Build the project with Maven (Maven wrapper is provided)
cd E:/KonguStudentEval
./mvnw clean package

# 5. Run the application
java -jar target/kec-student-eval-1.0.jar
```

## Project Structure
```
E:/KonguStudentEval
├─ src/main/java/com/kec
│   ├─ DBConnection.java
│   ├─ MainApp.java
│   ├─ dao
│   │   ├─ StudentDAO.java
│   │   ├─ CourseDAO.java
│   │   └─ AcademicDAO.java
│   └─ model
│       ├─ Student.java
│       ├─ Course.java
│       └─ Enrollment.java
├─ kec_schema.sql          # database schema
├─ pom.xml                 # Maven build file
└─ README.md               # this file
```

## Running Inside VS Code
1. Open **File → Open Folder…** and select `E:/KonguStudentEval`.
2. VS Code will detect the Maven project. Use the **Run** icon or terminal command `./mvnw spring-boot:run` (the jar is built as a plain Java app, not Spring).
3. Follow the on‑screen menu to add data and generate reports.

---
*If you encounter any MySQL connection errors, verify that the `DB_URL`, `DB_USER`, and `DB_PASSWORD` constants in `DBConnection.java` match your XAMPP MySQL credentials.*
