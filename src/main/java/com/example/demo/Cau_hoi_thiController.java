package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class Cau_hoi_thiController implements Initializable {

    @FXML
    private RadioButton option1;

    @FXML
    private RadioButton option2;

    @FXML
    private RadioButton option3;

    @FXML
    private RadioButton option4;

    @FXML
    private RadioButton option5;

    @FXML
    private Text question;

    @FXML
    private Label question_number;
    private float percent1;
    private float percent2;
    private float percent3;
    private float percent4;
    private float percent5;
    private ToggleGroup toggleGroup=new ToggleGroup();
    ObservableList<RadioButton> listoptions =FXCollections.observableArrayList();

    private ToggleGroup group=new ToggleGroup();

    public void setText(String a, Pair<String,Float> b,Pair<String,Float> c,Pair<String,Float> d,Pair<String,Float> e,Pair<String,Float> g,int f) {
        if(b.getValue()==100||c.getValue()==100||d.getValue()==100||e.getValue()==100||g.getValue()==100) this.setToggle();
        question.setText(a);
        List<Pair<String,Float>> options=new ArrayList<>();
        options.add(b);
        options.add(c);
        options.add(d);
        options.add(e);
        options.add(g);
        Collections.shuffle(options);
        option1.setText(option1.getText()+options.get(0).getKey());
        this.setPercentOption(option1,options.get(0).getValue());
        option2.setText(option2.getText()+options.get(1).getKey());
        this.setPercentOption(option2,options.get(1).getValue());
        option3.setText(option3.getText()+options.get(2).getKey());
        this.setPercentOption(option3,options.get(2).getValue());
        option4.setText(option4.getText()+options.get(3).getKey());
        this.setPercentOption(option4,options.get(3).getValue());
        option5.setText(option5.getText()+options.get(4).getKey());
        this.setPercentOption(option5,options.get(4).getValue());
        question_number.setText("Question "+f);
    }
    public void setText(String a, Pair<String,Float> b,Pair<String,Float> c,Pair<String,Float> d,Pair<String,Float> e,int f) {
        if(b.getValue()==100||c.getValue()==100||d.getValue()==100||e.getValue()==100) this.setToggle();
        question.setText(a);
        List<Pair<String,Float>> options=new ArrayList<>();
        options.add(b);
        options.add(c);
        options.add(d);
        options.add(e);
        Collections.shuffle(options);
        option1.setText(option1.getText()+options.get(0).getKey());
        this.setPercentOption(option1,options.get(0).getValue());
        option2.setText(option2.getText()+options.get(1).getKey());
        this.setPercentOption(option2,options.get(1).getValue());
        option3.setText(option3.getText()+options.get(2).getKey());
        this.setPercentOption(option3,options.get(2).getValue());
        option4.setText(option4.getText()+options.get(3).getKey());
        this.setPercentOption(option4,options.get(3).getValue());
        option5.setVisible(false);
        question_number.setText("Question "+f);
    }
    public void setText(String a, Pair<String,Float> b,Pair<String,Float> c,Pair<String,Float> d,int f) {
        if(b.getValue()==100||c.getValue()==100||d.getValue()==100) this.setToggle();
        question.setText(a);
        List<Pair<String,Float>> options=new ArrayList<>();
        options.add(b);
        options.add(c);
        options.add(d);
        Collections.shuffle(options);
        option1.setText(option1.getText()+options.get(0).getKey());
        this.setPercentOption(option1,options.get(0).getValue());
        option2.setText(option2.getText()+options.get(1).getKey());
        this.setPercentOption(option2,options.get(1).getValue());
        option3.setText(option3.getText()+options.get(2).getKey());
        this.setPercentOption(option3,options.get(2).getValue());
        option4.setVisible(false);
        option5.setVisible(false);
        question_number.setText("Question "+f);
    }

    public void setText(String a,Pair<String,Float> b,Pair<String,Float> c,int f) {
        if(b.getValue()==100||c.getValue()==100) this.setToggle();
        question.setText(a);
        List<Pair<String,Float>> options=new ArrayList<>();
        options.add(b);
        options.add(c);
        Collections.shuffle(options);
        option1.setText(option1.getText()+options.get(0).getKey());
        this.setPercentOption(option1,options.get(0).getValue());
        option2.setText(option2.getText()+options.get(1).getKey());
        this.setPercentOption(option2,options.get(1).getValue());
        option3.setVisible(false);
        option4.setVisible(false);
        option5.setVisible(false);
        question_number.setText("Question "+f);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        listoptions.add(option1);
        listoptions.add(option2);
        listoptions.add(option3);
        listoptions.add(option4);
        listoptions.add(option5);
    }

    public ObservableList<RadioButton> getListoptions() {
        return listoptions;
    }
    public void setPercentOption(RadioButton radio,float pecent) {
        if(radio==option1) this.percent1=pecent;
        else if(radio==option2) this.percent2=pecent;
        else if(radio==option3) this.percent3=pecent;
        else if(radio==option4) this.percent4=pecent;
        else this.percent5=pecent;
    }
    public float getPercentOption(RadioButton radio) {
        if(radio==option1) return percent1;
        else if(radio==option2) return percent2;
        else if(radio==option3) return percent3;
        else if(radio==option4) return percent4;
        else return percent5;
    }
    public void setToggle() {
        option1.setToggleGroup(toggleGroup);
        option2.setToggleGroup(toggleGroup);
        option3.setToggleGroup(toggleGroup);
        option4.setToggleGroup(toggleGroup);
        option5.setToggleGroup(toggleGroup);
    }
}
