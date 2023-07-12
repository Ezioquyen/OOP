package com.example.demo;

import java.util.*;

public class Question {
    private int categoryID;
    private int id;
    private int type;
    private String title = "";
    private List<String> options;

    private Double mark;
    private List<Integer> ansID;
    private List<Integer> imageID;


    public void setOptions(List<String> options) {
        this.options = options;
    }

    public void setPercent(List<Double> percent) {
        this.percent = percent;
    }

    private List<Double> percent;
    private final List<String> imageFilePath;
    private String gifPath;
    private String videoPath;
    private List<String> imageOptionPath;
    private List<OptionsPacket> packets = new ArrayList<>();

    public Question() {
        this.imageFilePath = new ArrayList<>();
        this.imageOptionPath = new ArrayList<>();
        this.options = new ArrayList<>();
        this.percent = new ArrayList<>();
        this.ansID = new ArrayList<>();
        this.imageID = new ArrayList<>();
    }

    public void match() {
        int i = 0;
        for (int ignored : ansID) {
            OptionsPacket pack = new OptionsPacket(options.get(i), percent.get(i), imageOptionPath.get(i));
            packets.add(pack);
            i++;
        }
    }

    public void shuffle() {
        Collections.shuffle(packets);
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

    public void setGifPath(String gifPath) {
        this.gifPath = gifPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getGifPath() {
        return gifPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath.add(imageFilePath);
    }

    public List<String> getImageOptionPath() {
        return imageOptionPath;
    }

    public void setImageOptionPath(List<String> imageOptionPath) {
        this.imageOptionPath = imageOptionPath;
    }

    public List<Integer> getImageID() {
        return imageID;
    }

    public List<OptionsPacket> getPackets() {
        return packets;
    }
}
