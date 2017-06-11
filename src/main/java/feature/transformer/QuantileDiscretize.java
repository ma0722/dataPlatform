package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.QuantileDiscretizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class QuantileDiscretize implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int numBuckets = paramPair.getInt("numBuckets");
        QuantileDiscretizer discretizer = new QuantileDiscretizer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setNumBuckets(numBuckets);
        return discretizer.fit(dataset).transform(dataset);
    }
}