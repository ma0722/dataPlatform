package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.MaxAbsScaler;
import org.apache.spark.ml.feature.MaxAbsScalerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class MaxAbsScale implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        MaxAbsScaler scaler = new MaxAbsScaler()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        MaxAbsScalerModel scalerModel = scaler.fit(dataset);
        return scalerModel.transform(dataset);
    }
}