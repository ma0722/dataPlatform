package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class PCA_ implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int k = paramPair.getInt("k");
        PCAModel pca = new PCA()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setK(k)
                .fit(dataset);

         return pca.transform(dataset);
    }
}
