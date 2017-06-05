package model;

import org.apache.spark.ml.classification.*;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

import java.io.IOException;

public class SparkClassification {

    public void lr(ParamPair paramPair, Dataset dataSet, String savePath) throws IOException{
        LogisticRegression lr = new LogisticRegression();
        LogisticRegressionModel model_lr= lr.fit(dataSet);
        model_lr.save(savePath);
    }


    public void dt(ParamPair paramPair, Dataset<Row> dataSet, String savePath) throws IOException{
        DecisionTreeClassifier dt = new DecisionTreeClassifier();
        DecisionTreeClassificationModel model_dt = dt.fit(dataSet);
        model_dt.save(savePath);
    }

    public void rf(ParamPair paramPair, Dataset<Row> dataSet, String savePath) throws IOException{
        RandomForestClassifier rf = new RandomForestClassifier();
        RandomForestClassificationModel model_rf = rf.fit(dataSet);
        model_rf.save(savePath);
    }

    public void gbt(ParamPair paramPair, Dataset<Row> dataSet, String savePath) throws IOException{
        GBTClassifier gbt = new GBTClassifier();
        GBTClassificationModel model_gbt = gbt.fit(dataSet);
        model_gbt.save(savePath);
    }

    public void mutPerception(ParamPair paramPair, Dataset<Row> dataSet, String savePath) throws IOException {
        MultilayerPerceptronClassifier mutPer = new MultilayerPerceptronClassifier();
        MultilayerPerceptronClassificationModel model_mutPer = mutPer.fit(dataSet);
        model_mutPer.save(savePath);
    }

    public void nb(ParamPair paramPair, Dataset<Row> dataSet, String savePath) throws IOException {
        NaiveBayes nb = new NaiveBayes();
        NaiveBayesModel model_nb = nb.fit(dataSet);
        model_nb.save(savePath);
    }

}
