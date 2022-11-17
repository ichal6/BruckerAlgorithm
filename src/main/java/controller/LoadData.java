package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class LoadData {

    public static final Map<String, Task> tasksMap = new HashMap<>();

    public static Task loadFromFile() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        ObjectMapper objectMapper = new ObjectMapper();
        FileReader fileReader = new FileReader("src/main/resources/defaultData.json");

        JSONArray a = (JSONArray) parser.parse(fileReader);
        Object[] objects = a.toArray();
        Task root = null;
        for(Object task: objects){
            Task nextTask = null;
            JSONObject taskObject = (JSONObject) task;
            Task newTask = objectMapper.readValue(task.toString(), Task.class);

            String nextTaskId = (String) taskObject.get("nextTaskId");
            if(nextTaskId != null){
                nextTask = tasksMap.get(nextTaskId);
                nextTask.getPreviousTasks().add(newTask);
            } else{
                root = newTask;
                tasksMap.put("root", root);
            }
            newTask.setNextTask(nextTask);
            tasksMap.put(newTask.getId(), newTask);
        }
        return root;
    }


    public static boolean isDataCorrect(){
        Task root = LoadData.tasksMap.get("root");
        List<Task> allTasks = new ArrayList<>(LoadData.tasksMap.values());
        List<Task> leafs = allTasks.stream().filter(t -> !t.isRoot()).filter(Task::isLeaf).toList();
        for(Task task: leafs){
            Task currentTask = task;
            while(currentTask != root){
                currentTask = currentTask.getNextTask();
                if(currentTask == null){
                    return false;
                }
            }
        }
        return true;
    }

}
