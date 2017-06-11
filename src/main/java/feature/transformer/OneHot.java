package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.OneHotEncoder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class OneHot implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        OneHotEncoder encoder = new OneHotEncoder()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return encoder.transform(dataset);
    }
}