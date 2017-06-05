package service.task;

import model.SparkClassification;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import support.Classification;
import util.SparkUtil;

import java.util.Date;

public class ClassificationDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        SparkClassification sparkClassification = new SparkClassification();
        SparkUtil sparkUtil = new SparkUtil();
        Logger logger = Logger.getLogger(ClassificationDelegate.class);
        logger.info(new Date().toString() + "activiti id: " + execution.getCurrentActivityId() + "actiiviti name" + execution.getCurrentActivityName());
        final String type = (String)execution.getVariable("type");
        final ParamPair paramPair = (ParamPair) execution.getVariable("paramParr");
        final String dataPath = (String)execution.getVariable("dataPath");
        final String dataType = (String)execution.getVariable("dataType");
        final String savePath = (String)execution.getVariable("savePath");
        Dataset<Row> dataset = sparkUtil.readData(dataPath, dataType);
        if (dataset == null) {
            logger.error("not such data for dataType " + dataType + "and dataPath :" + dataPath);
        }
        if (type.equals(Classification.DT.toString())) {
            sparkClassification.dt(paramPair, dataset, savePath);
        } else if (type.equals(Classification.GBT.toString())) {
            sparkClassification.gbt(paramPair, dataset, savePath);
        } else if (type.equals(Classification.LR.toString())) {
            sparkClassification.dt(paramPair, dataset, savePath);
        } else if (type.equals(Classification.RF.toString())) {
            sparkClassification.rf(paramPair, dataset, savePath);
        } else if (type.equals(Classification.mutPerception.toString())) {
            sparkClassification.mutPerception(paramPair, dataset, savePath);
        } else if (type.equals(Classification.NB.toString())) {
            sparkClassification.nb(paramPair, dataset, savePath);
        } else {
            logger.error("No such model: " + type);
            return;
        }
        logger.info(new Date().toString() + "cluster : " +  type + "dataPath : " + dataPath + "done");
    }

}
