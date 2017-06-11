package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class RemoveStopWords implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        StopWordsRemover remover = new StopWordsRemover()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return remover.transform(dataset);
    }
}
