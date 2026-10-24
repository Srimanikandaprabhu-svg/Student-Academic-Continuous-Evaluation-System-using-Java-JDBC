package com.kec.dao;
import com.kec.model.Student;
import com.kec.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class StudentDAO {
    public boolean addStudent(Student student) {
        String sql = "INSERT INTO students (roll_number, name, department, current_semester) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getRollNumber());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getDepartment());
            stmt.setInt(4, student.getCurrentSemester());
            int rows = stmt.executeUpdate();
            return rows == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Student with this roll number already exists.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Student getStudent(String rollNumber) {
        String sql = "SELECT roll_number, name, department, current_semester FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getString("roll_number"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("current_semester"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public List<Student> getAllStudents() {
        List<Student> list = new ArrayList<>();
        String sql = "SELECT roll_number, name, department, current_semester FROM students";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Student(
                        rs.getString("roll_number"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getInt("current_semester")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public boolean updateStudent(Student student) {
        String sql = "UPDATE students SET name = ?, department = ?, current_semester = ? WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getDepartment());
            stmt.setInt(3, student.getCurrentSemester());
            stmt.setString(4, student.getRollNumber());
            int rows = stmt.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deleteStudent(String rollNumber) {
        String sql = "DELETE FROM students WHERE roll_number = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, rollNumber);
            int rows = stmt.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
