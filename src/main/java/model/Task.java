package model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Task extends Node{
    private List<Task> previousTasks = new LinkedList<>();
    private Task nextTask;

    private Integer priorities;
    private String id;
    private int d_j;
    private Integer L;

    public Task(String taskId, int deadline, Task task) {
        this.id = taskId;
        this.d_j = deadline;
        this.nextTask = task;
    }

    public boolean isRoot() {
        return nextTask == null;
    }

    public boolean isLeaf(){
        return previousTasks.isEmpty();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id.equals(task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return this.id;
    }
}