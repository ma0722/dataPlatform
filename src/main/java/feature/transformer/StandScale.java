package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.StandardScalerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class StandScale implements Feature{
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final boolean withMean = paramPair.getBoolean("withMean");
        final boolean withStd = paramPair.getBoolean("withStd");
        StandardScaler scaler = new StandardScaler()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setWithStd(withStd)
                .setWithMean(withMean);
        StandardScalerModel scalerModel = scaler.fit(dataset);
        return scalerModel.transform(dataset);
    }
}