package feature.extractor;

import feature.Feature;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class CounterVector implements Feature{
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        String[] vocabulary = paramPair.getString("vocabulary").split(" ");
        CountVectorizerModel cvModel = new CountVectorizerModel(vocabulary)
                .setInputCol(inputCol)
                .setOutputCol(inputCol + CounterVector.class.getName());
        return cvModel.transform(dataset);
    }
}
