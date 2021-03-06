package component.model.cluster;

import component.Component;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.IOException;

public class KmeansC extends Component {

    private KMeans model = new KMeans();
    private KMeansModel model_;

    private String modelPath;
    private String dataPath;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        dataset = dataset.cache();
        System.out.println("----------training Kmeans model----------");
        model_ = model.fit(dataset);
        System.out.println("----------model Kmeans train success----------");
        Vector[] vectors = model_.clusterCenters();
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(outputs.containsKey("vectors"))
            outputs.get("vectors").setVectors(vectors);
        if(dataPath != null && !dataPath.equals("")) {
            Dataset result = model_.transform(dataset);
//            result.write().csv(HDFSFileUtil.HDFSPath(dataPath));
            while (HDFSFileUtil.checkFile(dataPath)) {
                HDFSFileUtil.delFile(dataPath, true);
            }
            result.show();
            result.write().save(HDFSFileUtil.HDFSPath(dataPath));
            System.out.println("data saved success on " + HDFSFileUtil.HDFSPath(dataPath));
        }
        if(modelPath != null && !modelPath.equals("")) {
            save();
        }
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("K"))
            model.setK(parameters.getJSONObject("K").getInt("value"));
        if(parameters.has("seed"))
            model.setSeed(parameters.getJSONObject("seed").getLong("value"));
        if(parameters.has("initSteps"))
            model.setInitSteps(parameters.getJSONObject("initSteps").getInt("value"));
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
        while (HDFSFileUtil.checkFile(modelPath))
            HDFSFileUtil.delFile(modelPath, true);
        model_.save(HDFSFileUtil.HDFSPath(modelPath));
        System.out.println("model saved success on " + HDFSFileUtil.HDFSPath(modelPath));
    }
    
    
    @Test
    public void test() throws Exception{
        this.modelPath = "/mode/Kmeans";
        Dataset dataset =  SparkUtil.readFromHDFS("/tmp/sample_cluster_data.txt", "libsvm");
        model_ = model.fit(dataset);
        if(modelPath != null && !modelPath.equals("")){
            save();
            System.out.println("model saved success on " + this.modelPath);
        }
    }
}
