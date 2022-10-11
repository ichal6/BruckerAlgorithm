package model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Machine {
    private final List<Node> executedList;

    public Machine() {
        this.executedList = new LinkedList<>();
    }

    public void addNode(Node node){
        this.executedList.add(node);
    }

    public List<Node> getNodes(){
        return new ArrayList<>(executedList);
    }
}
