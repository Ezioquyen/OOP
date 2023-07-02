package com.example.demo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.scene.control.TreeItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataModel {
    private static DataModel instance;

    private DataModel() {

    }
    public static DataModel getInstance() {
        if (instance == null) {
            instance = new DataModel();
            instance.Initialize();
        }
        return instance;
    }

    private final ConnectJDBC connectJDBC = new ConnectJDBC();
    private final Connection conn = connectJDBC.getConnection();
    private final TreeItem<String> root = new TreeItem<>("Course: IT");
    private final BiMap<TreeItem<String>, Integer> categoryMap = HashBiMap.create();
    private Integer lastNode_id = 0;
    private Integer count = 0;
    private final Map<Integer, Integer> numberOfQuestion = new HashMap<>();
    private final Map<TreeItem<String>, String> mapName = new HashMap<>();
    private String currentQuizName;
    private TreeItem<String> CurrentCategory;
    private Boolean isShowQues = false;
    private Question currentQuestion;

    private int totalQuestion;


    private void Initialize() {

        String query = "SELECT COUNT(CategoryID) AS quantity, CategoryID FROM QUESTIONS GROUP BY CategoryID";
        Statement stm;
        try {
            stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                Integer quantity = rs.getInt("quantity");
                Integer categoryID = rs.getInt("CategoryID");
                numberOfQuestion.put(categoryID, quantity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        query = "SELECT * FROM CATEGORIES";
        try {
            stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);

            categoryMap.put(root, 0);
            while (rs.next()) {
                int nodeId = rs.getInt("node_id");
                Integer parentId = rs.getInt("parent_id");
                String name = rs.getString("name");
                TreeItem<String> categoryName = new TreeItem<>();
                mapName.put(categoryName, name);
                if (numberOfQuestion.get(nodeId) != null) name = name + " (" + numberOfQuestion.get(nodeId) + ")";
                categoryName.setValue(name);
                categoryMap.put(categoryName, nodeId);
                categoryMap.inverse().get(parentId).getChildren().add(categoryName);
                lastNode_id = nodeId;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //
        query = "SELECT COUNT(QuestionID) FROM QUESTIONS";
        try {
            stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            while (rs.next()) {
                totalQuestion = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TreeItem<String> getRoot() {
        return root;
    }

    public void insertCategory(TreeItem<String> parent, String text) {
        try {
            String sql = "INSERT INTO CATEGORIES (parent_id, name) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, categoryMap.get(parent));
            statement.setString(2, text);
            TreeItem<String> child = new TreeItem<>(text);
            mapName.put(child, text);
            lastNode_id++;
            parent.getChildren().add(child);
            categoryMap.put(child, lastNode_id);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void insertQuestion(TreeItem<String> parent, String title, int type, Double mark) {
        try {
            String sql = "INSERT INTO QUESTIONS (Content,typeOfQuestion,CategoryID,mark) VALUES ( ?, ?, ?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, title);
            statement.setInt(2, type);
            statement.setInt(3, categoryMap.get(parent));
            statement.setDouble(4, mark);
            statement.executeUpdate();
            statement.close();
            totalQuestion += 1;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAnswers(List<String> options, List<Double> percent, int id) {
        try {
            String sql = "INSERT INTO ANSWER (questionID, choice, percent) VALUES ( ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            int i = 0;
            for (String option : options) {
                if (id == 0) {
                    statement.setInt(1, totalQuestion);
                } else statement.setInt(1, id);

                statement.setString(2, option);

                statement.setDouble(3, percent.get(i));

                statement.executeUpdate();
                i++;
            }
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    //Update so luong cau hoi
    public void updateCategory(TreeItem<String> parent) {
        if (numberOfQuestion.get(categoryMap.get(parent)) != null) {
            this.numberOfQuestion.put(categoryMap.get(parent), numberOfQuestion.get(categoryMap.get(parent)) + getCount());
        } else {
            this.numberOfQuestion.put(categoryMap.get(parent), getCount());
        }
        parent.setValue(mapName.get(parent) + " (" + this.numberOfQuestion.get(categoryMap.get(parent)) + ")");
    }

    public int getTotalQuestion() {
        return totalQuestion;
    }

    public void setTotalQuestion(int totalQuestion) {
        this.totalQuestion = totalQuestion;
    }

    public Integer getLastNode_id() {
        return lastNode_id;
    }

    public BiMap<TreeItem<String>, Integer> getCategoryMap() {
        return categoryMap;
    }

    public ArrayList<Question> getQuestion(Integer id) {
        ArrayList<Question> questions = new ArrayList<>();
        try {
            String sql = "SELECT QuestionID, Content, mark FROM QUESTIONS WHERE CategoryID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setCategoryID(id);
                question.setId(rs.getInt("QuestionID"));
                question.addTitle(rs.getString("Content"));
                question.setMark(rs.getDouble("mark"));
                questions.add(question);
            }
            rs.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT answerID, choice, percent FROM ANSWER WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getAnsID().add(rs.getInt("answerID"));
                    question.getOptions().add(rs.getString("choice"));
                    question.getPercent().add(rs.getDouble("percent"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public ArrayList<String> getQuizTitle() {
        ArrayList<String> quizTitle = new ArrayList<>();
        try {
            String sql = "SELECT name FROM QUIZ";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                quizTitle.add(rs.getString("name"));
            }
            rs.close();

            statement.close();

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return quizTitle;
    }

    public void insertQuiz(String name, float time) {
        try {
            String sql = "INSERT INTO QUIZ (name, time) value (?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            if (time != 0) statement.setFloat(2, time);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAns(Question question, int currentAns) {
        int j = 0;
        for (int ignore : question.getAnsID()) {
            if (j >= currentAns) question.getAnsID().remove(j);
        }
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("DELETE FROM ANSWER WHERE answerID NOT IN (");
            for (int i = 0; i < currentAns; i++) {
                sqlBuilder.append("?");
                if (i < currentAns - 1) {
                    sqlBuilder.append(",");
                }
            }
            sqlBuilder.append(")");
            sqlBuilder.append("AND questionID = ?");
            PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < currentAns; i++) {
                statement.setInt(i + 1, question.getAnsID().get(i));
            }
            statement.setInt(currentAns + 1, question.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentQuizName(String currentQuizName) {
        this.currentQuizName = currentQuizName;
    }

    public String getCurrentQuizName() {
        return currentQuizName;
    }

    public TreeItem<String> getCurrentCategory() {
        return CurrentCategory;
    }

    public void setCurrentCategory(TreeItem<String> currentCategory) {
        CurrentCategory = currentCategory;
    }

    public Boolean getShowQues() {
        return isShowQues;
    }

    public void setShowQues(Boolean showQues) {
        isShowQues = showQues;
    }

    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void updateQuestion(Question question) {
        try {
            String sql = "UPDATE QUESTIONS SET Content = ?, typeOfQuestion = ?, mark = ? WHERE QuestionID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, question.getTitle());
            statement.setInt(2, question.isType());
            statement.setDouble(3, question.getMark());
            statement.setInt(4, question.getId());
            statement.executeUpdate();
            int index = 0;
            for (Integer id : question.getAnsID()) {
                sql = "UPDATE ANSWER SET choice = ?,percent =? WHERE answerID = ? ";
                statement = conn.prepareStatement(sql);
                statement.setString(1, question.getOptions().get(index));
                statement.setDouble(2, question.getPercent().get(index));
                statement.setInt(3, id);
                statement.executeUpdate();
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setCurrentQuestion(Question currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

}
