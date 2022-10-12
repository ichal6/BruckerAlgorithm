package controller;

import algorithm.Brucker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Modality;
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
    private Button addNewTaskButton, refreshButton, numberOfMachinesButton, runAlgorithmButton, lmaxButton;

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
        taskIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        deadlineColumn.setCellValueFactory(new PropertyValueFactory<>("d_j"));
        nextTaskColumn.setCellValueFactory(new PropertyValueFactory<>("nextTask"));
        LColumn.setCellValueFactory(new PropertyValueFactory<>("L"));
        fillTableWithData();
        this.runAlgorithmButton.setDisable(true);
        lmaxButton.setDisable(true);
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
        lmaxButton.setDisable(false);
        this.runAlgorithmButton.setDisable(false);
        this.tableView.getSortOrder().add(taskIdColumn);
        this.tableView.refresh();
        machinesTable(algorithm.getMachines());
    }

    public void showLmax(ActionEvent event) {
        L_maxLabel.setText("L_MAX = " + algorithm.getL_Max());
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

        GridPane gridPane = new GridPane();
        gridPane.setMinSize(500,500);
        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setVgap(5);
        gridPane.setHgap(20);
        gridPane.setAlignment(Pos.CENTER);

        int maxSize = 0;
        for(Machine machine: machines){
            int size = machine.getNodes().size();
            if(size > maxSize){
                maxSize = size;
            }
        }

        for(int k = 0; k < maxSize; k++){
            Label label = new Label(String.valueOf(k + 1));
            label.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
            gridPane.add(label, k, 0);
        }

        int j = 0, i = 1;
        for(Machine machine: machines){
            for(model.Node node: machine.getNodes()){
                String taskName = "-";
                if(node instanceof Task task) {
                    taskName = task.getId();
                }
                Label label = new Label(taskName);
                label.setFont(new Font("Arial", 24));
                gridPane.add(label, j, i);
                j++;
            }
            j=0;
            i++;
        }
        Scene scene = new Scene(gridPane);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Result of Algorithm");
        stage.setScene(scene);
        stage.show();
    }
}
