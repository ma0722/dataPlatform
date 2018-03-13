package compontent.model.cluster;

import compontent.Component;
import org.apache.spark.ml.clustering.GaussianMixture;
import org.apache.spark.ml.clustering.GaussianMixtureModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GMMC extends Component {

    private GaussianMixture model = new GaussianMixture();;
    private GaussianMixtureModel model_;

    private String path;


    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        model_ = model.fit(dataset);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(path != null && path.equals("")) {
            save();
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
        if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(path);
    }


}
