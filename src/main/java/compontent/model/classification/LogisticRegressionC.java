package compontent.model.classification;

import compontent.Component;
import org.apache.spark.ml.classification.*;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;

import java.io.IOException;


public class LogisticRegressionC extends Component {

    private LogisticRegression model = new LogisticRegression();
    private LogisticRegressionModel model_;


    private String path;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        System.out.println("training LogisticRegression model");
        model_ = model.fit(dataset);
        System.out.println("train LogisticRegression model success");
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(path != null && !path.equals("")){
            save();
            System.out.println("model saved success on" + this.path);
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
        if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(HDFSFileUtil.HDFSPath(path));
    }
    
    @Test
    public void test() throws Exception{
        run();
    }

}
