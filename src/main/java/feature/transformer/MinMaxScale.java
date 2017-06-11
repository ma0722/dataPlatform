package feature.transformer;


import feature.Feature;
import org.apache.spark.ml.feature.MinMaxScaler;
import org.apache.spark.ml.feature.MinMaxScalerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class MinMaxScale implements Feature{
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        MinMaxScaler scaler = new MinMaxScaler()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        MinMaxScalerModel scalerModel = scaler.fit(dataset);
        return scalerModel.transform(dataset);
    }
}