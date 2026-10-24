-- =============================================================
-- KEC Student Evaluation System – Sample / Seed Data
-- 10 Students: 4 IT, 3 ECE, 3 CSE
-- Courses: THEORY, PRACTICAL, EMBEDDED for each department
-- Enrollments with varied attendance (>75%, 65-74%, <65%)
-- Marks: CIA1, CIA2, CIA3, ESE – covering O, A+, A, B+, B, RA, SA, AB
-- =============================================================

-- ===================== STUDENTS =====================
INSERT INTO students VALUES ('24ITR001', 'Arun Kumar',      'IT',  3);
INSERT INTO students VALUES ('24ITR002', 'Priya Dharshini', 'IT',  3);
INSERT INTO students VALUES ('24ITR003', 'Karthik Raja',    'IT',  3);
INSERT INTO students VALUES ('24ITR004', 'Deepa Lakshmi',   'IT',  3);
INSERT INTO students VALUES ('24ECE001', 'Surya Prakash',   'ECE', 3);
INSERT INTO students VALUES ('24ECE002', 'Meena Kumari',    'ECE', 3);
INSERT INTO students VALUES ('24ECE003', 'Ravi Shankar',    'ECE', 3);
INSERT INTO students VALUES ('24CSE001', 'Naveen Raj',      'CSE', 3);
INSERT INTO students VALUES ('24CSE002', 'Sowmiya Devi',    'CSE', 3);
INSERT INTO students VALUES ('24CSE003', 'Harish Kumar',    'CSE', 3);

-- ===================== COURSES =====================
-- IT Department Courses (Semester 3)
INSERT INTO courses VALUES ('24ITR31', 'Data Structures',                 4, 'THEORY');
INSERT INTO courses VALUES ('24ITR32', 'Database Management Systems',     4, 'THEORY');
INSERT INTO courses VALUES ('24ITP31', 'Data Structures Lab',             2, 'PRACTICAL');
INSERT INTO courses VALUES ('24ITE31', 'Object Oriented Programming',     3, 'EMBEDDED');

-- ECE Department Courses (Semester 3)
INSERT INTO courses VALUES ('24ECR31', 'Signals and Systems',             4, 'THEORY');
INSERT INTO courses VALUES ('24ECR32', 'Digital Electronics',             4, 'THEORY');
INSERT INTO courses VALUES ('24ECP31', 'Electronic Circuits Lab',         2, 'PRACTICAL');
INSERT INTO courses VALUES ('24ECE31', 'Embedded System Design',          3, 'EMBEDDED');

-- CSE Department Courses (Semester 3)
INSERT INTO courses VALUES ('24CSR31', 'Operating Systems',               4, 'THEORY');
INSERT INTO courses VALUES ('24CSR32', 'Computer Networks',               4, 'THEORY');
INSERT INTO courses VALUES ('24CSP31', 'OS Lab',                          2, 'PRACTICAL');
INSERT INTO courses VALUES ('24CSE31', 'Full Stack Development',          3, 'EMBEDDED');

-- ===================== ENROLLMENTS & ATTENDANCE =====================
-- Format: (enrollment_id, roll_number, course_code, semester, total_classes, attended_classes)
-- Attendance scenarios:
--   >=75%   : Eligible
--   65-74%  : Conditionally eligible (Medical Condonation)
--   <65%    : SA – Shortage of Attendance

-- ---- IT Students ----
-- 24ITR001 – Arun Kumar: Good student, high attendance everywhere
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR001', '24ITR31', 3, 50, 48);  -- 96% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR001', '24ITR32', 3, 50, 46);  -- 92% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR001', '24ITP31', 3, 30, 28);  -- 93% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR001', '24ITE31', 3, 40, 38);  -- 95% Eligible

-- 24ITR002 – Priya Dharshini: Moderate attendance, one course conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR002', '24ITR31', 3, 50, 40);  -- 80% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR002', '24ITR32', 3, 50, 35);  -- 70% Conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR002', '24ITP31', 3, 30, 26);  -- 87% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR002', '24ITE31', 3, 40, 32);  -- 80% Eligible

-- 24ITR003 – Karthik Raja: One course <65% (SA scenario)
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR003', '24ITR31', 3, 50, 38);  -- 76% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR003', '24ITR32', 3, 50, 30);  -- 60% SA!
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR003', '24ITP31', 3, 30, 24);  -- 80% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR003', '24ITE31', 3, 40, 34);  -- 85% Eligible

-- 24ITR004 – Deepa Lakshmi: Average student
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR004', '24ITR31', 3, 50, 42);  -- 84% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR004', '24ITR32', 3, 50, 39);  -- 78% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR004', '24ITP31', 3, 30, 25);  -- 83% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ITR004', '24ITE31', 3, 40, 30);  -- 75% Eligible

-- ---- ECE Students ----
-- 24ECE001 – Surya Prakash: Excellent attendance
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE001', '24ECR31', 3, 50, 49);  -- 98% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE001', '24ECR32', 3, 50, 47);  -- 94% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE001', '24ECP31', 3, 30, 29);  -- 97% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE001', '24ECE31', 3, 40, 39);  -- 98% Eligible

-- 24ECE002 – Meena Kumari: Conditional in two courses
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE002', '24ECR31', 3, 50, 34);  -- 68% Conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE002', '24ECR32', 3, 50, 33);  -- 66% Conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE002', '24ECP31', 3, 30, 23);  -- 77% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE002', '24ECE31', 3, 40, 36);  -- 90% Eligible

-- 24ECE003 – Ravi Shankar: Very poor attendance in multiple courses (SA)
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE003', '24ECR31', 3, 50, 28);  -- 56% SA!
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE003', '24ECR32', 3, 50, 31);  -- 62% SA!
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE003', '24ECP31', 3, 30, 22);  -- 73% Conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24ECE003', '24ECE31', 3, 40, 32);  -- 80% Eligible

-- ---- CSE Students ----
-- 24CSE001 – Naveen Raj: Topper
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE001', '24CSR31', 3, 50, 50);  -- 100% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE001', '24CSR32', 3, 50, 48);  -- 96% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE001', '24CSP31', 3, 30, 30);  -- 100% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE001', '24CSE31', 3, 40, 40);  -- 100% Eligible

-- 24CSE002 – Sowmiya Devi: Good
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE002', '24CSR31', 3, 50, 44);  -- 88% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE002', '24CSR32', 3, 50, 41);  -- 82% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE002', '24CSP31', 3, 30, 27);  -- 90% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE002', '24CSE31', 3, 40, 35);  -- 88% Eligible

-- 24CSE003 – Harish Kumar: One SA, borderline
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE003', '24CSR31', 3, 50, 37);  -- 74% Conditional
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE003', '24CSR32', 3, 50, 25);  -- 50% SA!
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE003', '24CSP31', 3, 30, 24);  -- 80% Eligible
INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES ('24CSE003', '24CSE31', 3, 40, 31);  -- 78% Eligible

-- ===================== ASSESSMENT MARKS =====================
-- Phase: CIA1, CIA2, CIA3 (0-100 each), ESE (0-100)
-- We create varied scenarios:
--   Topper (O grade), High performers (A+, A), Average (B+, B), Fail (RA), Absent in ESE

-- ---- 24ITR001 Arun Kumar – Topper (should get O / A+ grades) ----
INSERT INTO assessment_marks VALUES (1, 'CIA1', 95);
INSERT INTO assessment_marks VALUES (1, 'CIA2', 92);
INSERT INTO assessment_marks VALUES (1, 'CIA3', 90);
INSERT INTO assessment_marks VALUES (1, 'ESE',  94);

INSERT INTO assessment_marks VALUES (2, 'CIA1', 88);
INSERT INTO assessment_marks VALUES (2, 'CIA2', 90);
INSERT INTO assessment_marks VALUES (2, 'CIA3', 85);
INSERT INTO assessment_marks VALUES (2, 'ESE',  90);

INSERT INTO assessment_marks VALUES (3, 'CIA1', 92);
INSERT INTO assessment_marks VALUES (3, 'CIA2', 95);
INSERT INTO assessment_marks VALUES (3, 'CIA3', 90);
INSERT INTO assessment_marks VALUES (3, 'ESE',  88);

INSERT INTO assessment_marks VALUES (4, 'CIA1', 90);
INSERT INTO assessment_marks VALUES (4, 'CIA2', 88);
INSERT INTO assessment_marks VALUES (4, 'CIA3', 92);
INSERT INTO assessment_marks VALUES (4, 'ESE',  91);

-- ---- 24ITR002 Priya Dharshini – Good (A / B+ range) ----
INSERT INTO assessment_marks VALUES (5, 'CIA1', 78);
INSERT INTO assessment_marks VALUES (5, 'CIA2', 82);
INSERT INTO assessment_marks VALUES (5, 'CIA3', 75);
INSERT INTO assessment_marks VALUES (5, 'ESE',  80);

INSERT INTO assessment_marks VALUES (6, 'CIA1', 70);
INSERT INTO assessment_marks VALUES (6, 'CIA2', 68);
INSERT INTO assessment_marks VALUES (6, 'CIA3', 72);
INSERT INTO assessment_marks VALUES (6, 'ESE',  65);

INSERT INTO assessment_marks VALUES (7, 'CIA1', 85);
INSERT INTO assessment_marks VALUES (7, 'CIA2', 80);
INSERT INTO assessment_marks VALUES (7, 'CIA3', 78);
INSERT INTO assessment_marks VALUES (7, 'ESE',  82);

INSERT INTO assessment_marks VALUES (8, 'CIA1', 75);
INSERT INTO assessment_marks VALUES (8, 'CIA2', 78);
INSERT INTO assessment_marks VALUES (8, 'CIA3', 80);
INSERT INTO assessment_marks VALUES (8, 'ESE',  76);

-- ---- 24ITR003 Karthik Raja – Mixed (one SA course, one ESE fail) ----
INSERT INTO assessment_marks VALUES (9,  'CIA1', 60);
INSERT INTO assessment_marks VALUES (9,  'CIA2', 65);
INSERT INTO assessment_marks VALUES (9,  'CIA3', 58);
INSERT INTO assessment_marks VALUES (9,  'ESE',  55);

-- enrollment_id 10: SA (attendance <65%), still entering marks for record
INSERT INTO assessment_marks VALUES (10, 'CIA1', 50);
INSERT INTO assessment_marks VALUES (10, 'CIA2', 55);
INSERT INTO assessment_marks VALUES (10, 'CIA3', 48);
INSERT INTO assessment_marks VALUES (10, 'ESE',  40);  -- below ESE threshold too

INSERT INTO assessment_marks VALUES (11, 'CIA1', 72);
INSERT INTO assessment_marks VALUES (11, 'CIA2', 70);
INSERT INTO assessment_marks VALUES (11, 'CIA3', 68);
INSERT INTO assessment_marks VALUES (11, 'ESE',  75);

INSERT INTO assessment_marks VALUES (12, 'CIA1', 80);
INSERT INTO assessment_marks VALUES (12, 'CIA2', 78);
INSERT INTO assessment_marks VALUES (12, 'CIA3', 82);
INSERT INTO assessment_marks VALUES (12, 'ESE',  70);

-- ---- 24ITR004 Deepa Lakshmi – Average (B+ / B range) ----
INSERT INTO assessment_marks VALUES (13, 'CIA1', 68);
INSERT INTO assessment_marks VALUES (13, 'CIA2', 72);
INSERT INTO assessment_marks VALUES (13, 'CIA3', 65);
INSERT INTO assessment_marks VALUES (13, 'ESE',  70);

INSERT INTO assessment_marks VALUES (14, 'CIA1', 55);
INSERT INTO assessment_marks VALUES (14, 'CIA2', 60);
INSERT INTO assessment_marks VALUES (14, 'CIA3', 58);
INSERT INTO assessment_marks VALUES (14, 'ESE',  62);

INSERT INTO assessment_marks VALUES (15, 'CIA1', 78);
INSERT INTO assessment_marks VALUES (15, 'CIA2', 80);
INSERT INTO assessment_marks VALUES (15, 'CIA3', 75);
INSERT INTO assessment_marks VALUES (15, 'ESE',  70);

INSERT INTO assessment_marks VALUES (16, 'CIA1', 60);
INSERT INTO assessment_marks VALUES (16, 'CIA2', 65);
INSERT INTO assessment_marks VALUES (16, 'CIA3', 62);
INSERT INTO assessment_marks VALUES (16, 'ESE',  68);

-- ---- 24ECE001 Surya Prakash – Excellent (O / A+ grades) ----
INSERT INTO assessment_marks VALUES (17, 'CIA1', 96);
INSERT INTO assessment_marks VALUES (17, 'CIA2', 94);
INSERT INTO assessment_marks VALUES (17, 'CIA3', 98);
INSERT INTO assessment_marks VALUES (17, 'ESE',  97);

INSERT INTO assessment_marks VALUES (18, 'CIA1', 90);
INSERT INTO assessment_marks VALUES (18, 'CIA2', 92);
INSERT INTO assessment_marks VALUES (18, 'CIA3', 88);
INSERT INTO assessment_marks VALUES (18, 'ESE',  93);

INSERT INTO assessment_marks VALUES (19, 'CIA1', 95);
INSERT INTO assessment_marks VALUES (19, 'CIA2', 93);
INSERT INTO assessment_marks VALUES (19, 'CIA3', 96);
INSERT INTO assessment_marks VALUES (19, 'ESE',  90);

INSERT INTO assessment_marks VALUES (20, 'CIA1', 92);
INSERT INTO assessment_marks VALUES (20, 'CIA2', 90);
INSERT INTO assessment_marks VALUES (20, 'CIA3', 94);
INSERT INTO assessment_marks VALUES (20, 'ESE',  95);

-- ---- 24ECE002 Meena Kumari – Moderate (A / B+ range, conditional attendance) ----
INSERT INTO assessment_marks VALUES (21, 'CIA1', 72);
INSERT INTO assessment_marks VALUES (21, 'CIA2', 75);
INSERT INTO assessment_marks VALUES (21, 'CIA3', 70);
INSERT INTO assessment_marks VALUES (21, 'ESE',  78);

INSERT INTO assessment_marks VALUES (22, 'CIA1', 65);
INSERT INTO assessment_marks VALUES (22, 'CIA2', 70);
INSERT INTO assessment_marks VALUES (22, 'CIA3', 68);
INSERT INTO assessment_marks VALUES (22, 'ESE',  72);

INSERT INTO assessment_marks VALUES (23, 'CIA1', 80);
INSERT INTO assessment_marks VALUES (23, 'CIA2', 82);
INSERT INTO assessment_marks VALUES (23, 'CIA3', 78);
INSERT INTO assessment_marks VALUES (23, 'ESE',  85);

INSERT INTO assessment_marks VALUES (24, 'CIA1', 76);
INSERT INTO assessment_marks VALUES (24, 'CIA2', 78);
INSERT INTO assessment_marks VALUES (24, 'CIA3', 74);
INSERT INTO assessment_marks VALUES (24, 'ESE',  80);

-- ---- 24ECE003 Ravi Shankar – Poor (SA in 2 courses, RA in others) ----
-- enrollment 25: SA (56% attendance)
INSERT INTO assessment_marks VALUES (25, 'CIA1', 40);
INSERT INTO assessment_marks VALUES (25, 'CIA2', 45);
INSERT INTO assessment_marks VALUES (25, 'CIA3', 38);
INSERT INTO assessment_marks VALUES (25, 'ESE',  35);

-- enrollment 26: SA (62% attendance)
INSERT INTO assessment_marks VALUES (26, 'CIA1', 42);
INSERT INTO assessment_marks VALUES (26, 'CIA2', 48);
INSERT INTO assessment_marks VALUES (26, 'CIA3', 44);
INSERT INTO assessment_marks VALUES (26, 'ESE',  30);

-- enrollment 27: conditional attendance, low marks
INSERT INTO assessment_marks VALUES (27, 'CIA1', 55);
INSERT INTO assessment_marks VALUES (27, 'CIA2', 50);
INSERT INTO assessment_marks VALUES (27, 'CIA3', 52);
INSERT INTO assessment_marks VALUES (27, 'ESE',  45);

-- enrollment 28: eligible attendance, but low marks (RA)
INSERT INTO assessment_marks VALUES (28, 'CIA1', 48);
INSERT INTO assessment_marks VALUES (28, 'CIA2', 45);
INSERT INTO assessment_marks VALUES (28, 'CIA3', 50);
INSERT INTO assessment_marks VALUES (28, 'ESE',  40);

-- ---- 24CSE001 Naveen Raj – Topper (O grades across) ----
INSERT INTO assessment_marks VALUES (29, 'CIA1', 98);
INSERT INTO assessment_marks VALUES (29, 'CIA2', 96);
INSERT INTO assessment_marks VALUES (29, 'CIA3', 97);
INSERT INTO assessment_marks VALUES (29, 'ESE',  99);

INSERT INTO assessment_marks VALUES (30, 'CIA1', 95);
INSERT INTO assessment_marks VALUES (30, 'CIA2', 93);
INSERT INTO assessment_marks VALUES (30, 'CIA3', 94);
INSERT INTO assessment_marks VALUES (30, 'ESE',  96);

INSERT INTO assessment_marks VALUES (31, 'CIA1', 97);
INSERT INTO assessment_marks VALUES (31, 'CIA2', 95);
INSERT INTO assessment_marks VALUES (31, 'CIA3', 96);
INSERT INTO assessment_marks VALUES (31, 'ESE',  98);

INSERT INTO assessment_marks VALUES (32, 'CIA1', 94);
INSERT INTO assessment_marks VALUES (32, 'CIA2', 96);
INSERT INTO assessment_marks VALUES (32, 'CIA3', 95);
INSERT INTO assessment_marks VALUES (32, 'ESE',  97);

-- ---- 24CSE002 Sowmiya Devi – Good (A+ / A range) ----
INSERT INTO assessment_marks VALUES (33, 'CIA1', 85);
INSERT INTO assessment_marks VALUES (33, 'CIA2', 88);
INSERT INTO assessment_marks VALUES (33, 'CIA3', 82);
INSERT INTO assessment_marks VALUES (33, 'ESE',  86);

INSERT INTO assessment_marks VALUES (34, 'CIA1', 80);
INSERT INTO assessment_marks VALUES (34, 'CIA2', 82);
INSERT INTO assessment_marks VALUES (34, 'CIA3', 78);
INSERT INTO assessment_marks VALUES (34, 'ESE',  84);

INSERT INTO assessment_marks VALUES (35, 'CIA1', 90);
INSERT INTO assessment_marks VALUES (35, 'CIA2', 88);
INSERT INTO assessment_marks VALUES (35, 'CIA3', 85);
INSERT INTO assessment_marks VALUES (35, 'ESE',  82);

INSERT INTO assessment_marks VALUES (36, 'CIA1', 82);
INSERT INTO assessment_marks VALUES (36, 'CIA2', 85);
INSERT INTO assessment_marks VALUES (36, 'CIA3', 80);
INSERT INTO assessment_marks VALUES (36, 'ESE',  88);

-- ---- 24CSE003 Harish Kumar – Borderline (one SA, one fail) ----
-- enrollment 37: conditional attendance (74%)
INSERT INTO assessment_marks VALUES (37, 'CIA1', 55);
INSERT INTO assessment_marks VALUES (37, 'CIA2', 58);
INSERT INTO assessment_marks VALUES (37, 'CIA3', 52);
INSERT INTO assessment_marks VALUES (37, 'ESE',  50);

-- enrollment 38: SA (50% attendance)
INSERT INTO assessment_marks VALUES (38, 'CIA1', 45);
INSERT INTO assessment_marks VALUES (38, 'CIA2', 40);
INSERT INTO assessment_marks VALUES (38, 'CIA3', 42);
INSERT INTO assessment_marks VALUES (38, 'ESE',  35);

-- enrollment 39: eligible, decent marks
INSERT INTO assessment_marks VALUES (39, 'CIA1', 70);
INSERT INTO assessment_marks VALUES (39, 'CIA2', 72);
INSERT INTO assessment_marks VALUES (39, 'CIA3', 68);
INSERT INTO assessment_marks VALUES (39, 'ESE',  65);

-- enrollment 40: eligible, average marks
INSERT INTO assessment_marks VALUES (40, 'CIA1', 60);
INSERT INTO assessment_marks VALUES (40, 'CIA2', 62);
INSERT INTO assessment_marks VALUES (40, 'CIA3', 58);
INSERT INTO assessment_marks VALUES (40, 'ESE',  55);
