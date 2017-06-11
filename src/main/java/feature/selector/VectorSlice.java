package feature.selector;

import feature.Feature;
import org.apache.spark.ml.feature.VectorSlicer;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;

public class VectorSlice implements Feature {
    public Dataset<Row> run(Dataset dataset, String inputCol, JSONObject paramPair) throws JSONException {
        final String[] names = paramPair.getString("names").split(",");
        final String[] index_string = paramPair.getString("indexs").split(",");
        int[] indexs = new int[index_string.length];
        for(int i = 0; i < index_string.length; i++){
            indexs[i] = Integer.valueOf(index_string[i]);
        }
        VectorSlicer vectorSlicer = new VectorSlicer()
                .setInputCol(inputCol)
                .setOutputCol(inputCol + this.getClass().getName());
        vectorSlicer.setIndices(indexs).setNames(names);
        return vectorSlicer.transform(dataset);
    }
}
