package model;

import org.apache.spark.ml.classification.*;
import org.apache.spark.ml.param.Param;
import org.apache.spark.ml.param.ParamPair;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class SparkClassification {

    public void lr(JSONObject paramPair, Dataset dataSet, String savePath) throws IOException, JSONException {
        LogisticRegression lr = new LogisticRegression()
                .setMaxIter(paramPair.getInt("maxIter"))
                .setRegParam(paramPair.getDouble("regParam"))
                .setElasticNetParam(paramPair.getDouble("elasticNetParam"))
                .setStandardization(paramPair.getBoolean("standardization"));
        LogisticRegressionModel model_lr= lr.fit(dataSet);
        model_lr.save(savePath);
    }


    public void dt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException{
        DecisionTreeClassifier dt = new DecisionTreeClassifier();
        DecisionTreeClassificationModel model_dt = dt.fit(dataSet);
        model_dt.save(savePath);
    }

    public void rf(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException{
        RandomForestClassifier rf = new RandomForestClassifier();
        RandomForestClassificationModel model_rf = rf.fit(dataSet);
        model_rf.save(savePath);
    }

    public void gbt(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException{
        GBTClassifier gbt = new GBTClassifier();
        GBTClassificationModel model_gbt = gbt.fit(dataSet);
        model_gbt.save(savePath);
    }

    public void mutPerception(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        MultilayerPerceptronClassifier mutPer = new MultilayerPerceptronClassifier();
        MultilayerPerceptronClassificationModel model_mutPer = mutPer.fit(dataSet);
        model_mutPer.save(savePath);
    }

    public void nb(JSONObject paramPair, Dataset<Row> dataSet, String savePath) throws IOException, JSONException {
        NaiveBayes nb = new NaiveBayes();
        NaiveBayesModel model_nb = nb.fit(dataSet);
        model_nb.save(savePath);
    }

}
