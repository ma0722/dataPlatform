package feature;




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

    }
}
