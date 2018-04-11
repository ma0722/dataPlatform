package component.feature.extractor;

import component.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.apache.spark.ml.clustering.KMeans;
import org.apache.spark.ml.clustering.KMeansModel;
import org.apache.spark.ml.feature.CountVectorizer;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.SparkUtil;

public class Bag0fWordsC extends Component{

    private Tokenizer tokenizer = new Tokenizer().setOutputCol("temp");
    private CountVectorizer countVectorizer = new CountVectorizer().setVocabSize(3).setMinDF(2).setInputCol("temp");
    private CountVectorizerModel model;

    private String inputCol;
    private String outputCol;
    public void run(){

        Dataset dataset = inputs.get("data").getDataset();
        dataset = dataset.filter(dataset.col(inputCol).isNotNull());
        Dataset wordsData = tokenizer.transform(dataset);
        wordsData.show();
        model = countVectorizer.fit(wordsData);
        Dataset newDataset = model.transform(wordsData);
        newDataset.show();
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(newDataset);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("inputCol")) {
            inputCol = parameters.getJSONObject("inputCol").getString("value");
            tokenizer.setInputCol(inputCol);
        }
        if(parameters.has("outputCol")) {
            outputCol = parameters.getJSONObject("outputCol").getString("value");
            countVectorizer.setOutputCol(outputCol);
        }
    }


    @Test
    public void test2() throws Exception {
        Dataset dataset = SparkUtil.readFromHDFS("/tmp/part-r-00000-4d9f7252-d0c5-4097-aa4d-3a5ba51d166d_tokenizer.csv", "csv");
        dataset.show();
        inputCol = "画师 tofuvi 的 一组 清新 系列 图 ~ 淡淡 而 清新 的 颜色 [ 心 ] id = 7377710   _";
//        inputCol = "value";
        dataset = dataset.filter(dataset.col(inputCol).isNotNull());
        dataset.show();
        Dataset wordsData = tokenizer.transform(dataset);
        wordsData.show();
        model = countVectorizer.fit(wordsData);
        Dataset newDataset = model.transform(wordsData);
        newDataset.show();
    }

    @Test
    public void test() throws Exception{

        DataFrameReader reader = SparkUtil.spark.read().format("jdbc") ;

        String url = String.format("jdbc:mysql://%s:%d/%s", "10.109.247.63", 3306, "db_weibo");
        reader.option("url",url);
        reader.option("dbtable", "(SELECT weibo_content from weibo_original limit 30) as tmp");
        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user", "root");
        reader.option("password", "hadoop");

        Dataset<Row> dataset = reader.load();
        dataset.show();

        Encoder<String> encoder = Encoders.STRING();

        Dataset data = dataset.map(new MapFunction <Row, String>() {
            public String call(Row row) {
                int index = row.fieldIndex("weibo_content");
                String s = (String)row.get(index);
                Result result = ToAnalysis.parse(s);
                StringBuffer sb = new StringBuffer();
                for (Term term: result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        }, encoder);

        data.show();

        tokenizer.setInputCol("value").setOutputCol("temp");
        Dataset wordsData = tokenizer.transform(data);
        wordsData.show();

        model = new CountVectorizer()
                .setInputCol("temp")
                .setOutputCol("features")
//                .setVocabSize(3)
//                .setMinDF(2)
                .fit(wordsData);
        model.transform(wordsData).show();


        KMeans kmeans = new KMeans();
        kmeans.setFeaturesCol("features");
        Dataset tmp = model.transform(wordsData);
        KMeansModel kMeansModel = kmeans.fit(tmp);
        Dataset result = kMeansModel.transform(tmp);
        result.show();
        result.write().save("/Users/MaChao/Desktop/result");

    }

}
