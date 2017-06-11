package feature.selector;

import feature.Feature;
import org.apache.spark.ml.feature.RFormula;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class RModelFormula implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String formual = paramPair.getString("formual");
        final String featureCol = paramPair.getString("featureCol");
        final String labelCol = paramPair.getString("labelCol");
        RFormula formula = new RFormula()
                .setFormula(formual)
                .setFeaturesCol(featureCol)
                .setLabelCol(labelCol);
        return formula.fit(dataset).transform(dataset);
    }
}