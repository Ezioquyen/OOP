package com.example.demo;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.File;

public class VideoPlayer extends VBox {
    private MediaView mediaView = new MediaView();

    private Button playOrPause = new Button();

    private Button reset = new Button();

    private Button volume = new Button();

    private Slider volumeSlider = new Slider();

    private Slider mediaSlider = new Slider();

    private Media media;
    private final MediaPlayer mediaPlayer;

    public VideoPlayer(File file) {
        mediaView.setFitHeight(450);
        mediaView.setFitWidth(700);
        GridPane pane = new GridPane();
        pane.setAlignment(Pos.CENTER);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(volume, volumeSlider);
        pane.setPadding(new Insets(5));
        ColumnConstraints column1 = new ColumnConstraints();
        ColumnConstraints column2 = new ColumnConstraints();
        ColumnConstraints column3 = new ColumnConstraints();
        column1.setPrefWidth(196);
        column2.setPrefWidth(262);
        column3.setPrefWidth(196);
        column1.setHgrow(Priority.ALWAYS);
        column2.setHgrow(Priority.ALWAYS);
        column3.setHgrow(Priority.ALWAYS);
        column1.setHalignment(HPos.LEFT);
        column2.setHalignment(HPos.CENTER);
        column3.setHalignment(HPos.RIGHT);
        pane.getColumnConstraints().addAll(column1, column2, column3);
        volume.setGraphic(new FontIcon("fas-volume-up"));
        FontIcon icon1 = new FontIcon("far-play-circle");
        icon1.setIconSize(25);
        playOrPause.setGraphic(icon1);
        setStyle("-fx-background-color: #C8CCE8");
        pane.setStyle("-fx-background-color: #F1F1F1");
        FontIcon icon2 = new FontIcon("fas-undo");
        icon2.setIconSize(25);
        reset.setGraphic(icon2);
        volume.setStyle("-fx-background-color: transparent");
        playOrPause.setStyle("-fx-background-color: transparent");
        reset.setStyle("-fx-background-color: transparent");
        pane.add(hBox, 0, 0);
        pane.add(playOrPause, 1, 0);
        pane.add(reset, 2, 0);
        setAlignment(Pos.CENTER);

        getChildren().addAll(mediaView, mediaSlider, pane);
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaView.setMediaPlayer(mediaPlayer);
        volumeSlider.setValue(mediaPlayer.getVolume() * 100);
        volumeSlider.valueProperty().addListener(observable -> mediaPlayer.setVolume(volumeSlider.getValue() / 100));
        mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> mediaSlider.setValue(newValue.toSeconds())
        );

        mediaSlider.setOnMousePressed(event -> mediaPlayer.seek(Duration.seconds(mediaSlider.getValue())));

        mediaSlider.setOnMouseDragged(event -> mediaPlayer.seek(Duration.seconds(mediaSlider.getValue())));

        mediaPlayer.setOnReady(() -> {
            Duration total = media.getDuration();
            mediaSlider.setMax(total.toSeconds());
        });

        final boolean[] toggle = {false};
        playOrPause.setOnAction(e -> {
            if (toggle[0]) {
                mediaPlayer.play();
                toggle[0] = false;
            } else {
                mediaPlayer.pause();
                toggle[0] = true;
            }
        });
        reset.setOnAction(e -> {
            if (mediaPlayer.getStatus() != MediaPlayer.Status.READY) {
                mediaPlayer.seek(Duration.seconds(0.0));
            }
        });
    }

}
