package algorithm;

import model.Machine;
import model.Task;

import java.util.*;

public class Brucker {
    private Integer L_Max;
    private List<Machine> machines;
    private int numberOfMachines;
    private Task root;


    public Brucker(int numberOfMachines, Task root){
        this.machines = new ArrayList<>(numberOfMachines);
        this.root = root;
        runAlgorithm();
    }

    private void runAlgorithm() {
        setPriorities(this.root);
    }

    private void setPriorities(Task task){
        Queue<Task> queue = new LinkedList<>(task.getPreviousTasks());
        while(true){
            if(task.isRoot()){
                task.setPriorities(1 - task.getD_j());
            } else{
                task.setPriorities(Math.max(1 + task.getNextTask().getPriorities(), 1 - task.getD_j()));
            }
            if(queue.isEmpty()){
                break;
            }
            task = queue.remove();
            queue.addAll(task.getPreviousTasks());
        }

    }

    public void display(){
        Set<Task> tasks = new LinkedHashSet<>();
        tasks.add(root);
        while(tasks.size() > 0){
            Set<Task> tempList = new LinkedHashSet<>();
            for(Task task: tasks){
                System.out.println(task);
                tempList.addAll(task.getPreviousTasks());
            }
            tasks.clear();
            tasks.addAll(tempList);
        }
    }
}
