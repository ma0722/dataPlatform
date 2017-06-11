package feature.transformer;

import feature.Feature;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class VectorAssemble implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String outputCol = paramPair.getString("outputCol");
        VectorAssembler assembler = new VectorAssembler()
                .setInputCols(inputCol.split(","))
                .setOutputCol(outputCol);
        return assembler.transform(dataset);
    }
}
