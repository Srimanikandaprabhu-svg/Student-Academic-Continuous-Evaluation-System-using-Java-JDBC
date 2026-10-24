package com.kec.model;

public class Student {
    private String rollNumber;
    private String name;
    private String department;
    private int currentSemester;

    public Student(String rollNumber, String name, String department, int currentSemester) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.department = department;
        this.currentSemester = currentSemester;
    }

    public String getRollNumber() { return rollNumber; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public int getCurrentSemester() { return currentSemester; }
}
