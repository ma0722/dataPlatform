package feature.selector;


import feature.FeatureBase;
import org.apache.spark.ml.feature.ChiSqSelector;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class ChiSqSelect implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String featureCol = paramPair.getString("featureCol");
        final String labelCol = paramPair.getString("labelCol");
        final int numTopFeatures = paramPair.getInt("numTopFeatures");
        ChiSqSelector selector = new ChiSqSelector()
                .setNumTopFeatures(numTopFeatures)
                .setFeaturesCol(featureCol)
                .setLabelCol(labelCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return selector.fit(dataset).transform(dataset);
    }
}