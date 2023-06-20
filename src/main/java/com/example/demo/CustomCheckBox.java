package com.example.demo;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

public class CustomCheckBox extends HBox {
    private final CheckBox checkBox = new CheckBox("");
    private final MenuButton menuButton = new MenuButton("Edit");

    public CustomCheckBox(String text) {
        setPrefHeight(30);
        setAlignment(Pos.CENTER_LEFT);
        setPadding(new Insets(0, 0, 0, 5));
        setSpacing(5);
        Label label = new Label();
        label.setText(text);
        label.setMaxWidth(1000);
        label.setWrapText(true);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);

        menuButton.setTextFill(Color.valueOf("#00ACEA"));
        menuButton.getStyleClass().add("checkBox-MenuButton");
        Region region = new Region();
        FontIcon fontIcon = new FontIcon("fas-list-ul");
        getChildren().addAll(checkBox, fontIcon, label, region, menuButton);
        setHgrow(region, Priority.ALWAYS);
    }

    public MenuButton getMenuButton() {
        return menuButton;
    }

    public CheckBox getCheckBox() {
        return checkBox;
    }
}
