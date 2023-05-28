package com.example.demo;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;

import java.util.HashMap;
import java.util.Map;


public class BreadCrumbBarModel {

    private static BreadCrumbBarModel instance;

    private BreadCrumbBarModel() {
    }

    public static BreadCrumbBarModel getInstance() {
        if (instance == null) {
            instance = new BreadCrumbBarModel();
            instance.process();
        }
        return instance;
    }

    private final ObservableList<String> viewList = FXCollections.observableArrayList();
    private final ObjectProperty<String> currentView = new SimpleObjectProperty<>();
    private final ObservableList<TreeItem<String>> treeList = FXCollections.observableArrayList();
    private final ObjectProperty<TreeItem<String>> currentTree = new SimpleObjectProperty<>();

    private Map<String, TreeItem<String>> breadConnection = new HashMap<>();

    public TreeItem<String> getCurrentTree() {
        return currentTree.get();
    }

    public ObjectProperty<TreeItem<String>> currentTreeProperty() {
        return currentTree;
    }

    public void setCurrentTree(TreeItem<String> currentTree) {
        this.currentTree.set(currentTree);
    }

    public ObjectProperty<String> currentViewProperty() {
        return currentView;
    }

    public final String getCurrentView() {
        return currentView.get();
    }

    public final void setCurrentView(String view) {
        this.currentView.set(view);
    }

    public void setViewList(ObservableList<String> viewList) {
        this.viewList.addAll(viewList);
    }

    public void setTreeList(ObservableList<TreeItem<String>> treeList) {
        this.treeList.addAll(treeList);
    }

    public void setBreadConnection(Map<String, TreeItem<String>> breadConnection) {
        this.breadConnection = breadConnection;
    }

    public Map<String, TreeItem<String>> getBreadConnection() {
        return this.breadConnection;
    }

    public void process() {
        TreeItem<String> home = new TreeItem<>("Home");
        TreeItem<String> myCourse = new TreeItem<>("My Course");
        TreeItem<String> thiCuoiKy = new TreeItem<>("THI CUỐI KỲ");
        TreeItem<String> questionBank = new TreeItem<>("Question Bank");
        TreeItem<String> question = new TreeItem<>("Questions ");
        TreeItem<String> addMTPCQ = new TreeItem<>("Editing a Multiple choice question");
        home.getChildren().addAll(myCourse);
        myCourse.getChildren().addAll(thiCuoiKy);
        thiCuoiKy.getChildren().add(questionBank);
        questionBank.getChildren().add(question);
        question.getChildren().add(addMTPCQ);
        this.currentView.set("thi-cuoi-ky.fxml");
        this.currentTree.set(thiCuoiKy);
        this.treeList.addAll(home, myCourse, thiCuoiKy, questionBank, question, addMTPCQ);
        this.viewList.addAll("thi-cuoi-ky.fxml", "questionbank.fxml", "add-quiz.fxml", "add-MTPCQ.fxml");
        this.breadConnection.put("add-MTPCQ.fxml", addMTPCQ);
        this.breadConnection.put("questionbank.fxml", questionBank);
    }
}
