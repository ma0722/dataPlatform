package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.SQLTransformer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class SQLTransform implements FeatureBase {

    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String statement = paramPair.getString("statement");
        SQLTransformer sqlTrans = new SQLTransformer().
                setStatement(statement);
        return sqlTrans.transform(dataset);
    }
}