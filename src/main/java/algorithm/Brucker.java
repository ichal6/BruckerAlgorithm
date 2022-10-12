package algorithm;

import model.Machine;
import model.Node;
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
    }

    public void runAlgorithm() {
        setPriorities(this.root);

        List<Task> finishJobs = new LinkedList<>();
        Set<Task> possibleReadyTasks = new LinkedHashSet<>();
        int workTime = 1;

        do {
            upperTask.sort(Comparator.comparing(Task::getPriorities).reversed().thenComparing(Task::getId));
            for(int i = 0; i < this.numberOfMachines; i++){
                if(i >= upperTask.size()){
                    this.machines.get(i).addNode(new Node()); //insert empty Node for IDLE processor
                } else{
                    this.machines.get(i).addNode(upperTask.get(i));
                    int actualL = workTime - upperTask.get(i).getD_j();
                    upperTask.get(i).setL(actualL);
                    if (L_Max == null || actualL > L_Max){
                        this.L_Max = actualL;
                    }
                    finishJobs.add(this.upperTask.get(i));
                    if(this.upperTask.get(i).getNextTask() != null)
                        possibleReadyTasks.add(this.upperTask.get(i).getNextTask());
                }
            }
            possibleReadyTasks.stream().forEach(task -> {
                if(finishJobs.containsAll(task.getPreviousTasks())){
                    upperTask.add(task);
                }
            });
            this.upperTask.removeAll(finishJobs);
            workTime++;
        } while(!upperTask.isEmpty());

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

    public Integer getL_Max() {
        return L_Max;
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
        System.out.println("L_Max = " + this.L_Max);
    }
}
