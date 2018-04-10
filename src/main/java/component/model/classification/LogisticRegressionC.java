package component.model.classification;

import component.Component;
import org.apache.spark.ml.classification.*;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.IOException;


public class LogisticRegressionC extends Component {

    private LogisticRegression model = new LogisticRegression();
    private LogisticRegressionModel model_;


    private String modelPath;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        System.out.println("training LogisticRegression model");
        model_ = model.fit(dataset);
        System.out.println("train LogisticRegression model success");
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(modelPath != null && !modelPath.equals("")){
            save();
        }
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("maxIter"))
            model.setMaxIter(parameters.getJSONObject("maxIter").getInt("value"));
        if(parameters.has("reg"))
            model.setRegParam(parameters.getJSONObject("reg").getDouble("value"));
        if(parameters.has("elasticNet"))
            model.setElasticNetParam(parameters.getJSONObject("elasticNet").getDouble("value"));
        if(parameters.has("tol"))
            model.setTol(parameters.getJSONObject("tol").getDouble("value"));
        if(parameters.has("features"))
            model.setFeaturesCol(parameters.getJSONObject("features").getString("value"));
        if(parameters.has("label"))
            model.setFeaturesCol(parameters.getJSONObject("label").getString("value"));
        if(parameters.has("modelPath"))
            this.modelPath = parameters.getJSONObject("modelPath").getString("value");
    }

    public void save() throws IOException {
        model_.save(HDFSFileUtil.HDFSPath(modelPath));
        System.out.println("model saved success on " + this.modelPath);
    }
    
    @Test
    public void test() throws Exception{
        Dataset dataset =  SparkUtil.readFromHDFS("/data/sample_binary_classification_data.txt", "libsvm");
        this.modelPath = "/model/LogisticRegression";
        this.model_ = model.fit(dataset);
        if(modelPath != null && !modelPath.equals("")){
            save();
            System.out.println("model saved success on " + this.modelPath);
        }
    }

}
