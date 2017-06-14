package feature.transformer;

import feature.FeatureBase;
import org.apache.spark.ml.feature.VectorIndexer;
import org.apache.spark.ml.feature.VectorIndexerModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class VectorIndex implements FeatureBase {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int maxCategories = paramPair.getInt("maxCategories");
        VectorIndexer indexer = new VectorIndexer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setMaxCategories(maxCategories);
        VectorIndexerModel indexerModel = indexer.fit(dataset);
        return indexerModel.transform(dataset);
    }
}
