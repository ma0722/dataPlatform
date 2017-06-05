package service.task;

import support.Cluster;
import model.SparkCluster;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.apache.log4j.Logger;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import util.SparkUtil;

import java.util.Date;

public class ClusterDelegate implements JavaDelegate {

    public void execute(DelegateExecution execution) throws Exception {
        SparkCluster sparkCluster = new SparkCluster();
        SparkUtil sparkUtil = new SparkUtil();
        Logger logger = Logger.getLogger(ClusterDelegate.class);
        logger.info(new Date().toString() + "activiti id: " + execution.getCurrentActivityId() + "actiiviti name" + execution.getCurrentActivityName());
        final String type = (String)execution.getVariable("type");
        final ParamPair paramPair = (ParamPair) execution.getVariable("paramParr");
        final String dataPath = (String)execution.getVariable("dataPath");
        final String dataType = (String)execution.getVariable("dataType");
        Dataset dataset = sparkUtil.readData(dataPath, dataType);
        if (dataset == null) {
            logger.error("not such data for dataType " + dataType + "and dataPath :" + dataPath);
        }
        if (type.equals(Cluster.B_KMEANS.toString())) {
            sparkCluster.b_kmeans(paramPair, dataset);
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
        logger.info(new Date().toString() + "cluster : " +  type + "dataPath : " + dataPath + "done");
    }

}
