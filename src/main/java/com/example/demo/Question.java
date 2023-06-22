package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private int categoryID;
    private int id;
    private int type;
    private String title = "";
    private List<String> options;

    private Double mark;
    private List<Integer> ansID;


    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setPercent(List<Double> percent) {
        this.percent = percent;
    }

    private List<Double> percent;
    /*private String imageFilePath;*/

    public Question() {
        this.options = new ArrayList<>();
        this.percent = new ArrayList<>();
        this.ansID = new ArrayList<>();
    }

    public String getTitle() {
        return title;
    }

    public void addTitle(String title) {
        this.title = String.join("", this.title, title);
    }

    public List<String> getOptions() {
        return options;
    }

    public List<Double> getPercent() {
        return percent;
    }

    public void addOption(String option) {
        this.options.add(option);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int isType() {
        return type;
    }

    public void typeDetect() {
        int count = 0;
        for (Double percent : getPercent()) {
            if (percent != 100.0 && percent != (double) 0) {
                this.type = 0;
                return;
            } else if (percent == 100.0) count++;
        }
        if (count == 1) {
            this.type = 1;
        } else this.type = 0;
    }

    public Double getMark() {
        return mark;
    }

    public void setMark(Double mark) {
        this.mark = mark;
    }

    public List<Integer> getAnsID() {
        return ansID;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAnsID(List<Integer> ansID) {
        this.ansID = ansID;
    }
    /*public String getImageFilePath() {
        return imageFilePath;
    }*/

    /*public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }*/
}
