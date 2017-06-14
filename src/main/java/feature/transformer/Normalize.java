package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.Normalizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class Normalize implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final double p = paramPair.getDouble("p");
        Normalizer normalizer = new Normalizer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setP(p);
        return normalizer.transform(dataset);
    }
}