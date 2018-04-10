package component.model.cluster;

import component.Component;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.IOException;

public class GMMC extends Component {

    private GaussianMixture model = new GaussianMixture();;
    private GaussianMixtureModel model_;

    private String modelPath;
    private String dataPath;


    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        model_ = model.fit(dataset);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(modelPath != null && !modelPath.equals("")) {
            save();
        }
        if(dataPath != null && !dataPath.equals("")) {
            Dataset result = model_.transform(dataset);
            result.show();
            result.write().csv(HDFSFileUtil.HDFSPath(dataPath));
            System.out.println("data saved success on " + HDFSFileUtil.HDFSPath(dataPath));
        }
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("K"))
            model.setK(parameters.getJSONObject("K").getInt("value"));
        if(parameters.has("seed"))
            model.setSeed(parameters.getJSONObject("seed").getLong("value"));
        if(parameters.has("maxIter"))
            model.setMaxIter(parameters.getJSONObject("maxIter").getInt("value"));
        if(parameters.has("tol"))
            model.setTol(parameters.getJSONObject("tol").getDouble("value"));
        if(parameters.has("modelPath"))
            this.modelPath = parameters.getJSONObject("modelPath").getString("value");
        if(parameters.has("dataPath"))
            this.dataPath = parameters.getJSONObject("dataPath").getString("value");
        if(parameters.has("features"))
            model.setFeaturesCol(parameters.getJSONObject("features").getString("value"));
    }

    public void save() throws IOException {
        model_.save(HDFSFileUtil.HDFSPath(modelPath));
        System.out.println("model saved success on " + this.modelPath);
    }
    
    @Test
    public void test() throws Exception{
        Dataset dataset =  SparkUtil.readFromHDFS("/data/sample_cluster_data.txt", "libsvm");
        model_ = model.fit(dataset);
        this.modelPath = "/mode/GMM";
        if(modelPath != null && !modelPath.equals("")){
            save();
            System.out.println("model saved success on " + this.modelPath);
        }
    }

}
