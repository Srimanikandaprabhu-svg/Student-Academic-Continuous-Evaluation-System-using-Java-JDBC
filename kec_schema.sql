DROP TABLE IF EXISTS assessment_marks;
DROP TABLE IF EXISTS enrollments;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS students;
CREATE TABLE students (
    roll_number VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    department VARCHAR(10) NOT NULL,
    current_semester INT NOT NULL
);
CREATE TABLE courses (
    course_code VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    credits INT NOT NULL,
    course_type ENUM('THEORY','PRACTICAL','EMBEDDED') NOT NULL
);
CREATE TABLE enrollments (
    enrollment_id INT AUTO_INCREMENT PRIMARY KEY,
    roll_number VARCHAR(20) NOT NULL,
    course_code VARCHAR(20) NOT NULL,
    semester INT NOT NULL,
    total_classes INT NOT NULL DEFAULT 0,
    attended_classes INT NOT NULL DEFAULT 0,
    CONSTRAINT fk_student FOREIGN KEY (roll_number) REFERENCES students(roll_number) ON DELETE CASCADE,
    CONSTRAINT fk_course FOREIGN KEY (course_code) REFERENCES courses(course_code) ON DELETE CASCADE,
    UNIQUE KEY uniq_enroll (roll_number, course_code, semester)
);
CREATE TABLE assessment_marks (
    enrollment_id INT NOT NULL,
    phase ENUM('CIA1','CIA2','CIA3','ESE') NOT NULL,
    marks INT CHECK (marks BETWEEN 0 AND 100),
    CONSTRAINT fk_enrollment FOREIGN KEY (enrollment_id) REFERENCES enrollments(enrollment_id) ON DELETE CASCADE,
    PRIMARY KEY (enrollment_id, phase)
);
CREATE INDEX idx_enrollment_student ON enrollments (roll_number);
CREATE INDEX idx_enrollment_course ON enrollments (course_code);
