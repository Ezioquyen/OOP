package com.example.demo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;
import org.controlsfx.control.BreadCrumbBar;


public class BreadCrumbBarModel {

    private static BreadCrumbBarModel instance;

    private BreadCrumbBarModel() {
    }

    public static BreadCrumbBarModel getInstance() {
        if (instance == null) {
            instance = new BreadCrumbBarModel();
            instance.init();
        }
        return instance;
    }

    private final ObjectProperty<String> currentView = new SimpleObjectProperty<>();
    private final ObjectProperty<TreeItem<String>> currentTree = new SimpleObjectProperty<>();
    private final BiMap<String, TreeItem<String>> breadConnection = HashBiMap.create();

    public TreeItem<String> getCurrentTree() {
        return currentTree.get();
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

    public BiMap<String, TreeItem<String>> getBreadConnection() {
        return this.breadConnection;
    }

    private boolean tabCheck = false;
    private BreadCrumbBar<String> breadCrumbBar;


    public boolean isTabCheck() {
        return tabCheck;
    }

    public void setTabCheck(boolean tabCheck) {
        this.tabCheck = tabCheck;
    }

    private final TreeItem<String> thiCuoiKy = new TreeItem<>("THI CUỐI KỲ");

    public TreeItem<String> getThiCuoiKy() {
        return thiCuoiKy;
    }

    public void insertQuizView(String view) {
        TreeItem<String> mainView = new TreeItem<>(view);
        TreeItem<String> edit = new TreeItem<>("Edit quiz");
        getThiCuoiKy().getChildren().add(mainView);
        mainView.getChildren().add(edit);
        getBreadConnection().put("quiz.fxml", mainView);
        getBreadConnection().put("EditQuiz.fxml", edit);
    }

    public void removeQuizView() {
        getThiCuoiKy().getChildren().remove(getBreadConnection().get("quiz.fxml"));
        getBreadConnection().remove("quiz.fxml", getBreadConnection().get("quiz.fxml"));
    }

    private void init() {
        TreeItem<String> myCourse = new TreeItem<>("My Course");
        TreeItem<String> questionBank = new TreeItem<>("Question Bank");
        TreeItem<String> question = new TreeItem<>("Questions");
        TreeItem<String> addMTPCQ = new TreeItem<>("Editing a Multiple choice question");
        TreeItem<String> addQuiz = new TreeItem<>("Add new quiz");
        TreeItem<String> category = new TreeItem<>("Category");
        TreeItem<String> importVar = new TreeItem<>("Import");
        TreeItem<String> exportVar = new TreeItem<>("Export");
        myCourse.getChildren().add(thiCuoiKy);
        thiCuoiKy.getChildren().add(questionBank);
        thiCuoiKy.getChildren().add(addQuiz);
        questionBank.getChildren().addAll(question, category, importVar, exportVar);
        question.getChildren().add(addMTPCQ);
        this.currentView.set("thi-cuoi-ky.fxml");
        this.currentTree.set(thiCuoiKy);
        breadCrumbBar.selectedCrumbProperty().set(thiCuoiKy);
        this.breadConnection.put("thi-cuoi-ky.fxml", thiCuoiKy);
        this.breadConnection.put("add-MTPCQ.fxml", addMTPCQ);
        this.breadConnection.put("questionbank.fxml", question);
        this.breadConnection.put("add-quiz.fxml", addQuiz);
        this.breadConnection.put("1", category);
        this.breadConnection.put("2", importVar);
        this.breadConnection.put("3", exportVar);
    }
}
