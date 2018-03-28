package component.model.cluster;

import component.Component;
import org.apache.spark.ml.clustering.LDA;
import org.apache.spark.ml.clustering.LDAModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class LDAC extends Component {

    private LDA model = new LDA();;
    private LDAModel model_;

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
        if(parameters.has("checkpointInterval"))
            model.setCheckpointInterval(parameters.getJSONObject("checkpointInterval").getInt("value"));
        if(parameters.has("docConcentration"))
            model.setDocConcentration(parameters.getJSONObject("docConcentration").getDouble("value"));
        if(parameters.has("topicConcentration"))
            model.setTopicConcentration(parameters.getJSONObject("topicConcentration").getDouble("value"));
        if(parameters.has("optimizer"))
            model.setOptimizer(parameters.getJSONObject("optimizer").getString("value"));
        if(parameters.has("learningOffset"))
            model.setLearningOffset(parameters.getJSONObject("learningOffset").getDouble("value"));
        if(parameters.has("topicDistributionCol"))
            model.setTopicDistributionCol(parameters.getJSONObject("topicDistributionCol").getString("value"));
        if(parameters.has("learningDecay"))
            model.setLearningDecay(parameters.getJSONObject("learningDecay").getDouble("value"));
        if(parameters.has("subsamplingRate"))
            model.setSubsamplingRate(parameters.getJSONObject("subsamplingRate").getDouble("value"));
        if(parameters.has("optimizeDocConcentration"))
            model.setOptimizeDocConcentration(parameters.getJSONObject("optimizeDocConcentration").getBoolean("value"));
        if(parameters.has("savePath"))
            this.path = parameters.getJSONObject("savePath").getString("value");
    }

    public void save() throws IOException {
        model_.save(path);
    }


}
