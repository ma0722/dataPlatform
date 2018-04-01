package Base;

import component.Component;
import org.json.JSONObject;
import util.ComponentUtil;

import java.io.Serializable;
import java.util.HashMap;


public class Node implements Serializable{

    private String name;
    private HashMap<String, Edge> inputs;
    private HashMap<String, Edge> outputs;

    private Component component;

    public Node(String name, JSONObject parameters) throws Exception{
        this.name = name;
        String type = name.split("_")[0];
        System.out.println(type);
        component = ComponentUtil.generateComponent(type);
        System.out.println(String.format("========================%s copoment init succsss" , name));
        component.setParameters(parameters);
        System.out.println(String.format("========================%s copoment setParameters success", name));
        inputs = new HashMap<String, Edge>();
        outputs = new HashMap<String, Edge>();
    }

    public void run() throws Exception{
        component.setInputs(inputs);
        component.setOutputs(outputs);
        System.out.println(String.format("========================start running %s component ", name));
        component.run();
        System.out.println(String.format("========================%s component run success!", name));
        outputs = component.getOutputs();
    }

    public HashMap<String, Edge> getInputs() {
        return inputs;
    }

    public HashMap<String, Edge> getOutputs() {
        return outputs;
    }

    public Component getComponent() {
        return component;
    }

    public void setInputs(HashMap<String, Edge> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(HashMap<String, Edge> outputs) {
        this.outputs = outputs;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
