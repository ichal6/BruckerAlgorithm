package model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Task implements Node{
    private Node previousTask;
    private Node nextTask;
    private int id;
    private int d_j;
    private int L;

    public Task(Node previousTask, Node nextTask, int id, int d_j) {
        this.previousTask = previousTask;
        this.nextTask = nextTask;
        this.id = id;
        this.d_j = d_j;
    }

    public boolean isRoot() {
        return nextTask == null;
    }
}