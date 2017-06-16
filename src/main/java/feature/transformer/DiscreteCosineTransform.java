package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.DCT;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class DiscreteCosineTransform implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final boolean inverse = paramPair.getBoolean("inverse");
        DCT dct = new DCT()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setInverse(inverse);
        return dct.transform(dataset);
    }
}