package com.example.demo;

public class OptionsPacket {
    private String option;
    private Double percent;
    private int ID;
    private String imagePath;

    public OptionsPacket(String option, Double percent, int ID, String imagePath) {
        this.option = option;
        this.percent = percent;
        this.ID = ID;
        this.imagePath = imagePath;
    }

    public String getOption() {
        return option;
    }

    public Double getPercent() {
        return percent;
    }

    public int getID() {
        return ID;
    }

    public String getImagePath() {
        return imagePath;
    }
}
