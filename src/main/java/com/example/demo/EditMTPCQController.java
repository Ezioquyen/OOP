package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditMTPCQController {
    @FXML
    private VBox showChoices;
    @FXML
    private TreeView<String> root;
    @FXML
    private MenuButton category;
    @FXML
    private Label label;
    @FXML
    private TextField mark;
    @FXML
    private TextField quesName;
    private int preNumberOfChoice = 0;
    private int currentChoice = 0;
    private DataModel dataModel;
    private BreadCrumbBarModel breadCrumbBarModel;
    private final List<ChoiceBoxController> controllers = new ArrayList<>();

    public void initDataModel(DataModel dataModel) {
        if (this.dataModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.dataModel = dataModel;
    }

    public void initModel(BreadCrumbBarModel breadCrumbBarModel) {
        if (this.breadCrumbBarModel != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.breadCrumbBarModel = breadCrumbBarModel;
    }

    @FXML
    private void initialize() throws IOException {
        initDataModel(DataModel.getInstance());
        initModel(BreadCrumbBarModel.getInstance());
        quesName.setText(dataModel.getCurrentQuestion().getTitle());
        mark.setText("" + dataModel.getCurrentQuestion().getMark());
        for (String option : dataModel.getCurrentQuestion().getOptions()) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (1 + currentChoice));
            view.getTextArea().setText(option);
            if (dataModel.getCurrentQuestion().getPercent().get(currentChoice) == 0.0)
                view.getComboBox().getSelectionModel().select("None");
            else {
                DecimalFormat decimalFormat = new DecimalFormat("#.######");
                decimalFormat.setGroupingUsed(false);
                view.getComboBox().getSelectionModel().select(decimalFormat.format(dataModel.getCurrentQuestion().getPercent().get(currentChoice)) + "%");
            }
            currentChoice++;
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        root.setRoot(dataModel.getRoot());
        root.getSelectionModel().select(dataModel.getCurrentCategory());
        label.setText(root.getSelectionModel().getSelectedItem().getValue());
        root.getSelectionModel().selectedItemProperty().addListener(e -> {
            label.setText(root.getSelectionModel().getSelectedItem().getValue());
        });
        preNumberOfChoice = currentChoice;
        category.setDisable(true);

    }

    @FXML
    private void btnAddThreeMoreChoices() throws IOException {
        for (int i = 1; i <= 3; i++) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("choice-box.fxml"));
            Parent parent = fxmlLoader.load();
            ChoiceBoxController view = fxmlLoader.getController();
            view.setChoice("Choice " + (i + currentChoice));
            controllers.add(view);
            showChoices.getChildren().add(parent);
        }
        currentChoice += 3;
    }

    @FXML
    private void btnSaveAndContinueEditing() throws NullPointerException {
        int index = 0;
        List<String> options = new ArrayList<>();
        List<Double> percent = new ArrayList<>();
        dataModel.getCurrentQuestion().setTitle(quesName.getText());
        dataModel.getCurrentQuestion().setMark(Double.valueOf(mark.getText()));
        for (ChoiceBoxController controller : controllers) {
            if (!Objects.equals(controller.getTextArea().getText(), "")) {
                index++;
                options.add(controller.getTextArea().getText());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    percent.add((double) 0);
                } else
                    percent.add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
            }
        }
        dataModel.getCurrentQuestion().setOptions(options);
        dataModel.getCurrentQuestion().setPercent(percent);
        dataModel.getCurrentQuestion().typeDetect();
        if (dataModel.getCurrentQuestion().getAnsID().size() > index) {
            dataModel.getCurrentQuestion().getAnsID().subList(index, dataModel.getCurrentQuestion().getAnsID().size()).clear();
        }
        dataModel.updateQuestion(dataModel.getCurrentQuestion());
        if (index > preNumberOfChoice) {
            List<String> newOption = new ArrayList<>();
            List<Double> newPercent = new ArrayList<>();
            for (int i = preNumberOfChoice; i < index; i++) {
                newOption.add(dataModel.getCurrentQuestion().getOptions().get(i));
                newPercent.add(dataModel.getCurrentQuestion().getPercent().get(i));
            }
            dataModel.insertAnswers(newOption, newPercent, dataModel.getCurrentQuestion().getId());
        } else if (index < preNumberOfChoice) {
            dataModel.deleteAns(dataModel.getCurrentQuestion(), index);
        }
    }

    @FXML
    private void btnSaveChanges() throws NullPointerException {
        int index = 0;
        List<String> options = new ArrayList<>();
        List<Double> percent = new ArrayList<>();
        dataModel.getCurrentQuestion().setTitle(quesName.getText());
        dataModel.getCurrentQuestion().setMark(Double.valueOf(mark.getText()));
        for (ChoiceBoxController controller : controllers) {
            if (!Objects.equals(controller.getTextArea().getText(), "")) {
                index++;
                options.add(controller.getTextArea().getText());
                if (Objects.equals(controller.getComboBox().getSelectionModel().getSelectedItem(), "None") || controller.getComboBox().getSelectionModel().getSelectedItem() == null) {
                    percent.add((double) 0);
                } else {
                    percent.add(Double.valueOf(controller.getComboBox().getSelectionModel().getSelectedItem().substring(0, controller.getComboBox().getSelectionModel().getSelectedItem().length() - 1)));
                }
            }
        }
        dataModel.getCurrentQuestion().setOptions(options);
        dataModel.getCurrentQuestion().setPercent(percent);
        dataModel.getCurrentQuestion().typeDetect();
        if (dataModel.getCurrentQuestion().getAnsID().size() > index) {
            dataModel.getCurrentQuestion().getAnsID().subList(index, dataModel.getCurrentQuestion().getAnsID().size()).clear();
        }
        dataModel.updateQuestion(dataModel.getCurrentQuestion());
        if (index > preNumberOfChoice) {
            List<String> newOption = new ArrayList<>();
            List<Double> newPercent = new ArrayList<>();
            for (int i = preNumberOfChoice; i < index; i++) {
                newOption.add(dataModel.getCurrentQuestion().getOptions().get(i));
                newPercent.add(dataModel.getCurrentQuestion().getPercent().get(i));
            }
            dataModel.insertAnswers(newOption, newPercent, dataModel.getCurrentQuestion().getId());
        } else if (index < preNumberOfChoice) {
            dataModel.deleteAns(dataModel.getCurrentQuestion(), index);
        }
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }

    @FXML
    private void btnCancel() {
        breadCrumbBarModel.getBreadCrumbBar().setSelectedCrumb(breadCrumbBarModel.getBreadConnection().get("questionbank.fxml"));
    }
}
