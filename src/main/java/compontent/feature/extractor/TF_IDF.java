package compontent.feature.extractor;

import compontent.Component;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class TF_IDF extends Component {


    private Tokenizer tokenizer;
    private HashingTF hashingTF;
    private IDF idf;

    public TF_IDF() {
        tokenizer = new Tokenizer();
        hashingTF = new HashingTF();
        idf = new IDF();
    }

    public void run() throws Exception {
        tokenizer.setOutputCol("tokenizer_output");
        Dataset dataset = inputs.get("data").getDataset();
        Dataset wordsData = tokenizer.transform(dataset);
        hashingTF.setInputCol("tokenizer_output").setOutputCol("raw_features_TF_IDF");
        Dataset featurizedData = hashingTF.transform(wordsData);
        idf.setInputCol("raw_features_TF_IDF");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset dataset1 = idfModel.transform(featurizedData);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(dataset1);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(idfModel);
    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("inputCol"))
            tokenizer.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            idf.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
        if(parameters.has("numFeatures"))
            hashingTF.setNumFeatures(parameters.getJSONObject("numFeatures").getInt("value"));
    }
}
