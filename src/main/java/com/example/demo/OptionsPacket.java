package com.example.demo;

public class OptionsPacket {
    private String option;
    private Double percent;
    private String imagePath;

    public OptionsPacket(String option, Double percent, String imagePath) {
        this.option = option;
        this.percent = percent;
        this.imagePath = imagePath;
    }

    public String getOption() {
        return option;
    }

    public Double getPercent() {
        return percent;
    }


    public String getImagePath() {
        return imagePath;
    }

    public void setOption(String option) {
        this.option = option;
    }
}
