package service.task;

import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.sql.Row;
import org.json.JSONObject;
import support.Cluster;
import model.SparkCluster;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.apache.spark.sql.Dataset;
import util.JSONUtil;
import util.MongoUtil;
import util.SparkUtil;
import util.UtilManager;

import java.util.Date;
import java.util.Map;

public class ClusterDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        SparkCluster sparkCluster = new SparkCluster();
        UtilManager utilManager = UtilManager.getUtilManager();
        SparkUtil sparkUtil = utilManager.getSparkUtil();
        JSONUtil jsonUtil = utilManager.getJsonUtil();
        MongoUtil mongoUtil = utilManager.getMongoUtil();
        Logger logger = Logger.getLogger(ClusterDelegate.class);
        logger.info(new Date().toString() + "activiti id: " + execution.getCurrentActivityId() + "actiiviti name" + execution.getCurrentActivityName());
        final String type = (String)execution.getVariable("type");
        final JSONObject paramPair = jsonUtil.jsonRead((String)execution.getVariable("paramPair"));
        final String dataPath = (String)execution.getVariable("dataPath");
        final String dataFormat = (String)execution.getVariable("dataFormat");
        final String dataType = (String)execution.getVariable("dataType");
        final String[] featureCols = ((String)execution.getVariable("featureCols")).split(",");
        final String labelCol = (String)execution.getVariable("labelCol");
        Dataset<Row> dataset = sparkUtil.readData(dataPath, dataType, dataFormat, featureCols, labelCol);
        if (dataset == null) {
            logger.error("not such data for dataType " + dataType + "and dataPath :" + dataPath);
        }
        if (type.equals(Cluster.B_KMEANS.toString())) {
            Vector[] vectors = sparkCluster.b_kmeans(paramPair, dataset);
            for(Vector vector : vectors) {
                System.out.println(vector.toString());
            }
        } else if (type.equals(Cluster.GMM.toString())) {
            sparkCluster.gmm(paramPair, dataset);
        } else if (type.equals(Cluster.LDA.toString())) {
            sparkCluster.lda(paramPair, dataset);
        } else if (type.equals(Cluster.KMEANS.toString())) {
            sparkCluster.kmeans(paramPair, dataset);
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
