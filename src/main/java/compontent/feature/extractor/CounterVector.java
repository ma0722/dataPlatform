package compontent.feature.extractor;

import compontent.Component;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class CounterVector extends Component{


    private CountVectorizerModel model;
    private String[] vocabulary;

    public void run(){
        Dataset dataset = inputs.get("data").getDataset();
        Dataset data = model.transform(dataset);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("vocabulary")) {
            vocabulary = parameters.getJSONObject("vocabulary").getString("value").split(" ");
            model = new CountVectorizerModel(vocabulary);
        }
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
    }

}
