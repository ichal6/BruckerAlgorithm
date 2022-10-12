package controller;

import algorithm.Brucker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private TableView<Task> tableView;
    @FXML
    private TableColumn columnOne, columnTwo, columnThree, columnFour;
    @FXML
    public Label effectLabel;
    @FXML
    private TextField machinesTF;

    @FXML
    private Button addNewTaskButton, refreshButton, numberOfMachinesButton, runAlgorithmButton, lmaxButton;

    private Stage stage;
    private Scene scene;
    private Task root;
    private Brucker algorithm;
    private int machines;


    public void setRoot(Task root) {
        this.root = root;
    }

    private void fillTableWithData() {
        Map<String, Task> tasks = LoadData.tasksMap;
        tasks.forEach(
                (key, value) -> tableView.getItems().add(value)
        );
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        columnOne.setCellValueFactory(new PropertyValueFactory<Task, Integer>("id"));
        columnTwo.setCellValueFactory(new PropertyValueFactory<Task, Integer>("d"));
        columnThree.setCellValueFactory(new PropertyValueFactory<Task, Task>("nextTaskID"));
        columnFour.setCellValueFactory(new PropertyValueFactory<Task, Integer>("L"));
        fillTableWithData();
        this.runAlgorithmButton.setDisable(true);
        lmaxButton.setDisable(true);
        numberValidator();
    }


    public void addNewTask(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("addNewTask.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void setMachines(ActionEvent event) {
        this.machines = Integer.parseInt(machinesTF.getText());
        this.runAlgorithmButton.setDisable(false);
    }

    public void runAlgorithm(ActionEvent event) {
        this.algorithm = new Brucker(this.machines, this.root);
        algorithm.runAlgorithm();
        lmaxButton.setDisable(false);
        this.runAlgorithmButton.setDisable(false);
    }

    public void showLmax(ActionEvent event) {
        effectLabel.setText(String.valueOf(algorithm.getL_Max()));
    }

    private void numberValidator() {
        machinesTF.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(
                    "([1-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-9][0-9][0-9][0-9][0-9])?")) {
                boolean disableButton = change.getControlNewText().length() == 0;
                this.numberOfMachinesButton.setDisable(disableButton);
                return change;
            } else {
                boolean disableButton = this.machinesTF.lengthProperty().get() == 0;
                this.numberOfMachinesButton.setDisable(disableButton);
                return null;
            }
        }));
    }
}
