package component.model.classification;

import component.Component;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.IOException;

public class DecisionTreeC extends Component {

    private DecisionTreeClassifier model = new DecisionTreeClassifier();
    private DecisionTreeClassificationModel model_;

    private String modelPath;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        model_ = model.fit(dataset);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(modelPath != null && !modelPath.equals(""))
            save();
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("maxDepth"))
            model.setMaxDepth(parameters.getJSONObject("maxDepth").getInt("value"));
        if(parameters.has("maxBins"))
            model.setMaxBins(parameters.getJSONObject("maxBins").getInt("value"));
        if(parameters.has("MinInstancesPerNode"))
            model.setMinInstancesPerNode(parameters.getJSONObject("MinInstancesPerNode").getInt("value"));
        if(parameters.has("minInfoGain"))
            model.setMinInfoGain(parameters.getJSONObject("minInfoGain").getDouble("value"));
        if(parameters.has("cacheNodeIds"))
            model.setCacheNodeIds(parameters.getJSONObject("cacheNodeIds").getBoolean("value"));
        if(parameters.has("checkpointInterval"))
            model.setCheckpointInterval(parameters.getJSONObject("checkpointInterval").getInt("value"));
        if(parameters.has("impurity"))
            model.setImpurity(parameters.getJSONObject("impurity").getString("value"));
        if(parameters.has("modelPath"))
            this.modelPath = parameters.getJSONObject("modelPath").getString("value");
    }

    public void save() throws IOException {
        model_.save(HDFSFileUtil.HDFSPath(modelPath));
        System.out.println("model saved success on " + modelPath);
    }
    
    @Test
    public void test() throws Exception{
        Dataset dataset =  SparkUtil.readFromHDFS("/data/sample_binary_classification_data.txt", "libsvm");
        this.modelPath = "/model/decisionTree";
        this.model_ = model.fit(dataset);
        if(modelPath != null && !modelPath.equals("")){
            save();
            System.out.println("model saved success on " + this.modelPath);
        }
    }
}
