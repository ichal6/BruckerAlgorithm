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
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import model.Task;

import java.io.IOException;
import java.net.URL;
import java.util.*;

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
        final double size = 30;
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        Task currentTask = this.tasksMap.get("root");
        List<Task> remainTasks = new ArrayList<>(Collections.singletonList(currentTask));
        List<Task> childrenTasks = new ArrayList<>();
        double x = 0, y = 0, xParent = 0, yParent = 0;
        int siblingCount = 0;
        boolean disableAngle = false;

        while(!childrenTasks.isEmpty() || !remainTasks.isEmpty()){
            if(remainTasks.isEmpty()){
                yParent = y;
                xParent = 0;
                y+=2*size;
                x=0;
                remainTasks.addAll(childrenTasks);
                childrenTasks.clear();
                siblingCount = currentTask.getPreviousTasks().size();
            } else{
                Task nextTask = currentTask.getNextTask();
                currentTask = remainTasks.remove(0);
                disableAngle = nextTask == currentTask.getNextTask();
                if(nextTask != currentTask.getNextTask() && x > 0){
                    xParent += 2*size;
                }
                childrenTasks.addAll(currentTask.getPreviousTasks());
                gc.strokeOval(x, y,size,size);
                gc.setFill(Color.rgb(217,217,217));
                gc.fillOval(x, y,size,size);
                if(!currentTask.isRoot()){
                    drawArrowLine(x+size/2,y, xParent+size/2, yParent+size, disableAngle, gc);
                }
                x+= 2*size;
            }
        }
    }

    private void drawArrowLine(double startX, double startY, double endX, double endY, boolean disableAngle, GraphicsContext gc) {
        gc.strokeLine(startX, startY, endX, endY);
        if(disableAngle)
            return;
        // get the slope of the line and find its angle
        double slope = (startY - endY) / (startX - endX);
        double lineAngle = Math.atan(slope);
        double arrowAngle = startX >= endX ? Math.toRadians(45) : -Math.toRadians(225);

        double arrowLength = 20;

        // create the arrow legs
        gc.strokeLine(endX, endY, endX + arrowLength * Math.cos(lineAngle - arrowAngle), endY + arrowLength * Math.sin(lineAngle - arrowAngle));
        gc.strokeLine(endX, endY, endX + arrowLength * Math.cos(lineAngle + arrowAngle), endY + arrowLength * Math.sin(lineAngle + arrowAngle));
    }

    public void returnToMainMenu(ActionEvent event){
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        stage.close();
    }
}
