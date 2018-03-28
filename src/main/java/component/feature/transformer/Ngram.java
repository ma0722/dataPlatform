package component.feature.transformer;

import component.Component;;
import org.apache.spark.ml.feature.NGram;
import org.apache.spark.sql.Dataset;
import org.json.JSONObject;


public class Ngram extends Component {


    private NGram model = new NGram();


    public void run() throws Exception {
        Dataset dataset = inputs.get("data").getDataset();
        Dataset data = model.transform(dataset);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("N"))
            model.setN(parameters.getJSONObject("N").getInt("value"));
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setInputCol(parameters.getJSONObject("outputCol").getString("value"));
    }
}
