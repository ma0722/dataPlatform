package feature.extractor;

import feature.FeatureBase;
import org.apache.spark.ml.feature.Word2Vec;
import org.apache.spark.ml.feature.Word2VecModel;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class WordToVec implements FeatureBase {

    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final int vectorSize = paramPair.getInt("vectorSize");
        final int minCount = paramPair.getInt("minCount");
        final int windowSize = paramPair.getInt("windowSize");
        Word2Vec word2Vec = new Word2Vec()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName())
                .setVectorSize(vectorSize)
                .setMinCount(minCount)
                .setWindowSize(windowSize);

        Word2VecModel model = word2Vec.fit(dataset);
        return model.transform(dataset);
    }
}
