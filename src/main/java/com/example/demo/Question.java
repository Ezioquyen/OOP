package com.example.demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Question {

    private String question;
    private Pair<String,Float> option1;

    private Pair<String,Float> option2;

    private Pair<String,Float> option3;

    private Pair<String,Float> option4;
    private Pair<String,Float> option5;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public Pair<String, Float> getOption1() {
        return option1;
    }

    public void setOption1(Pair<String, Float> option1) {
        this.option1 = option1;
    }

    public Pair<String, Float> getOption2() {
        return option2;
    }

    public void setOption2(Pair<String, Float> option2) {
        this.option2 = option2;
    }

    public Pair<String, Float> getOption3() {
        return option3;
    }

    public void setOption3(Pair<String, Float> option3) {
        this.option3 = option3;
    }

    public Pair<String, Float> getOption4() {
        return option4;
    }

    public void setOption4(Pair<String, Float> option4) {
        this.option4 = option4;
    }

    public Pair<String, Float> getOption5() {
        return option5;
    }

    public void setOption5(Pair<String, Float> option5) {
        this.option5 = option5;
    }

    public static ObservableList<Question> getAllQuestions() {
        ObservableList<Question> allquestions= FXCollections.observableArrayList();
        String DB_URL = "jdbc:mysql://localhost:3306/testhdh";
        String DB_USER = "root";
        String DB_PASSWORD = "";
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement statement = connection.createStatement();
            String query = "SELECT * FROM questionname";
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Question question=new Question();
                String questionname=resultSet.getString("questionname");
                int id = resultSet.getInt("questionid");
                String query1 = String.format("SELECT * FROM option WHERE questionid = %d",id);
                Statement statement1=connection.createStatement();
                ResultSet resultSet1=statement1.executeQuery(query1);
                List<Pair<String,Float>> list= new ArrayList<>();
                while (resultSet1.next()) {
                    list.add(new Pair(resultSet1.getString("choice"),
                                      resultSet1.getFloat("percent")));

                }
                if (list.size()==2) {
                    question.setQuestion(questionname);
                    question.setOption1(list.get(0));
                    question.setOption2(list.get(1));
                    question.setOption3(null);
                    question.setOption4(null);
                    question.setOption5(null);
                } else if (list.size()==3) {
                    question.setQuestion(questionname);
                    question.setOption1(list.get(0));
                    question.setOption2(list.get(1));
                    question.setOption3(list.get(2));
                    question.setOption4(null);
                    question.setOption5(null);
                } else if (list.size()==4) {
                    question.setQuestion(questionname);
                    question.setOption1(list.get(0));
                    question.setOption2(list.get(1));
                    question.setOption3(list.get(2));
                    question.setOption4(list.get(3));
                    question.setOption5(null);
                } else {
                    question.setQuestion(questionname);
                    question.setOption1(list.get(0));
                    question.setOption2(list.get(1));
                    question.setOption3(list.get(2));
                    question.setOption4(list.get(3));
                    question.setOption5(list.get(4));
                }
                allquestions.add(question);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return allquestions;
    }
}
