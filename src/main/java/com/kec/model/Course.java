package com.kec.model;

public class Course {
    private String courseCode;
    private String title;
    private int credits;
    private String courseType; // THEORY, PRACTICAL, EMBEDDED

    public Course(String courseCode, String title, int credits, String courseType) {
        this.courseCode = courseCode;
        this.title = title;
        this.credits = credits;
        this.courseType = courseType;
    }

    public String getCourseCode() { return courseCode; }
    public String getTitle() { return title; }
    public int getCredits() { return credits; }
    public String getCourseType() { return courseType; }
}
