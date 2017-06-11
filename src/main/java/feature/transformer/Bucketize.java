package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.Bucketizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class Bucketize implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String[] split_string = paramPair.getString("splits").split(",");
        double[] splits = new double[split_string.length];
        for(int i = 0; i < split_string.length; i++){
            splits[i] = Double.valueOf(split_string[i]);
        }
        Bucketizer bucketizer = new Bucketizer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setSplits(splits);
        return bucketizer.transform(dataset);
    }
}