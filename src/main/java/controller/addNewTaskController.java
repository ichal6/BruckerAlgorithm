package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;

public class addNewTaskController {

    @FXML
    private Label taskIdLabel, deadlineLabel, nextTaskIdLabel, addNewTaskLabel;
    @FXML
    private TextField taskIdTextField, deadlineTextField, nextTaskIdTextField;
    @FXML
    private Button addButton, retunrButton;

    private Stage stage;
    private Scene scene;

    public void add(ActionEvent event) throws IOException {

        String taskId = taskIdTextField.getText();
        int deadline = Integer.parseInt(deadlineTextField.getText());
        String nextTaskId = nextTaskIdTextField.getText();

        Task nextTask = LoadData.tasksMap.get(nextTaskId);
        Task newTask = new Task(taskId, deadline, nextTask);
        nextTask.getPreviousTasks().add(newTask);
        LoadData.tasksMap.put("Z13", newTask);

        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getClassLoader().getResource("LayoutFXML/enter_page.fxml"));
        Parent root = loader.load();
        scene = new Scene(root);
        stage.setScene(scene);

        MainController controller = loader.getController();
        controller.setRoot(LoadData.tasksMap.get("root"));
        stage.show();
    }

    public void returnToStageOne(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
