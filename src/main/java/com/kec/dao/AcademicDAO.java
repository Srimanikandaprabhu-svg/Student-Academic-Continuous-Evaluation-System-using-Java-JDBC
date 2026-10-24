package com.kec.dao;

import com.kec.DBConnection;
import com.kec.model.Course;
import com.kec.model.Enrollment;
import com.kec.model.Student;

import java.sql.*;
import java.util.*;

/**
 * AcademicDAO handles enrollment, attendance, incremental marks entry and the
 * evaluation engine (grading, SGPA calculation, reports) as per KEC R2024 Regulation.
 */
public class AcademicDAO {

    // ======================== ENROLLMENT ========================

    /** Enroll a student into a course for a given semester. */
    public boolean enrollStudent(String rollNumber, String courseCode, int semester) {
        String sql = "INSERT INTO enrollments (roll_number, course_code, semester, total_classes, attended_classes) VALUES (?,?,?,0,0)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            stmt.setString(2, courseCode);
            stmt.setInt(3, semester);
            stmt.executeUpdate();
            return true;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("  [ERROR] Student already enrolled in this course for the semester.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Get enrollment ID for a student-course-semester combination. */
    public Integer getEnrollmentId(String rollNumber, String courseCode, int semester) {
        String sql = "SELECT enrollment_id FROM enrollments WHERE roll_number = ? AND course_code = ? AND semester = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            stmt.setString(2, courseCode);
            stmt.setInt(3, semester);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("enrollment_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /** List all enrollments for a student in a semester (with enrollment IDs). */
    public void listEnrollments(String rollNumber, int semester) {
        String sql = "SELECT e.enrollment_id, e.course_code, c.title, c.credits, c.course_type, " +
                     "e.total_classes, e.attended_classes " +
                     "FROM enrollments e JOIN courses c ON e.course_code = c.course_code " +
                     "WHERE e.roll_number = ? AND e.semester = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            stmt.setInt(2, semester);
            ResultSet rs = stmt.executeQuery();
            System.out.println("  +------+----------+-----------------------------------+------+-----------+");
            System.out.println("  | E.ID | Code     | Title                             | Cred | Type      |");
            System.out.println("  +------+----------+-----------------------------------+------+-----------+");
            while (rs.next()) {
                System.out.printf("  | %-4d | %-8s | %-33s | %-4d | %-9s |%n",
                        rs.getInt("enrollment_id"),
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getString("course_type"));
            }
            System.out.println("  +------+----------+-----------------------------------+------+-----------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================== ATTENDANCE ========================

    /** Update attendance for a particular enrollment. */
    public boolean updateAttendance(int enrollmentId, int totalClasses, int attendedClasses) {
        String sql = "UPDATE enrollments SET total_classes = ?, attended_classes = ? WHERE enrollment_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, totalClasses);
            stmt.setInt(2, attendedClasses);
            stmt.setInt(3, enrollmentId);
            return stmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ======================== MARKS ENTRY ========================

    /** Record marks for a specific phase (CIA1, CIA2, CIA3, ESE). Uses UPSERT. */
    public boolean recordMarks(int enrollmentId, String phase, int marks) {
        if (marks < 0 || marks > 100) {
            System.err.println("  [ERROR] Marks must be between 0 and 100.");
            return false;
        }
        String sql = "INSERT INTO assessment_marks (enrollment_id, phase, marks) VALUES (?,?,?) " +
                     "ON DUPLICATE KEY UPDATE marks = VALUES(marks)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);
            stmt.setString(2, phase);
            stmt.setInt(3, marks);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /** Show currently recorded marks for an enrollment. */
    public void showCurrentMarks(int enrollmentId) {
        String sql = "SELECT phase, marks FROM assessment_marks WHERE enrollment_id = ? ORDER BY FIELD(phase,'CIA1','CIA2','CIA3','ESE')";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                if (!found) {
                    System.out.println("  Currently recorded marks:");
                    found = true;
                }
                System.out.printf("    %s : %d/100%n", rs.getString("phase"), rs.getInt("marks"));
            }
            if (!found) System.out.println("  No marks recorded yet for this enrollment.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================== EVALUATION ENGINE ========================

    /** Retrieve all phase marks for an enrollment. */
    private Map<String, Integer> getPhaseMarks(int enrollmentId, Connection conn) throws SQLException {
        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT phase, marks FROM assessment_marks WHERE enrollment_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, enrollmentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                map.put(rs.getString("phase"), rs.getInt("marks"));
            }
        }
        return map;
    }

    /** Calculate CIA weighted score (out of CIA weight portion). */
    private double computeCiaWeighted(Map<String, Integer> marks, double ciaWeight) {
        int count = 0; double sum = 0;
        for (String p : new String[]{"CIA1", "CIA2", "CIA3"}) {
            Integer m = marks.get(p);
            if (m != null) { sum += m; count++; }
        }
        double ciaAvg = count == 0 ? 0 : sum / count;
        return ciaAvg * ciaWeight;                    // e.g. 80 * 0.40 = 32
    }

    /** Calculate ESE weighted score (out of ESE weight portion). */
    private double computeEseWeighted(Map<String, Integer> marks, double eseWeight) {
        Integer ese = marks.get("ESE");
        return ese == null ? 0 : ese * eseWeight;     // e.g. 90 * 0.60 = 54
    }

    /** Check if ESE minimum threshold is met. */
    private boolean isEseMinMet(Map<String, Integer> marks, String courseType) {
        Integer ese = marks.get("ESE");
        if (ese == null) return false;   // absent
        switch (courseType) {
            case "THEORY":    return ese >= 45;   // 45% of 100 raw → 27/60 weighted
            case "PRACTICAL": return ese >= 50;   // 50% of 100 raw → 20/40 weighted
            case "EMBEDDED":  return ese >= 45;   // 45% of 100 raw → 22.5/50 weighted
            default: return false;
        }
    }

    /** Get weight pair [ciaWeight, eseWeight] for a course type. */
    private double[] getWeights(String courseType) {
        switch (courseType) {
            case "THEORY":    return new double[]{0.40, 0.60};
            case "PRACTICAL": return new double[]{0.60, 0.40};
            case "EMBEDDED":  return new double[]{0.50, 0.50};
            default:          return new double[]{0.50, 0.50};
        }
    }

    /** Map numeric score (0-100) to letter grade. */
    private String letterGrade(double score) {
        if (score >= 91) return "O";
        if (score >= 81) return "A+";
        if (score >= 71) return "A";
        if (score >= 61) return "B+";
        if (score >= 50) return "B";
        return "RA";
    }

    /** Map numeric score to grade point. */
    private int gradePoint(double score) {
        if (score >= 91) return 10;
        if (score >= 81) return 9;
        if (score >= 71) return 8;
        if (score >= 61) return 7;
        if (score >= 50) return 6;
        return 0;
    }

    // ======================== REPORT 1: MARK SHEET ========================

    public void generateMarkSheet(String rollNumber, int semester) {
        StudentDAO sDao = new StudentDAO();
        Student student = sDao.getStudent(rollNumber);
        if (student == null) { System.out.println("  [ERROR] Student not found."); return; }

        String sql = "SELECT e.enrollment_id, e.course_code, c.title, c.credits, c.course_type, " +
                     "e.total_classes, e.attended_classes " +
                     "FROM enrollments e JOIN courses c ON e.course_code = c.course_code " +
                     "WHERE e.roll_number = ? AND e.semester = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            stmt.setInt(2, semester);
            ResultSet rs = stmt.executeQuery();

            // Header
            System.out.println();
            System.out.println("  ╔══════════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("  ║              KONGU ENGINEERING COLLEGE (AUTONOMOUS), PERUNDURAI - 638060                        ║");
            System.out.println("  ║                   STUDENT SEMESTER MARK SHEET (R2024)                                          ║");
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("  ║  Roll No   : %-20s   Name       : %-40s  ║%n", student.getRollNumber(), student.getName());
            System.out.printf("  ║  Department: %-20s   Semester   : %-40d  ║%n", student.getDepartment(), semester);
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("  ║  Code     | Title                      | Cred | Att%  | CIA  | ESE  | Total | Grade | GP | P/F ║");
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣");

            double sumCredits = 0, sumWeightedGP = 0;
            int passCount = 0, failCount = 0;

            while (rs.next()) {
                int eid = rs.getInt("enrollment_id");
                String code = rs.getString("course_code");
                String title = rs.getString("title");
                int credits = rs.getInt("credits");
                String cType = rs.getString("course_type");
                int totalCl = rs.getInt("total_classes");
                int attendCl = rs.getInt("attended_classes");
                double attPct = totalCl == 0 ? 0 : (attendCl * 100.0) / totalCl;

                Map<String, Integer> marks = getPhaseMarks(eid, conn);
                double[] w = getWeights(cType);

                double ciaW = computeCiaWeighted(marks, w[0]);
                double eseW = computeEseWeighted(marks, w[1]);
                double total = ciaW + eseW;

                String grade; int gp; String pf;

                if (attPct < 65) {
                    // Shortage of Attendance
                    grade = "SA"; gp = 0; pf = "FAIL";
                } else if (marks.get("ESE") == null) {
                    // Absent in ESE
                    grade = "AB"; gp = 0; pf = "FAIL";
                } else if (!isEseMinMet(marks, cType)) {
                    // ESE minimum not met
                    grade = "RA"; gp = 0; pf = "FAIL";
                } else if (total < 50) {
                    // Overall < 50
                    grade = "RA"; gp = 0; pf = "FAIL";
                } else {
                    grade = letterGrade(total);
                    gp = gradePoint(total);
                    pf = gp > 0 ? "PASS" : "FAIL";
                }

                sumCredits += credits;
                sumWeightedGP += credits * gp;
                if (pf.equals("PASS")) passCount++; else failCount++;

                System.out.printf("  ║  %-8s | %-26s | %-4d | %5.1f | %4.1f | %4.1f | %5.1f | %-5s | %2d | %-4s║%n",
                        code, title, credits, attPct, ciaW, eseW, total, grade, gp, pf);
            }

            double sgpa = sumCredits == 0 ? 0 : sumWeightedGP / sumCredits;
            double pct = sgpa * 10;

            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("  ║  Total Credits: %-4.0f   |   Passed: %d   |   Failed: %d                                         ║%n", sumCredits, passCount, failCount);
            System.out.printf("  ║  SGPA: %-6.2f          |   Equivalent Percentage: %.2f%%                                       ║%n", sgpa, pct);
            System.out.println("  ╚══════════════════════════════════════════════════════════════════════════════════════════════════╝");
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================== REPORT 2: ATTENDANCE DEFAULTERS ========================

    public void attendanceDefaulterReport(int semester) {
        String sql = "SELECT s.roll_number, s.name, s.department, e.course_code, c.title, " +
                     "e.total_classes, e.attended_classes " +
                     "FROM enrollments e " +
                     "JOIN students s ON e.roll_number = s.roll_number " +
                     "JOIN courses c ON e.course_code = c.course_code " +
                     "WHERE e.semester = ? " +
                     "ORDER BY s.department, s.roll_number";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, semester);
            ResultSet rs = stmt.executeQuery();

            System.out.println();
            System.out.println("  ╔══════════════════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("  ║           ATTENDANCE DEFAULTER REPORT - SEMESTER " + semester + "  (Attendance < 65%)                   ║");
            System.out.println("  ║           Students flagged with SA (Shortage of Attendance) Grade                          ║");
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("  ║  Roll No    | Name              | Dept | Course Code | Course Title             | Att%     ║");
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣");

            int count = 0;
            while (rs.next()) {
                int total = rs.getInt("total_classes");
                int attended = rs.getInt("attended_classes");
                double pct = total == 0 ? 0 : (attended * 100.0) / total;
                if (pct < 65) {
                    count++;
                    System.out.printf("  ║  %-10s | %-17s | %-4s | %-11s | %-24s | %5.1f%%  ║%n",
                            rs.getString("roll_number"),
                            rs.getString("name"),
                            rs.getString("department"),
                            rs.getString("course_code"),
                            rs.getString("title"),
                            pct);
                }
            }
            if (count == 0) {
                System.out.println("  ║  No defaulters found.                                                                    ║");
            }
            System.out.println("  ╠══════════════════════════════════════════════════════════════════════════════════════════════╣");
            System.out.printf("  ║  Total SA Entries: %-4d                                                                    ║%n", count);
            System.out.println("  ╚══════════════════════════════════════════════════════════════════════════════════════════════╝");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ======================== REPORT 3: CLASS PERFORMANCE & RESULT SUMMARY ========================

    public void classPerformanceReport(int semester) {
        // For each department: count pass, fail, compute avg SGPA
        String deptSql = "SELECT DISTINCT s.department FROM students s " +
                         "JOIN enrollments e ON s.roll_number = e.roll_number WHERE e.semester = ? ORDER BY s.department";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement deptStmt = conn.prepareStatement(deptSql)) {
            deptStmt.setInt(1, semester);
            ResultSet deptRs = deptStmt.executeQuery();
            List<String> departments = new ArrayList<>();
            while (deptRs.next()) departments.add(deptRs.getString("department"));

            System.out.println();
            System.out.println("  ╔════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("  ║         CLASS PERFORMANCE & RESULT SUMMARY - SEMESTER " + semester + "                    ║");
            System.out.println("  ╠════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("  ║  Department | Students | All Pass | Partial | All Fail | Avg SGPA | Avg %    ║");
            System.out.println("  ╠════════════════════════════════════════════════════════════════════════════════╣");

            for (String dept : departments) {
                // Get all students in this dept enrolled in this semester
                String stuSql = "SELECT DISTINCT e.roll_number FROM enrollments e " +
                                "JOIN students s ON e.roll_number = s.roll_number " +
                                "WHERE s.department = ? AND e.semester = ?";
                PreparedStatement stuStmt = conn.prepareStatement(stuSql);
                stuStmt.setString(1, dept);
                stuStmt.setInt(2, semester);
                ResultSet stuRs = stuStmt.executeQuery();

                int totalStudents = 0, allPassCount = 0, allFailCount = 0, partialCount = 0;
                double sgpaSum = 0;

                while (stuRs.next()) {
                    totalStudents++;
                    String rn = stuRs.getString("roll_number");
                    double[] result = computeStudentResult(rn, semester, conn);
                    // result[0] = sgpa, result[1] = passCount, result[2] = failCount
                    sgpaSum += result[0];
                    if (result[2] == 0) allPassCount++;
                    else if (result[1] == 0) allFailCount++;
                    else partialCount++;
                }
                stuStmt.close();

                double avgSgpa = totalStudents == 0 ? 0 : sgpaSum / totalStudents;
                System.out.printf("  ║  %-11s | %-8d | %-8d | %-7d | %-8d | %6.2f   | %5.1f%%  ║%n",
                        dept, totalStudents, allPassCount, partialCount, allFailCount, avgSgpa, avgSgpa * 10);
            }

            System.out.println("  ╚════════════════════════════════════════════════════════════════════════════════╝");

            // Per-student detail
            System.out.println();
            System.out.println("  ╔════════════════════════════════════════════════════════════════════════════════╗");
            System.out.println("  ║                     STUDENT-WISE SGPA DETAILS                                ║");
            System.out.println("  ╠════════════════════════════════════════════════════════════════════════════════╣");
            System.out.println("  ║  Roll No    | Name              | Dept | SGPA  | Pct%   | Status             ║");
            System.out.println("  ╠════════════════════════════════════════════════════════════════════════════════╣");

            String allStu = "SELECT DISTINCT e.roll_number, s.name, s.department " +
                            "FROM enrollments e JOIN students s ON e.roll_number = s.roll_number " +
                            "WHERE e.semester = ? ORDER BY s.department, e.roll_number";
            PreparedStatement allStmt = conn.prepareStatement(allStu);
            allStmt.setInt(1, semester);
            ResultSet allRs = allStmt.executeQuery();
            while (allRs.next()) {
                String rn = allRs.getString("roll_number");
                String name = allRs.getString("name");
                String dept = allRs.getString("department");
                double[] result = computeStudentResult(rn, semester, conn);
                String status = result[2] == 0 ? "ALL CLEAR" : (result[1] == 0 ? "ALL FAIL" : "PARTIAL");
                System.out.printf("  ║  %-10s | %-17s | %-4s | %5.2f | %5.1f%% | %-18s ║%n",
                        rn, name, dept, result[0], result[0] * 10, status);
            }
            allStmt.close();

            System.out.println("  ╚════════════════════════════════════════════════════════════════════════════════╝");
            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compute student result for a semester.
     * Returns double[3]: [sgpa, passCount, failCount]
     */
    private double[] computeStudentResult(String rollNumber, int semester, Connection conn) throws SQLException {
        String sql = "SELECT e.enrollment_id, c.credits, c.course_type, e.total_classes, e.attended_classes " +
                     "FROM enrollments e JOIN courses c ON e.course_code = c.course_code " +
                     "WHERE e.roll_number = ? AND e.semester = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, rollNumber);
        stmt.setInt(2, semester);
        ResultSet rs = stmt.executeQuery();

        double sumCredits = 0, sumWeightedGP = 0;
        int passCount = 0, failCount = 0;

        while (rs.next()) {
            int eid = rs.getInt("enrollment_id");
            int credits = rs.getInt("credits");
            String cType = rs.getString("course_type");
            int totalCl = rs.getInt("total_classes");
            int attendCl = rs.getInt("attended_classes");
            double attPct = totalCl == 0 ? 0 : (attendCl * 100.0) / totalCl;

            Map<String, Integer> marks = getPhaseMarks(eid, conn);
            double[] w = getWeights(cType);
            double total = computeCiaWeighted(marks, w[0]) + computeEseWeighted(marks, w[1]);
            int gp;

            if (attPct < 65 || marks.get("ESE") == null || !isEseMinMet(marks, cType) || total < 50) {
                gp = 0; failCount++;
            } else {
                gp = gradePoint(total);
                if (gp > 0) passCount++; else failCount++;
            }
            sumCredits += credits;
            sumWeightedGP += credits * gp;
        }
        stmt.close();

        double sgpa = sumCredits == 0 ? 0 : sumWeightedGP / sumCredits;
        return new double[]{sgpa, passCount, failCount};
    }
}
