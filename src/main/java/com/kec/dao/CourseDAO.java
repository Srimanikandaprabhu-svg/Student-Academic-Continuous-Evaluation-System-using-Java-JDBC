package com.kec.dao;

import com.kec.model.Course;
import com.kec.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for {@link Course} management.
 */
public class CourseDAO {
    /** Inserts a new course. */
    public boolean addCourse(Course course) {
        String sql = "INSERT INTO courses (course_code, title, credits, course_type) VALUES (?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, course.getCourseCode());
            stmt.setString(2, course.getTitle());
            stmt.setInt(3, course.getCredits());
            stmt.setString(4, course.getCourseType());
            int rows = stmt.executeUpdate();
            return rows == 1;
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Course with this code already exists.");
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Course getCourse(String courseCode) {
        String sql = "SELECT course_code, title, credits, course_type FROM courses WHERE course_code = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, courseCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Course(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getString("course_type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Course> getAllCourses() {
        List<Course> list = new ArrayList<>();
        String sql = "SELECT course_code, title, credits, course_type FROM courses";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Course(
                        rs.getString("course_code"),
                        rs.getString("title"),
                        rs.getInt("credits"),
                        rs.getString("course_type")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
