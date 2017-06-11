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

import java.util.Date;
import java.util.Map;

public class ClassificationDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        SparkClassification sparkClassification = new SparkClassification();
        SparkUtil sparkUtil = new SparkUtil();
        JSONUtil jsonUtil = new JSONUtil();
        MongoUtil mongoUtil = new MongoUtil();
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
        if (train_size >= 1 || train_size <= 0)
            throw new Exception("train_size is not between 0 and 1!");
        Dataset<Row> dataset = sparkUtil.readData(dataPath, dataType, dataFormat, featureCols, labelCol);
        if (dataset == null) {
            logger.error("not such data for dataType " + dataType + "and dataPath :" + dataPath);
        }

        Dataset<Row>[] dataset_split = dataset.randomSplit(new double[]{train_size, 1 - train_size});
        Dataset<Row> dataset_train = dataset_split[0];
        Dataset<Row> dataset_test = dataset_split[1];

        Model model = null;
        if (type.equals(Classification.DT.toString())) {
            model = sparkClassification.dt(paramPair, dataset_train, savePath);
        } else if (type.equals(Classification.GBT.toString())) {
            model = sparkClassification.gbt(paramPair, dataset_train, savePath);
        } else if (type.equals(Classification.LR.toString())) {
            model = sparkClassification.lr(paramPair, dataset_train, savePath);
        } else if (type.equals(Classification.RF.toString())) {
            model = sparkClassification.rf(paramPair, dataset_train, savePath);
        } else if (type.equals(Classification.mutPerception.toString())) {
            model = sparkClassification.mutPerception(paramPair, dataset_train, savePath);
        } else if (type.equals(Classification.NB.toString())) {
            model = sparkClassification.nb(paramPair, dataset_train, savePath);
        } else {
            logger.error("No such model: " + type);
            return;
        }


        Map<String, Object> values = execution.getVariables();
        values.put("currentActivityId", execution.getCurrentActivityId());
        values.put("currentActivityName", execution.getCurrentActivityName());
        values.put("eventName", execution.getEventName());
        values.put("processBusinessKey", execution.getProcessBusinessKey());
        values.put("processInstanceId", execution.getProcessInstanceId());
        mongoUtil.insert_model_info(values);
        logger.info(new Date().toString() + "cluster : " +  type + "dataPath : " + dataPath + "done");
    }

}
