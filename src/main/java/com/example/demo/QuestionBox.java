package com.example.demo;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

public class QuestionBox extends HBox {
    private DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat decimalFormat = new DecimalFormat("#0.00", decimalFormatSymbols);
    private final Question question;
    private final Label number = new Label();
    private final VBox box1 = new VBox(15);

    private final Button flag = new Button("Flag");
    private Label label;
    private final SimpleBooleanProperty selected = new SimpleBooleanProperty(false);
    private boolean triggered = false;

    private final List<OptionsPacket> selectedOps = new ArrayList<>();
    private final Map<CheckBox, OptionsPacket> checkBoxOptionsPacketMap = new HashMap<>();
    private final Map<RadioButton, OptionsPacket> packetMap = new HashMap<>();

    public void showAns() {
        HBox ans = new HBox();
        ans.setAlignment(Pos.CENTER_LEFT);
        ans.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        ans.setStyle("-fx-background-color: #FCEFDC");
        ans.setPadding(new Insets(0, 0, 0, 20));
        label = new Label();
        label.setMinHeight(50);
        label.setWrapText(true);
        label.setPrefWidth(720);
        label.setStyle("-fx-text-fill: #695442");
        ans.getChildren().add(label);
        box1.getChildren().add(ans);
    }

    public Label getLabel() {
        return label;
    }

    public QuestionBox(Question question) {
        this.question = question;
        setId("box");
        //
        setPrefWidth(877.0);
        setSpacing(20);
        setPrefHeight(USE_COMPUTED_SIZE);
        setMaxSize(USE_COMPUTED_SIZE, USE_COMPUTED_SIZE);
        //
        VBox box = new VBox();
        box.setPrefSize(116, 288.0);

        VBox boxChild = new VBox(5);
        boxChild.setPadding(new Insets(10));
        boxChild.setStyle("-fx-border-color: #DFDFDF");

        HBox boxChild1 = new HBox();
        boxChild1.setAlignment(Pos.BOTTOM_LEFT);
        Label questionLabel = new Label("Question ");
        questionLabel.setStyle("-fx-font-size: 14");
        questionLabel.setStyle("-fx-text-fill:#c02424");
        number.setAlignment(Pos.BOTTOM_LEFT);
        number.setStyle("-fx-font-weight: bold ");
        number.setStyle("-fx-font-size: 20");
        number.setStyle("-fx-text-fill: #c02424");
        boxChild1.getChildren().addAll(questionLabel, number);

        Label answered = new Label("Not answered");
        answered.setStyle("-fx-text-fill: black");

        Label markForQues = new Label("Marked out of " + decimalFormat.format(question.getMark()));
        markForQues.setWrapText(true);
        markForQues.setStyle("-fx-text-fill: black");
        flag.setPrefSize(97, 26);
        FontIcon flagIcon = new FontIcon("fas-flag");
        flag.setGraphic(flagIcon);
        flag.setContentDisplay(ContentDisplay.LEFT);
        flag.setStyle("-fx-font-size: 10.0");
        flag.setStyle("-fx-background-color: white");

        Label editQuestion = new Label("Edit question");
        editQuestion.setStyle("-fx-text-fill: #009fe5");
        FontIcon cog = new FontIcon("fas-cog");
        cog.setIconColor(Color.valueOf("#009fe5"));
        cog.setIconSize(15);
        editQuestion.setGraphic(cog);

        boxChild.getChildren().addAll(boxChild1, answered, markForQues, flag, editQuestion);

        box.getChildren().add(boxChild);
        //
        setHgrow(box1, Priority.ALWAYS);

        VBox questionContainer = new VBox();
        questionContainer.setSpacing(5);
        questionContainer.setPrefSize(592, 225);
        questionContainer.setStyle("-fx-background-color: #E7F3F5");
        questionContainer.setPadding(new Insets(10));

        HBox hBox = new HBox();
        Label title = new Label();
        title.setPrefWidth(720);
        title.setStyle("-fx-font-size: 15");
        title.setWrapText(true);
        title.setStyle("-fx-text-fill: black");

        hBox.getChildren().add(title);
        hBox.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        questionContainer.getChildren().add(hBox);
        VBox optionsContainer = new VBox(15);
        optionsContainer.setPadding(new Insets(0, 0, 0, 15));
        if (question.getImageFilePath() != null) {
            FlowPane pane = new FlowPane();
            questionContainer.getChildren().add(pane);
            for (String path : question.getImageFilePath()) {
                ImageView imageView = new ImageView(path);
                /*double desiredWidth = 750;
                double desiredHeight = 450;
                imageView.setFitWidth(desiredWidth);
                imageView.setFitHeight(desiredHeight);*/
                imageView.setPreserveRatio(true);
                imageView.setPreserveRatio(true);
                pane.getChildren().add(imageView);
            }
        }
        if (question.getVideoPath() != null) {
            VideoPlayer videoPlayer = new VideoPlayer(new File(question.getVideoPath()));
            videoPlayer.setMaxSize(700, 450);
            questionContainer.getChildren().add(videoPlayer);
        }
        questionContainer.getChildren().add(optionsContainer);

        box1.getChildren().add(questionContainer);


        //
        title.setText(question.getTitle());

        if (question.isType() == 1) {
            ToggleGroup group = new ToggleGroup();
            int i = 0;
            for (OptionsPacket pack : question.getPackets()) {
                pack.setOption((char) (65 + i) + ". " + pack.getOption());
                RadioButton radioButton = new RadioButton(pack.getOption());
                radioButton.setContentDisplay(ContentDisplay.RIGHT);
                radioButton.setStyle("-fx-text-fill: black");
                packetMap.put(radioButton, pack);
                if (pack.getImagePath() != null) {
                    ImageView imageView = new ImageView(pack.getImagePath());
                    double desiredWidth = 250;
                    double desiredHeight = 250;
                    if (imageView.getFitWidth() > desiredWidth) imageView.setFitWidth(desiredWidth);
                    if (imageView.getFitWidth() > desiredHeight) imageView.setFitHeight(desiredHeight);
                    imageView.setPreserveRatio(true);
                    radioButton.setGraphic(imageView);
                }
                group.getToggles().add(radioButton);
                optionsContainer.getChildren().add(radioButton);
                i++;
            }
            group.selectedToggleProperty().addListener(e -> {
                selectedOps.clear();
                selected.set(true);
                selectedOps.add(packetMap.get((RadioButton) group.getSelectedToggle()));
            });
        } else if (question.isType() == 0) {
            List<CheckBox> list = new ArrayList<>();
            ObservableList<CheckBox> checkBoxObservableList = FXCollections.observableList(list);
            int i = 0;
            for (OptionsPacket pack : question.getPackets()) {
                pack.setOption((char) (65 + i) + ". " + pack.getOption());
                CheckBox checkBox = new CheckBox(pack.getOption());
                checkBox.setStyle("-fx-text-fill: black");
                checkBox.setContentDisplay(ContentDisplay.RIGHT);
                checkBoxOptionsPacketMap.put(checkBox, pack);
                if (pack.getImagePath() != null) {
                    ImageView imageView = new ImageView(pack.getImagePath());
                    double desiredWidth = 250;
                    double desiredHeight = 250;
                    if (imageView.getFitWidth() > desiredWidth) imageView.setFitWidth(desiredWidth);
                    if (imageView.getFitWidth() > desiredHeight) imageView.setFitHeight(desiredHeight);
                    imageView.setPreserveRatio(true);
                    checkBox.setGraphic(imageView);
                }
                optionsContainer.getChildren().add(checkBox);
                checkBox.selectedProperty().addListener(e -> {
                    if (checkBox.isSelected()) {
                        checkBoxObservableList.add(checkBox);
                        selectedOps.add(checkBoxOptionsPacketMap.get(checkBox));
                    } else {
                        checkBoxObservableList.remove(checkBox);
                        selectedOps.remove(checkBoxOptionsPacketMap.get(checkBox));
                    }
                });
                checkBoxObservableList.addListener((ListChangeListener<? super CheckBox>) e -> selected.set(!checkBoxObservableList.isEmpty()));
                i++;
            }


        }
        getChildren().addAll(box, box1);
    }

    public SimpleBooleanProperty selectedProperty() {
        return selected;
    }

    public Label getNumber() {
        return number;
    }

    public Button getFlag() {
        return flag;
    }

    public boolean isTriggered() {
        return triggered;
    }

    public void setTriggered(boolean triggered) {
        this.triggered = triggered;
    }

    public Question getQuestion() {
        return question;
    }

    public List<OptionsPacket> getSelectedOps() {
        return selectedOps;
    }
}
