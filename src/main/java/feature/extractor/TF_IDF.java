package feature.extractor;

import com.mongodb.util.JSON;
import feature.Feature;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class TF_IDF implements Feature {

    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException{

        Tokenizer tokenizer = new Tokenizer().setInputCol(inputCol).setOutputCol(inputCol + "_tokenizer");
        Dataset<Row> wordsData = tokenizer.transform(dataset);

        final int numFeatures = paramPair.getInt("numFeatures");
        HashingTF hashingTF = new HashingTF()
                .setInputCol(inputCol + "_tokenizer")
                .setOutputCol("raw_features_TF_IDF")
                .setNumFeatures(numFeatures);
        Dataset<Row> featurizedData = hashingTF.transform(wordsData);
        IDF idf = new IDF().setInputCol("raw_features_TF_IDF").setOutputCol(inputCol + TF_IDF.class.getName());
        IDFModel idfModel = idf.fit(featurizedData);
        return idfModel.transform(featurizedData);
    }
}
