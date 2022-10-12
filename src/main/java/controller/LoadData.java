package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Task;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            }
            newTask.setNextTask(nextTask);
            tasksMap.put(newTask.getId(), newTask);
        }
        return root;
    }

}
