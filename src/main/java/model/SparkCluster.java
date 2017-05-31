package model;

import org.apache.spark.ml.clustering.*;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;


public class SparkCluster {

    public Vector[] kmeans(ParamPair paramPair, Dataset dataSet) throws IOException {
        KMeans kmeans = new KMeans();
        KMeansModel kMeansModel = kmeans.fit(dataSet);
        Vector[] centers = kMeansModel.clusterCenters();
        return centers;
    }
    public Dataset<Row> lda(ParamPair paramPair, Dataset dataSet) throws IOException {
        LDA lda = new LDA();
        LDAModel ldaModel = lda.fit(dataSet);
        Dataset<Row> topics = ldaModel.describeTopics();
        return topics;
    }

    public Vector[] b_kmeans(ParamPair paramPair, Dataset dataSet) throws IOException {
        BisectingKMeans bisectingKMeans = new BisectingKMeans();
        BisectingKMeansModel bisectingKMeansModel = bisectingKMeans.fit(dataSet);
        Vector[] centers = bisectingKMeansModel.clusterCenters();
        return centers;
    }

    public GaussianMixtureModel gmm(ParamPair paramPair, Dataset dataSet) throws IOException {
        GaussianMixture gmm = new GaussianMixture();
        GaussianMixtureModel gaussianMixtureModel = gmm.fit(dataSet);
        return gaussianMixtureModel;
    }

}
