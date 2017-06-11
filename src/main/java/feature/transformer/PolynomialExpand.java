package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.PolynomialExpansion;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class PolynomialExpand implements Feature{
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int degree = paramPair.getInt("degreee");
        PolynomialExpansion polyExpansion = new PolynomialExpansion()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setDegree(degree);
        return polyExpansion.transform(dataset);
    }
}