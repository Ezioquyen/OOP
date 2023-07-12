package com.example.demo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.scene.control.TreeItem;

import java.sql.*;
import java.util.*;

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
    private Quiz currentQuiz;
    private TreeItem<String> CurrentCategory;
    private Question currentQuestion;

    private int totalQuestion = 0;
    private List<Quiz> quizs = new ArrayList<>();

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

    private boolean duplicateCategoryCheck(TreeItem<String> parent, String text) {
        if (numberOfQuestion.get(categoryMap.get(parent)) != null) {
            if (Objects.equals(parent.getValue(), text + " (" + numberOfQuestion.get(categoryMap.get(parent)) + ")"))
                return false;
        } else if (Objects.equals(parent.getValue(), text)) return false;
        for (TreeItem<String> item : parent.getChildren()) {
            if (numberOfQuestion.get(categoryMap.get(item)) != null) {
                if (Objects.equals(item.getValue(), text + " (" + numberOfQuestion.get(categoryMap.get(item)) + ")"))
                    return false;
            } else if (Objects.equals(item.getValue(), text)) return false;
        }
        return true;
    }

    public boolean insertCategory(TreeItem<String> parent, String text) {
        if (duplicateCategoryCheck(parent, text)) {
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
        } else return false;
        return true;
    }

    public void insertQuestion(TreeItem<String> parent, String title, int type, Double mark, String video) {
        try {
            String sql = "INSERT INTO QUESTIONS (Content,typeOfQuestion,CategoryID,mark,videoPath) VALUES ( ?,?,?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, title);
            statement.setInt(2, type);
            statement.setInt(3, categoryMap.get(parent));
            statement.setDouble(4, mark);
            statement.setString(5, video);
            statement.executeUpdate();
            statement.close();
            totalQuestion += 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void updateImagePath(Question question) {
        try {
            int index = 0;
            for (Integer id : question.getImageID()) {
                String sql = "UPDATE IMAGE SET imagePath = ? WHERE imageID = ? ";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, question.getImageFilePath().get(index));
                statement.setInt(2, id);
                statement.executeUpdate();
                index++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void deleteImagePath(Question question) {
        try {
            StringBuilder sqlBuilder = new StringBuilder();

            if (question.getImageFilePath().size() > 0) {
                sqlBuilder.append("DELETE FROM IMAGE WHERE imageID NOT IN (");
                for (int i = 0; i < question.getImageFilePath().size(); i++) {
                    sqlBuilder.append("?");
                    if (i < question.getImageFilePath().size() - 1) {
                        sqlBuilder.append(",");
                    }
                }
                sqlBuilder.append(")");
                sqlBuilder.append("AND QuestionID = ?");
            } else sqlBuilder.append("DELETE FROM IMAGE WHERE QuestionID = ?");
            System.out.println(sqlBuilder);
            PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < question.getImageFilePath().size(); i++) {
                statement.setInt(i + 1, question.getImageID().get(i));
            }
            statement.setInt(question.getImageFilePath().size() + 1, question.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertImage(String path, int id) {
        try {
            String sql = "INSERT INTO IMAGE (imagePath, QuestionID) VALUES ( ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, path);
            if (id == 0) {
                statement.setInt(2, totalQuestion);
            } else statement.setInt(2, id);

            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertAnswers(List<String> options, List<Double> percent, int id, List<String> path) {
        try {
            String sql = "INSERT INTO ANSWER (questionID, choice, percent,imagePath) VALUES ( ?, ?, ?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);

            int i = 0;
            for (String option : options) {
                if (id == 0) {
                    statement.setInt(1, totalQuestion);
                } else statement.setInt(1, id);

                statement.setString(2, option);

                statement.setDouble(3, percent.get(i));
                if (path != null && path.size() > i) statement.setString(4, path.get(i));
                else statement.setString(4, null);

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

    public Integer getLastNode_id() {
        return lastNode_id;
    }

    public BiMap<TreeItem<String>, Integer> getCategoryMap() {
        return categoryMap;
    }

    public List<Question> getQuestion(Integer id) {
        List<Question> questions = new ArrayList<>();
        try {
            String sql = "SELECT QuestionID, Content, mark,videoPath FROM QUESTIONS WHERE CategoryID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setCategoryID(id);
                question.setId(rs.getInt("QuestionID"));
                question.addTitle(rs.getString("Content"));
                question.setMark(rs.getDouble("mark"));
                question.setVideoPath(rs.getString("videoPath"));
                questions.add(question);
            }
            rs.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT answerID, choice, percent,imagePath FROM ANSWER WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getAnsID().add(rs.getInt("answerID"));
                    question.getOptions().add(rs.getString("choice"));
                    question.getPercent().add(rs.getDouble("percent"));
                    question.getImageOptionPath().add(rs.getString("imagePath"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {

                String sql = "SELECT imagePath,imageID FROM IMAGE WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getImageID().add(rs.getInt("imageID"));
                    if (rs.getString("imagePath") != null) question.getImageFilePath().add(rs.getString("imagePath"));

                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public List<Quiz> getQuiz() {
        quizs = new ArrayList<>();
        try {
            String sql = "SELECT name,time,quizID,totalMark,shuffle, totalQuestion, grade FROM QUIZ";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Quiz quiz = new Quiz();
                quiz.setName(rs.getString("name"));
                quiz.setQuizID(rs.getInt("quizID"));
                quiz.setTime(rs.getDouble("time"));
                quiz.setShuffle(rs.getBoolean("shuffle"));
                quiz.setTotalMarks(rs.getDouble("totalMark"));
                quiz.setMaxGrade(rs.getDouble("grade"));
                quiz.setTotalQuestion(rs.getInt("totalQuestion"));
                quizs.add(quiz);
            }
            rs.close();

            statement.close();

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return quizs;
    }

    public List<Quiz> getQuizs() {
        return quizs;
    }

    public void insertQuiz(String name, float time) {
        try {
            String sql = "INSERT INTO QUIZ (name, time,totalMark,shuffle,grade) value (?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, name);
            statement.setFloat(2, time);
            statement.setDouble(3, 0.00);
            statement.setBoolean(4, false);
            statement.setDouble(5, 10.00);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAns(Question question) {
        int j = 0;
        for (int ignore : question.getAnsID()) {
            if (j >= question.getOptions().size()) question.getAnsID().remove(j);
        }
        try {
            StringBuilder sqlBuilder = new StringBuilder();
            sqlBuilder.append("DELETE FROM ANSWER WHERE answerID NOT IN (");
            for (int i = 0; i < question.getOptions().size(); i++) {
                sqlBuilder.append("?");
                if (i < question.getOptions().size() - 1) {
                    sqlBuilder.append(",");
                }
            }
            sqlBuilder.append(")");
            sqlBuilder.append("AND questionID = ?");
            PreparedStatement statement = conn.prepareStatement(sqlBuilder.toString());
            for (int i = 0; i < question.getOptions().size(); i++) {
                statement.setInt(i + 1, question.getAnsID().get(i));
            }
            statement.setInt(question.getOptions().size() + 1, question.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Quiz getCurrentQuiz() {
        return currentQuiz;
    }

    public TreeItem<String> getCurrentCategory() {
        return CurrentCategory;
    }

    public void setCurrentCategory(TreeItem<String> currentCategory) {
        CurrentCategory = currentCategory;
    }


    public Question getCurrentQuestion() {
        return currentQuestion;
    }

    public void updateQuestion(Question question) {
        try {
            String sql = "UPDATE QUESTIONS SET Content = ?, typeOfQuestion = ?, mark = ?,videoPath=? WHERE QuestionID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, question.getTitle());
            statement.setInt(2, question.isType());
            statement.setDouble(3, question.getMark());
            statement.setString(4, question.getVideoPath());
            statement.setInt(5, question.getId());

            statement.executeUpdate();
            int index = 0;
            for (Integer id : question.getAnsID()) {
                sql = "UPDATE ANSWER SET choice = ?,percent =?, imagePath = ? WHERE answerID = ? ";
                statement = conn.prepareStatement(sql);
                statement.setString(1, question.getOptions().get(index));
                statement.setDouble(2, question.getPercent().get(index));
                statement.setString(3, question.getImageOptionPath().get(index));
                statement.setInt(4, id);
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

    public void insertQuestionToQuiz(int questionID, int quizID) {
        try {
            String sql = "INSERT INTO QUIZ_QUESTION (quizID, QuestionID) value (?,?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quizID);
            statement.setInt(2, questionID);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void removeQuestionToQuiz(int questionID, int quizID) {
        try {
            String sql = "DELETE FROM QUIZ_QUESTION WHERE quizID = ? AND QuestionID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quizID);
            statement.setInt(2, questionID);
            statement.executeUpdate();
            statement.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Question> getQuestionToQuiz(int quizID) {
        List<Question> questions = new ArrayList<>();
        try {
            String sql = "SELECT q.QuestionID,q.CategoryID, q.Content, q.mark, q.videoPath FROM QUESTIONS q JOIN QUIZ_QUESTION QQ on q.QuestionID = QQ.QuestionID WHERE QQ.quizID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, quizID);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Question question = new Question();
                question.setId(rs.getInt("QuestionID"));
                question.setCategoryID(rs.getInt("CategoryID"));
                question.addTitle(rs.getString("Content"));
                question.setMark(rs.getDouble("mark"));
                question.setVideoPath(rs.getString("videoPath"));
                questions.add(question);
            }
            rs.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT answerID, choice, percent,imagePath FROM ANSWER WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getAnsID().add(rs.getInt("answerID"));
                    question.getOptions().add(rs.getString("choice"));
                    question.getPercent().add(rs.getDouble("percent"));
                    question.getImageOptionPath().add(rs.getString("imagePath"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT imagePath FROM IMAGE WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getImageFilePath().add(rs.getString("imagePath"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void setCurrentQuiz(Quiz quiz) {
        this.currentQuiz = quiz;
    }

    public List<Question> getQuestionExcept(Integer id, int quizID) {
        List<Question> questions = new ArrayList<>();
        try {
            String sql = "SELECT QuestionID, Content, mark,videoPath FROM QUESTIONS WHERE CategoryID = ? AND QuestionID NOT IN(SELECT QuestionID FROM QUIZ_QUESTION WHERE quizID = ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.setInt(2, quizID);
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Question question = new Question();
                question.setCategoryID(id);
                question.setId(rs.getInt("QuestionID"));
                question.addTitle(rs.getString("Content"));
                question.setMark(rs.getDouble("mark"));
                question.setVideoPath(rs.getString("videoPath"));
                questions.add(question);
            }
            rs.close();
            statement.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT answerID, choice, percent, imagePath FROM ANSWER WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getAnsID().add(rs.getInt("answerID"));
                    question.getOptions().add(rs.getString("choice"));
                    question.getPercent().add(rs.getDouble("percent"));
                    question.getImageOptionPath().add(rs.getString("imagePath"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            for (Question question : questions) {
                String sql = "SELECT imagePath FROM IMAGE WHERE questionID = ?";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setInt(1, question.getId());
                ResultSet rs = statement.executeQuery();
                while (rs.next()) {
                    question.getImageFilePath().add(rs.getString("imagePath"));
                }
                rs.close();
                statement.close();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
        return questions;
    }

    public void updateQuiz(Quiz quiz) {
        try {
            String sql = "UPDATE QUIZ SET  shuffle = ?, totalMark = ?, grade =? ,totalQuestion = ? WHERE quizID = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBoolean(1, quiz.getShuffle());
            statement.setDouble(2, quiz.getTotalMarks());
            statement.setDouble(3, quiz.getMaxGrade());
            statement.setInt(4, quiz.getTotalQuestion());
            statement.setDouble(5, quiz.getQuizID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
