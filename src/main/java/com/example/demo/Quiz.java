package com.example.demo;

public class Quiz {
    private int QuizID;
    private String name;
    private double time;
    private Boolean shuffle;
    private double totalMarks;

    public Quiz() {
    }

    public int getQuizID() {
        return QuizID;
    }

    public void setQuizID(int quizID) {
        QuizID = quizID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public Boolean getShuffle() {
        return shuffle;
    }

    public void setShuffle(Boolean shuffle) {
        this.shuffle = shuffle;
    }

    public double getTotalMarks() {
        return totalMarks;
    }

    public void setTotalMarks(double totalMarks) {
        this.totalMarks = totalMarks;
    }
}
