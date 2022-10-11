package algorithm;

import model.Machine;
import model.Task;

import java.util.*;

public class Brucker {
    private Integer L_Max;
    private List<Machine> machines;
    private final int numberOfMachines;
    private final Task root;
    private List<Task> upperTask;


    public Brucker(int numberOfMachines, Task root){
        this.machines = new ArrayList<>(numberOfMachines);
        for(int i = 0; i< numberOfMachines; i++){
            this.machines.add(new Machine());
        }
        this.numberOfMachines = numberOfMachines;
        this.upperTask = new ArrayList<>();
        this.root = root;
        runAlgorithm();

    }

    private void runAlgorithm() {
        setPriorities(this.root);
        upperTask.sort(Comparator.comparing(Task::getPriorities).reversed().thenComparing(Task::getId));
        for(int i = 0; i < this.numberOfMachines; i++){
            this.machines.get(i).addNode(upperTask.get(i));
        }
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

            if(task.isLeaf())
                this.upperTask.add(task);
            else
                queue.addAll(task.getPreviousTasks());
        }

    }

    public void displaytasks(){
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

    public void displayMachines(){
        for(Machine machine: machines){
            System.out.println(machine.getNodes());
        }
    }
}
