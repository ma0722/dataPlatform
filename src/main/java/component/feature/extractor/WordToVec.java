package component.feature.extractor;

import component.Component;
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.json.JSONObject;


public class WordToVec extends Component {


    private Word2Vec model = new Word2Vec();
    private Word2VecModel model_;


    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();


        model_ = model.fit(dataset);


        if(outputs.containsKey("model"))
            outputs.get("model").setModel(model_);
        Dataset out = model_.transform(dataset);

        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(out);
    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("vectorSize"))
            model.setVectorSize(parameters.getJSONObject("vectorSize").getInt("value"));
        if(parameters.has("minCount"))
            model.setMinCount(parameters.getJSONObject("minCount").getInt("value"));
        if(parameters.has("windowSize"))
            model.setWindowSize(parameters.getJSONObject("windowSize").getInt("value"));
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
    }
}
