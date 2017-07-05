package service.task;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import util.FactoryUtil;
import util.JSONUtil;
import util.MongoUtil;
import util.SparkUtil;


public class FeaureDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        FactoryUtil factoryUtil = FactoryUtil.getFactory();
        SparkUtil sparkUtil = factoryUtil.getSparkUtil();
        JSONUtil jsonUtil = factoryUtil.getJsonUtil();
        MongoUtil mongoUtil = factoryUtil.getMongoUtil();
        final String type = (String)execution.getVariable("type");
        final JSONObject paramPair = jsonUtil.jsonRead((String)execution.getVariable("paramPair"));
        final String dataPath = (String)execution.getVariable("dataPath");
        final String dataFormat = (String)execution.getVariable("dataFormat");
        final String dataType = (String)execution.getVariable("dataType");
        final String[] featureCols = ((String)execution.getVariable("featureCols")).split(",");
        final String labelCol = (String)execution.getVariable("labelCol");
        Dataset<Row> dataset = sparkUtil.readData(dataPath, dataType, dataFormat, featureCols, labelCol);
//        featureManager.run()
    }
}
