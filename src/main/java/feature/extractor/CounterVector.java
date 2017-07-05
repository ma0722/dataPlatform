package feature.extractor;

import feature.FeatureBase;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class CounterVector implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        String[] vocabulary = paramPair.getString("vocabulary").split(" ");
        CountVectorizerModel cvModel = new CountVectorizerModel(vocabulary)
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return cvModel.transform(dataset);
    }
}
