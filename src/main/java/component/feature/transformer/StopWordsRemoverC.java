package component.feature.transformer;

import component.Component;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.sql.Dataset;
import org.json.JSONObject;



public class StopWordsRemoverC extends Component {


    private StopWordsRemover model = new StopWordsRemover();


    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        Dataset data = model.transform(dataset);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setInputCol(parameters.getJSONObject("outputCol").getString("value"));

    }
}
