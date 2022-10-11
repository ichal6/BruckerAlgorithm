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
public class Task implements Node{
    private List<Task> previousTasks = new LinkedList<>();
    private Task nextTask;

    private Integer priorities;
    private String id;
    private int d_j;
    private Integer L;

    public boolean isRoot() {
        return nextTask == null;
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
        return "Task{" +
                "id='" + id + '\'' +
                ", d_j=" + d_j +
                ", L=" + L +
                ", Priorities=" + priorities +
                '}';
    }
}