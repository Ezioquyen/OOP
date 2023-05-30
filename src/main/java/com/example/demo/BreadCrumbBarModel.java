package com.example.demo;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TreeItem;

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

    private final ObjectProperty<String> currentView = new SimpleObjectProperty<>();
    private final ObjectProperty<TreeItem<String>> currentTree = new SimpleObjectProperty<>();
    private boolean tabcheck = false;
    private BiMap<String, TreeItem<String>> breadConnection = HashBiMap.create();

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


    public void setBreadConnection(BiMap<String, TreeItem<String>> breadConnection) {
        this.breadConnection = breadConnection;
    }

    public BiMap<String, TreeItem<String>> getBreadConnection() {
        return this.breadConnection;
    }

    public void setTabcheck(boolean tabcheck) {
        this.tabcheck = tabcheck;
    }

    public boolean isTabcheck() {
        return tabcheck;
    }

    public void process() {
        TreeItem<String> home = new TreeItem<>("Home");
        TreeItem<String> myCourse = new TreeItem<>("My Course");
        TreeItem<String> thiCuoiKy = new TreeItem<>("THI CUỐI KỲ");
        TreeItem<String> questionBank = new TreeItem<>("Question Bank");
        TreeItem<String> question = new TreeItem<>("Questions ");
        TreeItem<String> addMTPCQ = new TreeItem<>("Editing a Multiple choice question");
        TreeItem<String> addQuiz = new TreeItem<>("Add new quiz");
        TreeItem<String> category = new TreeItem<>("Category");
        TreeItem<String> importVar = new TreeItem<>("Import");
        TreeItem<String> exportVar = new TreeItem<>("Export");
        home.getChildren().addAll(myCourse);
        myCourse.getChildren().addAll(thiCuoiKy);
        thiCuoiKy.getChildren().add(questionBank);
        myCourse.getChildren().add(addQuiz);
        questionBank.getChildren().addAll(question, category, importVar, exportVar);
        question.getChildren().add(addMTPCQ);
        this.currentView.set("thi-cuoi-ky.fxml");
        this.currentTree.set(thiCuoiKy);
        this.breadConnection.put("check", questionBank);
        this.breadConnection.put("thi-cuoi-ky.fxml", thiCuoiKy);
        this.breadConnection.put("add-MTPCQ.fxml", addMTPCQ);
        this.breadConnection.put("questionbank.fxml", question);
        this.breadConnection.put("add-quiz.fxml", addQuiz);
        this.breadConnection.put("1", category);
        this.breadConnection.put("2", importVar);
        this.breadConnection.put("3", exportVar);
    }
}
