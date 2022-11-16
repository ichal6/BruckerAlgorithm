package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class Graph implements Initializable {
    @FXML
    public Canvas canvas;
    private Map<String, Task> tasksMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.tasksMap = LoadData.tasksMap;
        drawGraph();
    }

    private void drawGraph(){
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        Task currentTask = this.tasksMap.get("root");
        List<Task> remainTasks = new ArrayList<>();
        List<Task> childrenTasks = new ArrayList<>(currentTask.getPreviousTasks());
        double x = 0, y = 0;
        int siblingCount = 0;

        gc.strokeOval(x, y,10,10); //draw root
        while(!childrenTasks.isEmpty() || !remainTasks.isEmpty()){
            if(remainTasks.isEmpty()){
                y+=20;
                x=0;
                remainTasks.addAll(childrenTasks);
                childrenTasks.clear();
                siblingCount = currentTask.getPreviousTasks().size();
            } else{
                currentTask = remainTasks.remove(0);
                childrenTasks.addAll(currentTask.getPreviousTasks());
                gc.strokeOval(x, y,10,10);
                x+= 20;
            }
        }

    }

    public void returnToMainMenu(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.close();
    }
}
