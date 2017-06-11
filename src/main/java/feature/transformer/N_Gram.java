package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.NGram;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class N_Gram implements Feature {

    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int n = paramPair.getInt("N");
        NGram nGram = new NGram()
                .setN(n)
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return nGram.transform(dataset);
    }
}