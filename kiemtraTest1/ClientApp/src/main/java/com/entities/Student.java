package com.entities;

import java.io.Serializable;
import java.util.regex.Pattern;


public class Student implements Serializable{
    private static Pattern DATE_PATTERN = Pattern.compile(
            "^\\d{4}-\\d{2}-\\d{2}$");
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String dateOfBirth;
    private static String className;

    public Student() {
    }

    public Student(int id, String name, String dateOfBirth) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public static String getClassName() {
        return className;
    }

    public static void setClassName(String className) {
        Student.className = className;
    }

    @Override
    public String toString() {
        return String.format("\t Student --> ID: %d,Name: %s, DateOfBirth: %s",getId(),getName(),getDateOfBirth());
    }


}
