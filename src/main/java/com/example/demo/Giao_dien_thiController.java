package com.example.demo;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;

public class Giao_dien_thiController implements Initializable {
    private static final int START_HOUR = 1;
    private static final int START_MINUTE = 0;
    private static final int START_SECOND = 0;

    private int hour = START_HOUR;
    private int minute = START_MINUTE;
    private int second = START_SECOND;

    @FXML
    private VBox giao_dien_thi_layout;
    @FXML
    private FlowPane navigation;
    @FXML
    private Circle avatar;
    @FXML
    private Label timerLabel;
    private ObservableList<Question> allQuestions = Question.getAllQuestions();
    private Map<Cau_hoi_thiController,Progress_cau_hoi_controller> map=new HashMap<>();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        timerLabel.setText(formatTime(hour, minute, second));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            this.updateTimer();
            timerLabel.setText(formatTime(hour, minute, second));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();

        this.createCauhoithi();

        Image img = new Image(Objects.requireNonNull(getClass().getResource("/Image/hello.jfif")).toExternalForm());
        avatar.setFill(new ImagePattern(img));


    }
    @FXML
    public void Changecolor(ActionEvent event) {
        float marktotal=0;
        for(Map.Entry<Cau_hoi_thiController,Progress_cau_hoi_controller> mapEntry : map.entrySet()) {
            Cau_hoi_thiController cauhoithihientai=mapEntry.getKey();
            ObservableList<RadioButton> options=mapEntry.getKey().getListoptions();
            int mark=0;
            float maxpercent=-1000;
            int count=0;
            int truecount=0;
            mapEntry.getValue().setColor();
                for(RadioButton radioButton : options) {
                    if(maxpercent<cauhoithihientai.getPercentOption(radioButton)) maxpercent=cauhoithihientai.getPercentOption(radioButton);
                    if(radioButton.isSelected()) {
                        count++;
                        if(cauhoithihientai.getPercentOption(radioButton)!=0) {
                            mark+=cauhoithihientai.getPercentOption(radioButton)/100;
                            truecount++;
                        }
                    }
                }
                if(count!=truecount||100.0/truecount!=maxpercent) {
                    mark=0;
                }
                marktotal+=mark;


            }
        System.out.println(marktotal+"/"+map.size());
        }
    public void createCauhoithi() {
        for (int i = 0; i < allQuestions.size(); i++) {
            FXMLLoader fxmlLoader1 = new FXMLLoader(getClass().getResource("cau_hoi_thi.fxml"));
            FXMLLoader fxmlLoader2 = new FXMLLoader(getClass().getResource("progress_cauhoi.fxml"));
            try {
                Parent root1 = fxmlLoader1.load();
                Cau_hoi_thiController cauhoithi = fxmlLoader1.getController();
                if(allQuestions.get(i).getOption3()==null&& allQuestions.get(i).getOption4()==null&&allQuestions.get(i).getOption5()==null) {
                    cauhoithi.setText(allQuestions.get(i).getQuestion(),allQuestions.get(i).getOption1(),allQuestions.get(i).getOption2(),i+1);
                } else if(allQuestions.get(i).getOption3()!=null&& allQuestions.get(i).getOption4()==null&& allQuestions.get(i).getOption5()==null){
                    cauhoithi.setText(allQuestions.get(i).getQuestion(),allQuestions.get(i).getOption1(),allQuestions.get(i).getOption2(),allQuestions.get(i).getOption3(),i+1);
                } else if (allQuestions.get(i).getOption3()!=null&& allQuestions.get(i).getOption4()!=null&& allQuestions.get(i).getOption5()==null) cauhoithi.setText(allQuestions.get(i).getQuestion(),allQuestions.get(i).getOption1(),allQuestions.get(i).getOption2(),allQuestions.get(i).getOption3(),allQuestions.get(i).getOption4(),i+1);
                else cauhoithi.setText(allQuestions.get(i).getQuestion(),allQuestions.get(i).getOption1(),allQuestions.get(i).getOption2(),allQuestions.get(i).getOption3(),allQuestions.get(i).getOption4(),allQuestions.get(i).getOption5(),i+1);
                giao_dien_thi_layout.getChildren().add(root1);

                Parent root2 = fxmlLoader2.load();
                Progress_cau_hoi_controller progress = fxmlLoader2.getController();
                progress.setNumber(i + 1);
                navigation.getChildren().add(root2);
                map.put(cauhoithi,progress);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private void updateTimer() {
        if (hour == 0 && minute == 0 && second == 0) {
            // Đồng hồ đã hết thời gian, thực hiện các hành động sau khi hết thời gian
            // Ví dụ: Hiển thị thông báo, kết thúc ứng dụng, vv.
            return;
        }

        second--;
        if (second < 0) {
            second = 59;
            minute--;
            if (minute < 0) {
                minute = 59;
                hour--;
            }
        }
    }

    private String formatTime(int hour, int minute, int second) {
        return String.format("Time left %02d:%02d:%02d", hour, minute, second);
    }
}


