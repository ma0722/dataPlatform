package feature;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public interface Feature {
    abstract public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException;
}
