package com.example.demo;

import java.util.ArrayList;
import java.util.List;

public class AikenQuestion {
    private final List<String> title;
    private final List<String> options;
    private String answer;
    /*private String imageFilePath;*/

    public AikenQuestion() {
        this.options = new ArrayList<>();
        this.title = new ArrayList<>();
    }

    public List<String> getTitle() {
        return title;
    }

    public void addTitle(String title) {
        this.title.add(title);
    }

    public List<String> getOptions() {
        return options;
    }

    public void addOption(String option) {
        this.options.add(option);
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /*public String getImageFilePath() {
        return imageFilePath;
    }*/

    /*public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }*/
}
