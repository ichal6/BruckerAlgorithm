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
import javafx.scene.layout.VBox;
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
    public Button drawGraph, showResults, hideResults;
    public VBox vBoxResult;
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
    public Label L_maxLabel, L_maxValue;
    @FXML
    private TextField machinesTF;

    @FXML
    private Button addNewTaskButton, refreshButton, numberOfMachinesButton, runAlgorithmButton;

    private Stage stage;
    private Scene scene;
    private Task root;
    private Brucker algorithm;
    private int machines;
    private final double startPoint = 320.0;


    public void setRoot(Task root) {
        this.root = root;
    }

    public void fillTableWithData() {
        if(LoadData.isDataCorrect()) {
            Map<String, Task> tasks = LoadData.tasksMap;
            tasks.forEach(
                    (key, value) -> {
                        if(!key.equals("root"))
                            tableView.getItems().add(value);
                    }
            );
            this.tableView.getSortOrder().add(taskIdColumn);
        } else{
            LoadData.tasksMap.clear();
        }

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
        this.drawGraph.setDisable(LoadData.tasksMap.isEmpty());
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

    public void showGraph(ActionEvent event) throws IOException {
        this.stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("LayoutFXML/graph.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = new Stage();
        stage.setTitle("Graph");
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.stage);
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void setMachines(ActionEvent event) {
        this.machines = Integer.parseInt(machinesTF.getText());
        if(LoadData.tasksMap.size() > 2)
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
        this.L_maxLabel.setLayoutY(this.startPoint + 38*(this.machines + 1));
        this.L_maxValue.setLayoutY(this.startPoint + 38*(this.machines + 1));

        this.L_maxValue.setText(algorithm.getL_Max().toString());
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
        gridPane.setLayoutY(this.startPoint);
        gridPane.getChildren().clear();
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

    public void showResults(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setWidth(600);
        stage.setHeight(600);
        this.showResults.setDisable(true);
    }

    public void hideResults(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setWidth(600);
        stage.setHeight(290);
        this.showResults.setDisable(false);
    }

    public void resetData(ActionEvent actionEvent) throws IOException {
        LoadData.tasksMap.clear();
        this.tableView.getItems().clear();
        this.drawGraph.setDisable(true);
        addNewRoot(actionEvent);
    }

    private void addNewRoot(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("LayoutFXML/add_root.fxml"));
        stage.setUserData(this);
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void isCorrect(ActionEvent actionEvent) {
        boolean isCorrectData = LoadData.isDataCorrect();
        if(isCorrectData){
            correctData();
        }else{
            wrongData();
        }
    }

    private void wrongData(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong data");
        alert.setHeaderText("Problem with nodes.");
        alert.setContentText("Please insert correct data");
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    private void correctData(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dataset OK");
        alert.setHeaderText("Everything OK.");
        alert.setContentText("Dataset is OK");
        alert.showAndWait().ifPresent(rs -> {
        });
    }
}
