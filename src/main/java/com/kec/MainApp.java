package com.kec;
import com.kec.dao.*;
import com.kec.model.*;
import java.util.*;
public class MainApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final AcademicDAO academicDAO = new AcademicDAO();
        public static void main(String[] args) {
        System.out.println();
        System.out.println("  ╔════════════════════════════════════════════════════════════════════╗");
        System.out.println("  ║     KONGU ENGINEERING COLLEGE (AUTONOMOUS), PERUNDURAI 638060   ║");
        System.out.println("  ║     Student Academic & Continuous Evaluation System (R2024)       ║");
        System.out.println("  ║     Developed using Java JDBC + MySQL                            ║");
        System.out.println("  ╚════════════════════════════════════════════════════════════════════╝");

        while (true) {
            printMenu();
            int choice = readInt("\n  Enter your choice: ");
            switch (choice) {
                case 1:  addStudent(); break;
                case 2:  addCourse(); break;
                case 3:  enrollStudent(); break;
                case 4:  updateAttendance(); break;
                case 5:  enterAllMarks(); break;
                case 6:  generateMarkSheet(); break;
                case 7:  attendanceDefaulterReport(); break;
                case 8:  classPerformanceReport(); break;
                case 9:  viewAllStudents(); break;
                case 10: viewAllCourses(); break;
                case 11: listEnrollments(); break;
                case 0:
                    System.out.println("\n  Thank you for using KEC Evaluation System. Goodbye!\n");
                    return;
                default:
                    System.out.println("  [!] Invalid choice. Please try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n  ┌──────────────────────────────────────────────────────┐");
        System.out.println("  │                    MAIN MENU                         │");
        System.out.println("  ├──────────────────────────────────────────────────────┤");
        System.out.println("  │  DATA ENTRY                                         │");
        System.out.println("  │   1.  Add Student                                   │");
        System.out.println("  │   2.  Add Course                                    │");
        System.out.println("  │   3.  Enroll Student in Course                      │");
        System.out.println("  │   4.  Update Attendance                             │");
        System.out.println("  ├──────────────────────────────────────────────────────┤");
        System.out.println("  │  MARKS ENTRY                                        │");
        System.out.println("  │   5.  Enter Marks (CIA1 + CIA2 + Assignment + ESE)  │");
        System.out.println("  ├──────────────────────────────────────────────────────┤");
        System.out.println("  │  REPORTS                                            │");
        System.out.println("  │   6.  Generate Student Mark Sheet                   │");
        System.out.println("  │   7.  Attendance Defaulter Report                   │");
        System.out.println("  │   8.  Class Performance & Result Summary            │");
        System.out.println("  ├──────────────────────────────────────────────────────┤");
        System.out.println("  │  VIEW DATA                                          │");
        System.out.println("  │   9.  View All Students                             │");
        System.out.println("  │  10.  View All Courses                              │");
        System.out.println("  │  11.  List Student Enrollments                      │");
        System.out.println("  ├──────────────────────────────────────────────────────┤");
        System.out.println("  │   0.  Exit                                          │");
        System.out.println("  └──────────────────────────────────────────────────────┘");
    }
     private static void addStudent() {
        System.out.println("\n  --- Add New Student ---");
        String roll = readLine("  Roll Number (e.g., 24ITR005): ");
        String name = readLine("  Student Name: ");
        String dept = readLine("  Department (IT/CSE/ECE/MECH): ");
        int sem = readInt("  Current Semester: ");
        Student s = new Student(roll.toUpperCase(), name, dept.toUpperCase(), sem);
        if (studentDAO.addStudent(s)) {
            System.out.println("  [SUCCESS] Student '" + name + "' (" + roll.toUpperCase() + ") added successfully.");
        }
    }
    private static void addCourse() {
        System.out.println("\n  --- Add New Course ---");
        String code = readLine("  Course Code (e.g., 24ITR33): ");
        String title = readLine("  Course Title: ");
        int credits = readInt("  Credits: ");
        String type = readLine("  Course Type (THEORY/PRACTICAL/EMBEDDED): ");
        Course c = new Course(code.toUpperCase(), title, credits, type.toUpperCase());
        if (courseDAO.addCourse(c)) {
            System.out.println("  [SUCCESS] Course '" + title + "' (" + code.toUpperCase() + ") added successfully.");
        }
    }
    private static void enrollStudent() {
        System.out.println("\n  --- Enroll Student in Course ---");
        String roll = readLine("  Student Roll Number: ");
        String code = readLine("  Course Code: ");
        int sem = readInt("  Semester: ");
        if (academicDAO.enrollStudent(roll, code, sem)) {
            System.out.println("  [SUCCESS] Student " + roll + " enrolled in " + code + " for Semester " + sem + ".");
        }
    }
    private static void updateAttendance() {
        System.out.println("\n  --- Update Attendance ---");
        String roll = readLine("  Student Roll Number: ");
        int sem = readInt("  Semester: ");
        System.out.println("  Enrollments for " + roll + ":");
        academicDAO.listEnrollments(roll, sem);
        int eid = readInt("  Enter Enrollment ID to update: ");
        int total = readInt("  Total Classes Held: ");
        int attended = readInt("  Classes Attended: ");
        if (academicDAO.updateAttendance(eid, total, attended)) {
            double pct = total == 0 ? 0 : (attended * 100.0) / total;
            System.out.printf("  [SUCCESS] Attendance updated. Percentage: %.1f%%%n", pct);
            if (pct < 65) System.out.println("  [WARNING] Below 65% – Student will be awarded SA grade!");
            else if (pct < 75) System.out.println("  [NOTE] Between 65-75% – Conditional eligibility (Medical Condonation required).");
        }
    }
    private static void enterAllMarks() {
        System.out.println("\n  --- Enter Marks (CIA1 + CIA2 + Assignment + ESE) ---");
        String roll = readLine("  Student Roll Number: ");
        int sem = readInt("  Semester: ");
        System.out.println("  Enrollments for " + roll + ":");
        academicDAO.listEnrollments(roll, sem);
        int eid = readInt("  Enter Enrollment ID: ");
        academicDAO.showCurrentMarks(eid);
        System.out.println();
        System.out.println("  Enter all marks below (each out of 100):");
        int cia1 = readInt("  CIA Test 1 Marks       (0-100): ");
        int cia2 = readInt("  CIA Test 2 Marks       (0-100): ");
        int cia3 = readInt("  Assignment/Quiz Marks  (0-100): ");
        int ese  = readInt("  End Semester Exam (ESE) (0-100): ");
        boolean ok = true;
        ok &= academicDAO.recordMarks(eid, "CIA1", cia1);
        ok &= academicDAO.recordMarks(eid, "CIA2", cia2);
        ok &= academicDAO.recordMarks(eid, "CIA3", cia3);
        ok &= academicDAO.recordMarks(eid, "ESE",  ese);
        if (ok) {
            System.out.println();
            System.out.println("  [SUCCESS] All marks recorded for Enrollment ID " + eid + ":");
            System.out.println("            CIA1 = " + cia1 + "/100  |  CIA2 = " + cia2 + "/100  |  Assignment = " + cia3 + "/100  |  ESE = " + ese + "/100");
        } else {
            System.out.println("  [ERROR] Some marks could not be saved. Check for errors above.");
        }
    }
    private static void generateMarkSheet() {
        System.out.println("\n  --- Generate Student Semester Mark Sheet ---");
        String roll = readLine("  Student Roll Number: ");
        int sem = readInt("  Semester: ");
        academicDAO.generateMarkSheet(roll, sem);
    }
    private static void attendanceDefaulterReport() {
        System.out.println("\n  --- Attendance Defaulter Report ---");
        int sem = readInt("  Semester: ");
        academicDAO.attendanceDefaulterReport(sem);
    }
    private static void classPerformanceReport() {
        System.out.println("\n  --- Class Performance & Result Summary ---");
        int sem = readInt("  Semester: ");
        academicDAO.classPerformanceReport(sem);
    }
    private static void viewAllStudents() {
        System.out.println("\n  --- All Registered Students ---");
        java.util.List<Student> students = studentDAO.getAllStudents();
        System.out.println("  +------------+----------------------+------+-----+");
        System.out.println("  | Roll No    | Name                 | Dept | Sem |");
        System.out.println("  +------------+----------------------+------+-----+");
        for (Student s : students) {
            System.out.printf("  | %-10s | %-20s | %-4s | %-3d |%n",
                    s.getRollNumber(), s.getName(), s.getDepartment(), s.getCurrentSemester());
        }
        System.out.println("  +------------+----------------------+------+-----+");
        System.out.println("  Total: " + students.size() + " students.");
    }
    private static void viewAllCourses() {
        System.out.println("\n  --- All Registered Courses ---");
        java.util.List<Course> courses = courseDAO.getAllCourses();
        System.out.println("  +----------+-----------------------------------+------+-----------+");
        System.out.println("  | Code     | Title                             | Cred | Type      |");
        System.out.println("  +----------+-----------------------------------+------+-----------+");
        for (Course c : courses) {
            System.out.printf("  | %-8s | %-33s | %-4d | %-9s |%n",
                    c.getCourseCode(), c.getTitle(), c.getCredits(), c.getCourseType());
        }
        System.out.println("  +----------+-----------------------------------+------+-----------+");
        System.out.println("  Total: " + courses.size() + " courses.");
    }
     private static void listEnrollments() {
        System.out.println("\n  --- List Student Enrollments ---");
        String roll = readLine("  Student Roll Number: ");
        int sem = readInt("  Semester: ");
        academicDAO.listEnrollments(roll, sem);
    }
    private static String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    private static int readInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("  [!] Please enter a valid integer.");
            }
        }
    }
}
