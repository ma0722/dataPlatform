package service.task;

import model.SparkClassification;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.apache.spark.ml.Model;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import support.Classification;
import util.JSONUtil;
import util.MongoUtil;
import util.SparkUtil;
import util.UtilManager;

import java.util.Date;
import java.util.Map;

public class ClassificationDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        SparkClassification sparkClassification = new SparkClassification();
        UtilManager utilManager = UtilManager.getUtilManager();
        SparkUtil sparkUtil = utilManager.getSparkUtil();
        JSONUtil jsonUtil = utilManager.getJsonUtil();
        MongoUtil mongoUtil = utilManager.getMongoUtil();
        Logger logger = Logger.getLogger(ClassificationDelegate.class);
        logger.info(new Date().toString() + "activiti id: " + execution.getCurrentActivityId() + "actiiviti name" + execution.getCurrentActivityName());

        final String type = (String)execution.getVariable("type");
        final JSONObject paramPair = jsonUtil.jsonRead((String)execution.getVariable("paramPair"));
        final String dataPath = (String)execution.getVariable("dataPath");
        final String dataType = (String)execution.getVariable("dataType");
        final String dataFormat = (String)execution.getVariable("dataFormat");
        final String savePath = (String)execution.getVariable("savePath");
        final String[] featureCols = ((String)execution.getVariable("featureCols")).split(",");
        final String labelCol = (String)execution.getVariable("labelCol");
        final double train_size = Double.parseDouble((String)execution.getVariable("train_size"));

        Dataset<Row> dataset = sparkUtil.readData(dataPath, dataType, dataFormat, featureCols, labelCol);
        if (dataset == null) {
            logger.error("not such data for dataType " + dataType + "and dataPath :" + dataPath);
        }

    }

}
