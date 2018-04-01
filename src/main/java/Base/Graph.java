package Base;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Graph {

    private HashMap<String, Node> nodes;

    private ArrayList<Edge> edges;

    public Graph(JSONObject componentJsonObject, JSONObject edgeJsonObject) throws Exception{
        nodes = new HashMap<String, Node>();
        edges = new ArrayList<Edge>();

        Iterator iterator =  componentJsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            JSONObject componentInfo = componentJsonObject.getJSONObject(key);
            JSONObject parameters =  componentInfo.getJSONObject("parameters");
            String name = componentInfo.getString("name");
            nodes.put(name, new Node(name, parameters));
        }
        iterator = edgeJsonObject.keys();
        while (iterator.hasNext()) {
            String key = (String)iterator.next();
            Node startNode = nodes.get(key.split(":")[0]);
            Node endNode = nodes.get(key.split(":")[1]);
            String input = edgeJsonObject.getJSONObject(key).getString("input");
            String output = edgeJsonObject.getJSONObject(key).getString("output");
            Edge edge = new Edge(startNode, endNode, input, output);
            startNode.getOutputs().put(input, edge);
            endNode.getInputs().put(output, edge);
        }
    }

    public void run() throws Exception{
        ArrayList<Node> nodeToposort = Toposort();
        if (nodeToposort == null || nodeToposort.size() == 0)
            return;
        for (Node node: nodeToposort) {
            node.run();
        }

    }


    private ArrayList<Node> Toposort() {
        ArrayList<Node> nodeArrayList = new ArrayList<Node>();
        HashMap<Node, Integer> nodeInDegree = new HashMap<Node, Integer>();
        for (Node node: nodes.values()) {
            nodeInDegree.put(node, node.getInputs().size());
        }
        while (nodeInDegree.size() > 0) {
            Iterator iter = nodeInDegree.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Node node = (Node) entry.getKey();
                Integer inDegree = (Integer) entry.getValue();
                if (inDegree == 0) {
                    nodeArrayList.add(node);
                    iter.remove();
                    for (Edge edge : node.getOutputs().values()) {
                        Node tmpNode = edge.getEndNode();
                        nodeInDegree.put(tmpNode, nodeInDegree.get(tmpNode) - 1);
                    }
                }
            }
        }
        if (nodeArrayList.size() != nodes.size())
            return null;
        return nodeArrayList;

    }

    public HashMap<String, Node> getNodes() {
        return nodes;
    }

    public void setNodes(HashMap<String, Node> nodes) {
        this.nodes = nodes;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

}
