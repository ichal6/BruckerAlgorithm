package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
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
        final double size = 50;
        final double xCanvas = this.canvas.getWidth();
        GraphicsContext gc = this.canvas.getGraphicsContext2D();
        Task currentTask = this.tasksMap.get("root");
        List<Task> remainTasks = new ArrayList<>(Collections.singletonList(currentTask));
        List<Task> childrenTasks = new ArrayList<>();
        double x = xCanvas/2, y = 5, xParent = x, yParent = y, offset=0;
        int siblingCount = 2;
        boolean disableAngle = false;

        while(!childrenTasks.isEmpty() || !remainTasks.isEmpty()){
            if(remainTasks.isEmpty()){
                xParent = (xCanvas/(siblingCount));
                offset = xParent;
                siblingCount = childrenTasks.size() + 1;
                yParent = y;
                y+=2*size;
                x=(xCanvas/(siblingCount));

                remainTasks.addAll(childrenTasks);
                childrenTasks.clear();
            } else{
                Task nextTask = currentTask.getNextTask();
                currentTask = remainTasks.remove(0);
                disableAngle = nextTask == currentTask.getNextTask();
                if(nextTask != currentTask.getNextTask() && x > xParent){
                    xParent += offset;
                }
                childrenTasks.addAll(currentTask.getPreviousTasks());
                gc.strokeOval(x, y,size,size);
                gc.setFill(Color.rgb(217,217,217));
                gc.fillOval(x, y,size,size);
                gc.setFill(Color.GRAY);
                gc.setFont(new Font("Inter", 12));
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(VPos.CENTER);
                gc.fillText(currentTask.getId() + ";" + currentTask.getD_j(),x+size/2,y+size/2);
                if(!currentTask.isRoot()){
                    drawArrowLine(x+size/2,y, xParent+size/2, yParent+size, disableAngle, gc);
                }
                if(y>this.canvas.getHeight() || x > this.canvas.getWidth()){
                    this.canvas.setHeight(y+size*2);
                    this.canvas.setWidth(x+size);
                }
                x+= xCanvas/(siblingCount);
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
