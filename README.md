# 🎓 Student Academic & Continuous Evaluation System
### Kongu Engineering College (Autonomous) — R2024 Regulation

[![Java](https://img.shields.io/badge/Java-11%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![JDBC](https://img.shields.io/badge/Connectivity-JDBC-007396?style=for-the-badge&logo=java&logoColor=white)](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)
[![Maven](https://img.shields.io/badge/Build-Maven-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-green.svg?style=for-the-badge)](LICENSE)

A comprehensive, console-based academic management and continuous assessment evaluation system built using **Java (JDBC)** and **MySQL**. The system is strictly engineered in accordance with the **Kongu Engineering College (Autonomous) - R2024 Academic Regulations**, supporting multi-tier course evaluation (Theory, Practical, and Embedded courses), dynamic Continuous Internal Assessment (CIA), End Semester Examination (ESE) grading, attendance compliance checks, SGPA calculation, and administrative analytical reporting.

---

## 📋 Table of Contents
- [✨ Key Features](#-key-features)
- [🏛️ System Architecture & Design Pattern](#️-system-architecture--design-pattern)
- [📐 Database Schema](#-database-schema)
- [📊 KEC R2024 Evaluation Regulations & Grading System](#-kec-r2024-evaluation-regulations--grading-system)
  - [Course Assessment Weightage](#course-assessment-weightage)
  - [Attendance Compliance Rules](#attendance-compliance-rules)
  - [Letter Grades & Grade Points](#letter-grades--grade-points)
- [🖥️ Menu Options & Functionalities](#️-menu-options--functionalities)
- [🚀 Installation & Setup Guide](#-installation--setup-guide)
  - [Prerequisites](#prerequisites)
  - [Database Configuration](#database-configuration)
  - [Project Configuration](#project-configuration)
  - [Execution Options](#execution-options)
- [📑 Sample System Outputs](#-sample-system-outputs)
- [📁 Project Directory Structure](#-project-directory-structure)
- [💡 Demonstration Video](#-demonstration-video)
- [📄 License](#-license)

---

## ✨ Key Features

- **Student & Course Management**: Register students across multiple academic departments (`IT`, `CSE`, `ECE`, `MECH`, etc.) and register courses with credit points and classifications (`THEORY`, `PRACTICAL`, `EMBEDDED`).
- **Course Enrollment & Attendance Tracking**: Enroll students into semester-specific courses and track individual classroom attendance percentage with automated compliance alerts.
- **Continuous Internal Assessment (CIA) & ESE Entry**: Incremental recording and UPSERT updates for CIA-1, CIA-2, Assignment/Quiz (CIA-3), and End Semester Exam (ESE).
- **Rule-Engine Grading & SGPA Computation**:
  - Dynamically calculates weighted scores based on course category.
  - Verifies minimum mandatory ESE performance cutoffs.
  - Automatically calculates Semester Grade Point Average (SGPA) and equivalent percentage.
- **Executive & Administrative Reports**:
  - **Student Semester Mark Sheet**: Complete academic audit report showing course-wise CIA, ESE, total weighted scores, letter grades, and final SGPA.
  - **Attendance Defaulter Report**: Automatically flags students with attendance below 65% (`SA` grade).
  - **Class Performance & Result Summary**: Departmental analytics showing All Clear, Partial, All Fail distributions and average SGPA.

---

## 🏛️ System Architecture & Design Pattern

The application adopts the **Data Access Object (DAO)** architectural pattern, enforcing separation of concerns between business logic, data persistence, and console user interface:

```
                  ┌────────────────────────┐
                  │        MainApp         │  <--- Interactive CLI Menu / User Inputs
                  └───────────┬────────────┘
                              │
             ┌────────────────┼────────────────┐
             ▼                ▼                ▼
     ┌──────────────┐ ┌──────────────┐ ┌──────────────┐
     │  StudentDAO  │ │  CourseDAO   │ │ AcademicDAO  │  <--- Business Logic & Queries
     └───────┬──────┘ └──────┬───────┘ └──────┬───────┘
             │               │                │
             └───────────────┼────────────────┘
                             ▼
                    ┌─────────────────┐
                    │  DBConnection   │  <--- JDBC Connection Manager (Driver / Pool)
                    └────────┬────────┘
                             ▼
                    ┌─────────────────┐
                    │ MySQL Database  │  <--- 'kec_student_system'
                    └─────────────────┘
```

- **`model` Layer**: POJOs representing `Student`, `Course`, and `Enrollment`.
- **`dao` Layer**: Handles transactional SQL queries (`StudentDAO`, `CourseDAO`, `AcademicDAO`).
- **`DBConnection`**: Centralized JDBC connection factory using `com.mysql.cj.jdbc.Driver`.
- **`MainApp`**: Entry point presenting formatted ASCII-boxed interfaces and input scanners.

---

## 📐 Database Schema

The relational database (`kec_student_system`) is structured with cascading referential integrity and optimized indexes:

```
 students                          enrollments                           assessment_marks
┌────────────────────┐            ┌───────────────────┐                 ┌───────────────────┐
│ roll_number (PK)   │◄───┐       │ enrollment_id(PK) │◄───────────┐    │ enrollment_id(PK) │
│ name               │    └───────┤ roll_number (FK)  │            └────┤ phase (PK)        │
│ department         │            │ course_code (FK)  ├──┐              │ marks (0-100)     │
│ current_semester   │            │ semester          │  │              └───────────────────┘
└────────────────────┘            │ total_classes     │  │
                                  │ attended_classes  │  │
 courses                          └───────────────────┘  │
┌────────────────────┐                                   │
│ course_code (PK)   │◄──────────────────────────────────┘
│ title              │
│ credits            │
│ course_type (ENUM) │
└────────────────────┘
```

### Table Definitions:
1. **`students`**: Master record of students (`roll_number`, `name`, `department`, `current_semester`).
2. **`courses`**: Course catalog (`course_code`, `title`, `credits`, `course_type`: `THEORY`/`PRACTICAL`/`EMBEDDED`).
3. **`enrollments`**: Junction table with unique constraint on `(roll_number, course_code, semester)`, tracking attendance metrics (`total_classes`, `attended_classes`).
4. **`assessment_marks`**: Stores component marks (`phase`: `CIA1`, `CIA2`, `CIA3`, `ESE`) bounded by checks `[0, 100]`.

---

## 📊 KEC R2024 Evaluation Regulations & Grading System

### Course Assessment Weightage

| Course Type | CIA Weight | ESE Weight | Minimum ESE Threshold (Raw /100) | Minimum ESE Threshold (Weighted) |
| :--- | :---: | :---: | :---: | :---: |
| **THEORY** | 40% | 60% | 45 / 100 | 27.0 / 60 |
| **PRACTICAL** | 60% | 40% | 50 / 100 | 20.0 / 40 |
| **EMBEDDED** | 50% | 50% | 45 / 100 | 22.5 / 50 |

$$\text{Weighted CIA Score} = \left(\frac{\text{CIA1} + \text{CIA2} + \text{CIA3}}{3}\right) \times \text{Weight}_{\text{CIA}}$$

$$\text{Weighted ESE Score} = \text{Marks}_{\text{ESE}} \times \text{Weight}_{\text{ESE}}$$

$$\text{Total Score} = \text{Weighted CIA Score} + \text{Weighted ESE Score}$$

---

### Attendance Compliance Rules

- **$\ge$ 75% Attendance**: Fully eligible to sit for End Semester Examination.
- **65% to 74.9% Attendance**: Conditionally eligible (requires valid medical condonation approved by authorities).
- **$<$ 65% Attendance**: Shortage of Attendance (`SA` Grade awarded, zero credit earned, student must re-register for the course).

---

### Letter Grades & Grade Points

| Range of Marks | Letter Grade | Grade Point | Result | Remarks |
| :--- | :---: | :---: | :---: | :--- |
| **91 – 100** | **O** | **10** | PASS | Outstanding |
| **81 – 90** | **A+** | **9** | PASS | Excellent |
| **71 – 80** | **A** | **8** | PASS | Very Good |
| **61 – 70** | **B+** | **7** | PASS | Good |
| **50 – 60** | **B** | **6** | PASS | Average |
| **$<$ 50** | **RA** | **0** | FAIL | Re-Appearance Required |
| **Attendance $<$ 65%** | **SA** | **0** | FAIL | Shortage of Attendance |
| **ESE Absent** | **AB** | **0** | FAIL | Absent in End Semester Exam |

$$\text{SGPA} = \frac{\sum (\text{Credits} \times \text{Grade Point})}{\sum \text{Credits}}$$

$$\text{Equivalent Percentage} = \text{SGPA} \times 10$$

---

## 🖥️ Menu Options & Functionalities

```
  ┌──────────────────────────────────────────────────────┐
  │                    MAIN MENU                         │
  ├──────────────────────────────────────────────────────┤
  │  DATA ENTRY                                         │
  │   1.  Add Student                                   │
  │   2.  Add Course                                    │
  │   3.  Enroll Student in Course                      │
  │   4.  Update Attendance                             │
  ├──────────────────────────────────────────────────────┤
  │  MARKS ENTRY                                        │
  │   5.  Enter Marks (CIA1 + CIA2 + Assignment + ESE)  │
  ├──────────────────────────────────────────────────────┤
  │  REPORTS                                            │
  │   6.  Generate Student Mark Sheet                   │
  │   7.  Attendance Defaulter Report                   │
  │   8.  Class Performance & Result Summary            │
  ├──────────────────────────────────────────────────────┤
  │  VIEW DATA                                          │
  │   9.  View All Students                             │
  │  10.  View All Courses                              │
  │  11.  List Student Enrollments                      │
  ├──────────────────────────────────────────────────────┤
  │   0.  Exit                                          │
  └──────────────────────────────────────────────────────┘
```

---

## 🚀 Installation & Setup Guide

### Prerequisites
- **Java Development Kit (JDK 11 or higher)**
- **MySQL Server 8.0+** (or **XAMPP / WampServer** with MySQL service started)
- **Apache Maven 3.8+** (optional if using the bundled `run.bat`)
- Git

### Database Configuration

1. Start MySQL via XAMPP Control Panel or command line:
   ```bash
   mysql -u root -p
   ```
2. Create the database:
   ```sql
   CREATE DATABASE kec_student_system;
   EXIT;
   ```
3. Import the database schema and sample seed data:
   ```bash
   # From the project root directory
   mysql -u root kec_student_system < kec_schema.sql
   mysql -u root kec_student_system < kec_seed_data.sql
   ```

### Project Configuration

Verify connection properties in [`src/main/java/com/kec/DBConnection.java`](src/main/java/com/kec/DBConnection.java):
```java
private static final String DB_URL = "jdbc:mysql://localhost:3306/kec_student_system?useSSL=false&allowPublicKeyRetrieval=true";
private static final String DB_USER = "root";
private static final String DB_PASSWORD = ""; // Enter your MySQL root password if set
```

### Execution Options

#### Option 1: Quick Run using Batch Script (Windows)
Double-click [`run.bat`](run.bat) or execute via terminal:
```cmd
.\run.bat
```
*(This compiles all sources directly with the bundled MySQL Connector in `lib/` and launches the application.)*

#### Option 2: Build & Run with Apache Maven
```bash
# Clean and build executable JAR
mvn clean package

# Run the packaged JAR with dependencies
java -jar target/kec-student-eval-1.0-jar-with-dependencies.jar
```

#### Option 3: Run inside Visual Studio Code / IntelliJ IDEA
1. Open the project folder in your IDE.
2. Ensure the Java extension detects the Maven project structure.
3. Open `src/main/java/com/kec/MainApp.java` and click **Run** / **Debug**.

---

## 📑 Sample System Outputs

### 1. Student Semester Mark Sheet (Menu 6)
```text
  ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗
  ║              KONGU ENGINEERING COLLEGE (AUTONOMOUS), PERUNDURAI - 638060                        ║
  ║                   STUDENT SEMESTER MARK SHEET (R2024)                                          ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  Roll No   : 24ITR001               Name       : Arun Kumar                                    ║
  ║  Department: IT                     Semester   : 3                                             ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  Code     | Title                      | Cred | Att%  | CIA  | ESE  | Total | Grade | GP | P/F ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  24ITR31  | Data Structures            | 4    |  96.0 | 35.6 | 54.0 |  89.6 | A+    |  9 | PASS║
  ║  24ITR32  | Database Management Systems| 4    |  92.0 | 36.4 | 55.2 |  91.6 | O     | 10 | PASS║
  ║  24ITP31  | Data Structures Lab        | 2    |  95.0 | 57.0 | 36.0 |  93.0 | O     | 10 | PASS║
  ║  24ITE31  | Object Oriented Programming| 3    |  94.0 | 44.5 | 45.0 |  89.5 | A+    |  9 | PASS║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  Total Credits: 13     |   Passed: 4   |   Failed: 0                                         ║
  ║  SGPA: 9.46            |   Equivalent Percentage: 94.62%                                       ║
  ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝
```

### 2. Attendance Defaulter Report (Menu 7)
```text
  ╔══════════════════════════════════════════════════════════════════════════════════════════════╗
  ║           ATTENDANCE DEFAULTER REPORT - SEMESTER 3  (Attendance < 65%)                   ║
  ║           Students flagged with SA (Shortage of Attendance) Grade                          ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  Roll No    | Name              | Dept | Course Code | Course Title             | Att%     ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  24ITR003   | Karthik Raja      | IT   | 24ITR32     | Database Management Sys  |  62.0%   ║
  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣
  ║  Total SA Entries: 1                                                                       ║
  ╚══════════════════════════════════════════════════════════════════════════════════════════════╝
```

---

## 📁 Project Directory Structure

```
KonguStudentEval/
│
├── lib/
│   └── mysql-connector-j-8.0.33.jar     # Bundled MySQL JDBC connector driver
│
├── src/main/java/com/kec/
│   ├── DBConnection.java                 # Centralized JDBC connection manager
│   ├── MainApp.java                      # Interactive console UI and execution loop
│   ├── dao/
│   │   ├── AcademicDAO.java              # R2024 Evaluation rules, marks & reports
│   │   ├── CourseDAO.java                # Course persistence operations
│   │   └── StudentDAO.java               # Student persistence operations
│   └── model/
│       ├── Course.java                   # Course entity model
│       ├── Enrollment.java               # Enrollment & attendance model
│       └── Student.java                  # Student entity model
│
├── kec_schema.sql                        # DDL table creation and constraint script
├── kec_seed_data.sql                     # Seed test data (10 students, 12 courses, marks)
├── pom.xml                               # Maven project object model & build plugins
├── run.bat                               # One-click Windows build and execution script
├── javavideo.mp4                         # System demonstration video recording
└── README.md                             # Project documentation
```

---

## 💡 Demonstration Video

A video demonstration [`javavideo.mp4`](javavideo.mp4) showcasing the full workflow of compilation, execution, database operations, mark sheet generation, and analytics is included in the root directory.

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
