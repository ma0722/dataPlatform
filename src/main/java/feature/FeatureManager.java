package feature;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import feature.*;
import org.json.JSONObject;
import support.Feature;

import java.lang.reflect.Constructor;

public class FeatureManager {

    public Dataset<Row> run(Feature featureName, Dataset<Row> dataset, String inputCol, JSONObject paramPair) throws Exception{
        Class newoneClass = Class.forName(featureName.toString());
        Constructor cons = newoneClass.getConstructor();
        FeatureBase featureBase =  (FeatureBase)cons.newInstance();
        return featureBase.run(dataset, inputCol, paramPair);
    }
}
