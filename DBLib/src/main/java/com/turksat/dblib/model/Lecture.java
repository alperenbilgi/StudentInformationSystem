package com.turksat.dblib.model;

public class Lecture extends User {

    private int lectureID;
    private String lectureName;
    private int lectureCredit;

    public int getLectureID() {
        return lectureID;
    }

    public void setLectureID(int lectureID) {
        this.lectureID = lectureID;
    }

    public String getLectureName() {
        return lectureName;
    }

    public void setLectureName(String lectureName) {
        this.lectureName = lectureName;
    }

    public int getLectureCredit() {
        return lectureCredit;
    }

    public void setLectureCredit(int lectureCredit) {
        this.lectureCredit = lectureCredit;
    }
}
