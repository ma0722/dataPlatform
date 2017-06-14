package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.Binarizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class Binarization implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final double threshold = paramPair.getInt("threshold");
        Binarizer binarizer = new Binarizer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setThreshold(threshold);
        return binarizer.transform(dataset);
    }
}
