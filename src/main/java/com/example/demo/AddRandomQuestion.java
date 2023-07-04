package com.example.demo;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddRandomQuestion {

    @FXML
    private TreeView<String> root;
    @FXML
    private ComboBox<Integer> numberOfQuestion;
    @FXML
    private Button btnAddQues;
    @FXML
    private CheckBox getQuesFromSubCategory;
    @FXML
    private Pagination pagination;
    @FXML
    private Label label;
    @FXML
    private Button closeButton;

    private VBox page;
    private final SimpleIntegerProperty totalQuestion = new SimpleIntegerProperty(0);
    private DataModel dataModel;
    private final List<QuestionsView> list = new ArrayList<>();
    private final List<QuestionsView> quesFromSubcategories = new ArrayList<>();
    private List<QuestionsView> randomSelection = new ArrayList<>();

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    @FXML
    private void initialize() {
        initDataModel(DataModel.getInstance());
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            dataModel.setCurrentCategory(root.getSelectionModel().getSelectedItem());
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
            list.clear();
            quesFromSubcategories.clear();
            page.getChildren().clear();
            showQuestion();
            totalQuestion.set(list.size());
            pagination.setMaxPageIndicatorCount(Math.min((list.size() / 10) + 1, 8));
            pagination.setPageCount(list.size() / 10 + 1);
            pagination.setPageFactory(this::createPage);
        });

        getQuesFromSubCategory.selectedProperty().addListener(e -> {
            if (getQuesFromSubCategory.isSelected()) {
                traverseTreeView(root.getSelectionModel().getSelectedItem());
                list.addAll(quesFromSubcategories);
                page.getChildren().addAll(quesFromSubcategories);
            } else {
                page.getChildren().removeAll(quesFromSubcategories);
                list.removeAll(quesFromSubcategories);
                quesFromSubcategories.clear();
            }
            totalQuestion.set(list.size());
            pagination.setMaxPageIndicatorCount(Math.min((list.size() / 10) + 1, 8));
            pagination.setPageCount(list.size() / 10 + 1);
            pagination.setPageFactory(this::createPage);
        });
        pagination.setMaxPageIndicatorCount(Math.min((list.size() / 10) + 1, 8));
        pagination.setPageFactory(this::createPage);
        totalQuestion.addListener(e -> {
            numberOfQuestion.getItems().clear();
            for (int i = 1; i <= totalQuestion.getValue(); i++) {
                numberOfQuestion.getItems().add(i);
            }
        });

        numberOfQuestion.getSelectionModel().selectedItemProperty().addListener(e -> {
            randomSelection = selectRandomElements(list, numberOfQuestion.getSelectionModel().getSelectedItem());
        });

    }


    public Button getCloseButton() {
        return closeButton;
    }

    private void showQuestion() {

        if (root.getSelectionModel().getSelectedItem() != null) {
            for (Question question : dataModel.getQuestionExcept(dataModel.getCategoryMap().get(root.getSelectionModel().getSelectedItem()), dataModel.getCurrentQuiz().getQuizID())) {
                QuestionsView questionsView = new QuestionsView(question);
                list.add(questionsView);
            }
        }
        if (getQuesFromSubCategory.isSelected()) {
            traverseTreeView(root.getSelectionModel().getSelectedItem());
            list.addAll(quesFromSubcategories);
        }
        for (int i = pagination.getCurrentPageIndex() * 10; i < list.size() && i < 10; i++) {
            page.getChildren().add(list.get(i));
        }
    }

    private void traverseTreeView(TreeItem<String> root) {
        if (root != null) {
            for (TreeItem<String> child : root.getChildren()) {
                traverseTreeView(child);
                for (Question question : dataModel.getQuestionExcept(dataModel.getCategoryMap().get(child), dataModel.getCurrentQuiz().getQuizID())) {
                    QuestionsView questionsView = new QuestionsView(question);
                    quesFromSubcategories.add(questionsView);
                }
            }
        }
    }

    private VBox createPage(int pageIndex) {
        page = new VBox();
        page.setMinSize(800, Region.USE_COMPUTED_SIZE);
        page.setPadding(new Insets(10));
        for (int i = pagination.getCurrentPageIndex() * 10; i < list.size() && i < (pagination.getCurrentPageIndex() * 10 + 10); i++) {
            page.getChildren().add(list.get(i));
        }
        return page;
    }

    private <T> List<T> selectRandomElements(List<T> list, int k) {
        List<T> randomSelection = new ArrayList<>(list);

        Random random = new Random();
        for (int i = randomSelection.size() - 1; i >= k; i--) {
            int j = random.nextInt(i + 1);
            randomSelection.remove(j);
        }

        return randomSelection;
    }

    public Button getBtnAddQues() {
        return btnAddQues;
    }

    public List<QuestionsView> getRandomSelection() {
        return randomSelection;
    }

    public List<QuestionsView> getList() {
        return list;
    }

    public void setPagination() {
        pagination.setPageFactory(this::createPage);
    }

    public int getTotalQuestion() {
        return totalQuestion.get();
    }

    public void totalQuestionProperty(int value) {
        totalQuestion.set(value);
    }
}

