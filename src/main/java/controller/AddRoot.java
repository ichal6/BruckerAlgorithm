package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AddRoot implements Initializable {
    @FXML
    public TextField deadlineTextField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        numberValidator();
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

    private void displayMessage(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Wrong Id");
        alert.setHeaderText(message);
        alert.setContentText("Please insert correct data");
        alert.showAndWait().ifPresent(rs -> {
        });
    }

    private void returnToMainMenu(ActionEvent event) throws IOException {
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

    public void add(ActionEvent actionEvent) throws IOException {
        if(deadlineTextField.getText().length() == 0){
            displayMessage("Please provide deadline");
            return;
        }
        int deadline = Integer.parseInt(deadlineTextField.getText());
        Task newTask = new Task("Z01", deadline, null);
        LoadData.tasksMap.put("Z01", newTask);
        LoadData.tasksMap.put("root", newTask);

        returnToMainMenu(actionEvent);
    }
}
