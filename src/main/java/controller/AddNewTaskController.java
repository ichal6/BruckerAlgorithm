package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class AddNewTaskController implements Initializable {

    @FXML
    private Label taskIdLabel, deadlineLabel, nextTaskIdLabel, addNewTaskLabel;
    @FXML
    private TextField taskIdTextField, deadlineTextField, nextTaskIdTextField;
    @FXML
    private Button addButton, returnButton;

    private final Map<String, Task> tasks = LoadData.tasksMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberValidator();
    }

    public void add(ActionEvent event) throws IOException {
        String taskId = taskIdTextField.getText();
        if (!validatorTaskID(taskId)){
            displayMessage("Wrong id");
            return;
        }
        if(deadlineTextField.getText().length() == 0){
            displayMessage("Please provide deadline");
            return;
        }
        int deadline = Integer.parseInt(deadlineTextField.getText());
        String nextTaskId = nextTaskIdTextField.getText();
        if (validatorTaskID(nextTaskId)){
            displayMessage("Wrong Next Id");
            return;
        }

        Task nextTask = LoadData.tasksMap.get(nextTaskId);
        Task newTask = new Task(taskId, deadline, nextTask);
        nextTask.getPreviousTasks().add(newTask);
        LoadData.tasksMap.put("Z13", newTask);

        returnToMainMenu(event);
    }

    public void returnToMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("LayoutFXML/enter_page.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        stage.setScene(scene);

        MainController controller = loader.getController();
        controller.setRoot(LoadData.tasksMap.get("root"));
        stage.show();
    }

    private void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong Id");
        alert.setHeaderText(message);
        alert.setContentText("Please insert correct data");
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    private boolean validatorTaskID(String id){
        return !this.tasks.containsKey(id);
    }

    private void numberValidator() {
        deadlineTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches(
                    "([1-9]|[1-9][0-9]|[1-9][0-9][0-9]|[1-9][0-9][0-9][0-9]|[1-9][0-9][0-9][0-9][0-9])?")) {
                return change;
            } else {
                displayMessage("Wrong deadline value");
                change.setText("1");
                return change;
            }
        }));
    }
}
