package component.model.classification;

import component.Component;
import org.apache.spark.ml.classification.NaiveBayes;
import org.apache.spark.ml.classification.NaiveBayesModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class NaiveBayesC extends Component {

    private NaiveBayes model = new NaiveBayes();
    private NaiveBayesModel model_;


    private String path;

    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        model_ = model.fit(dataset);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        if(path != null && path.equals(""))
            save();
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("smoothing"))
            model.setSmoothing(parameters.getJSONObject("maxIter").getInt("value"));
        if(parameters.has("modelType"))
            model.setModelType(parameters.getJSONObject("reg").getString("value"));
        if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(path);
    }

}
