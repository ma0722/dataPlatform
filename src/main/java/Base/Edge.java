package Base;


import org.apache.spark.ml.Model;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;

import java.io.Serializable;
import java.util.HashMap;

public class Edge implements Serializable{

    private Node startNode;

    private Node endNode;

    private String input;

    private String output;

    private Dataset dataset;

    private Vector[] vectors;

    private Model model;

    private HashMap<String, Object> others = new HashMap<String, Object>();

    public Edge(Node startNode, Node endNode, String input, String output) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.input = input;
        this.output = output;
    }

    public Vector[] getVectors() {
        return vectors;
    }

    public void setVectors(Vector[] vectors) {
        this.vectors = vectors;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public String getInput() {
        return input;
    }

    public String getOutput() {
        return output;
    }

    public Dataset getDataset() {
        return dataset;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void setEndNode(Node endNode) {
        this.endNode = endNode;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public void setDataset(Dataset dataset) {
        this.dataset = dataset;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public HashMap<String, Object> getOthers() {
        return others;
    }

}
