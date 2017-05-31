package model;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.SparkConf;
import org.apache.spark.ml.classification.*;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.ml.classification.LogisticRegression;

class SparkClassification {

    public void lr(ParamPair paramPair, Dataset dataSet, String savePath) throws IOException{
        LogisticRegression lr = new LogisticRegression();
        LogisticRegressionModel model_lr= lr.fit(dataSet);
        model_lr.save(savePath);
    }


    public void dt(Dataset<Row> dataSet, String savePath) throws IOException{
        DecisionTreeClassifier dt = new DecisionTreeClassifier();
        DecisionTreeClassificationModel model_dt = dt.fit(dataSet);
        model_dt.save(savePath);
    }

    public void rf(ParamPair[] paramPair, Dataset<Row> dataSet, String savePath) throws IOException{
        RandomForestClassifier rf = new RandomForestClassifier();
        RandomForestClassificationModel model_rf = rf.fit(dataSet);
        model_rf.save(savePath);
    }

    public void gbt(Dataset<Row> dataSet, String savePath) throws IOException{
        GBTClassifier gbt = new GBTClassifier();
        GBTClassificationModel model_gbt = gbt.fit(dataSet);
        model_gbt.save(savePath);
    }

    public void mutPerception(Dataset<Row> dataSet, String savePath) throws IOException {
        MultilayerPerceptronClassifier mutPer = new MultilayerPerceptronClassifier();
        MultilayerPerceptronClassificationModel model_mutPer = mutPer.fit(dataSet);
        model_mutPer.save(savePath);
    }

    public void nb(ParamPair<Param> paramPair, Dataset<Row> dataSet, String savePath) throws IOException {
        NaiveBayes nb = new NaiveBayes();
        NaiveBayesModel model_nb = nb.fit(dataSet);
        model_nb.save(savePath);
    }

}