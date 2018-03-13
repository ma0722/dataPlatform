package compontent;

import Base.Edge;
import org.json.JSONObject;

import java.util.HashMap;

public abstract class Component {

    protected String name;
    protected HashMap<String, Edge> inputs;
    protected HashMap<String, Edge> outputs;

    public abstract void run() throws Exception;

    public abstract void setParameters(JSONObject parameters) throws Exception;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInputs(HashMap<String, Edge> inputs) {
        this.inputs = inputs;
    }

    public void setOutputs(HashMap<String, Edge> outputs) {
        this.outputs = outputs;
    }

    public HashMap<String, Edge> getOutputs() {
        return outputs;
    }

}
