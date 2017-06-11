package feature.transformer;


import feature.Feature;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class StringIndex implements Feature{
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        StringIndexer indexer = new StringIndexer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return indexer.fit(dataset).transform(dataset);
    }
}
