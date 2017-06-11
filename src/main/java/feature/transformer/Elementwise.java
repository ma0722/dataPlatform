package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.ElementwiseProduct;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;


public class Elementwise implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String[] weight_string = paramPair.getString("splits").split(",");
        double[] weight = new double[weight_string.length];
        for(int i = 0; i < weight_string.length; i++){
            weight[i] = Double.valueOf(weight_string[i]);
        }
        Vector transformingVector = Vectors.dense(weight);

        ElementwiseProduct transformer = new ElementwiseProduct()
                .setScalingVec(transformingVector)
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        return transformer.transform(dataset);
    }
}