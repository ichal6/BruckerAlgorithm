package controller;

import algorithm.Brucker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import model.Machine;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    public Button drawGraph, showResults, hideResults;
    private GridPane resultsGrid;
    @FXML
    public Group resultGroup;
    @FXML
    private TableView<Task> tableView;
    @FXML
    private TableColumn<Task, String> taskIdColumn;
    @FXML
    private TableColumn<Task, Integer> deadlineColumn;
    @FXML
    private TableColumn<Task, Task> nextTaskColumn;
    @FXML
    private TableColumn<Task, Integer> LColumn;
    @FXML
    public Label L_maxLabel;
    @FXML
    private TextField machinesTF;

    @FXML
    private Button addNewTaskButton, refreshButton, numberOfMachinesButton, runAlgorithmButton;

    private Stage stage;
    private Scene scene;
    private Task root;
    private Brucker algorithm;
    private int machines;


    public void setRoot(Task root) {
        this.root = root;
    }

    public void fillTableWithData() {
        Map<String, Task> tasks = LoadData.tasksMap;
        tasks.forEach(
                (key, value) -> {
                    if(!key.equals("root"))
                        tableView.getItems().add(value);
                }
        );
        this.tableView.getSortOrder().add(taskIdColumn);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.scene = this.tableView.getScene();
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("d_j"));
        nextTaskColumn.setCellValueFactory(new PropertyValueFactory<>("nextTask"));
        LColumn.setCellValueFactory(new PropertyValueFactory<>("L"));
        fillTableWithData();
        this.runAlgorithmButton.setDisable(true);
        showResults.setDisable(true);
        numberValidator();
    }

    public void addNewTask(ActionEvent event) throws IOException {
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("LayoutFXML/add_new_task.fxml"));
        stage.setUserData(this);
        Parent root = loader.load();
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
        this.runAlgorithmButton.setDisable(false);
        this.tableView.getSortOrder().add(taskIdColumn);
        this.tableView.refresh();
        this.tableView.getScene().getWindow().setHeight(600);
        machinesTable(algorithm.getMachines());
        showLMax();
    }

    private void showLMax(){
        this.L_maxLabel.setText(algorithm.getL_Max().toString());
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

    private void machinesTable(List<Machine> machines){
        if (this.resultsGrid != null){
            this.resultsGrid.getChildren().clear();
        }
        GridPane gridPane = new GridPane();
        this.resultsGrid = gridPane;
        gridPane.setLayoutX(40);
        gridPane.setLayoutY(320);
        gridPane.getChildren().clear();
//        gridPane.setMinSize(500,500);
        gridPane.setAlignment(Pos.TOP_LEFT);
        gridPane.setGridLinesVisible(true);

        int maxSize = 0;
        for(Machine machine: machines){
            int size = machine.getNodes().size();
            if(size > maxSize){
                maxSize = size;
            }
        }

        for(int k = 1; k <= maxSize; k++){
            Label label = new Label(String.valueOf(k));
            label.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
            label.setPadding(new Insets(10,10,10,10));
            GridPane.setHalignment(label, HPos.CENTER);
            gridPane.add(label, k, 0);
        }

        int j = 1, i = 1;
        for(Machine machine: machines){
            Label label = new Label("M" + i);
            label.setPadding(new Insets(10,10,10,10));
            GridPane.setHalignment(label, HPos.CENTER);
            label.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
            gridPane.add(label, 0, i);
            for(model.Node node: machine.getNodes()){
                String taskName = "-";
                if(node instanceof Task task) {
                    taskName = task.getId();
                }
                label = new Label(taskName);
                label.setFont(new Font("Arial", 24));
                label.setPadding(new Insets(10,10,10,10));
                gridPane.add(label, j, i);
                j++;
            }
            j=1;
            i++;
        }
        this.resultGroup.getChildren().add(gridPane);
    }

    public void showGraph(ActionEvent actionEvent) {
        System.out.println("Test click");
    }
}
