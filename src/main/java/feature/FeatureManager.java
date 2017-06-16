package feature;

<<<<<<< HEAD
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
=======

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.util.ArrayList;
import java.util.List;

public class FeatureManager {

    private List<FeatureBase> featureList;

    public FeatureManager() {
        featureList = new ArrayList<FeatureBase>();
    }

    public FeatureManager(String[] featureNames) throws ClassNotFoundException {
        featureList = new ArrayList<FeatureBase>();
        for (String featureName : featureNames) {
            addFeature(featureName);
        }
    }

    public void addFeature(String featureName) {

    }

    public Dataset<Row> run(Dataset<Row> dataset) {
        Dataset<Row> data_temp = dataset;
        for (FeatureBase featureBase : featureList) {
//            data_temp = featureBase.run(data_temp, inputCol, paramPair);
        }
        return data_temp;
>>>>>>> 84701c67f5fb5746a7921d80c21f5d603bee6446
    }
}
