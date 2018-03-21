package compontent.feature.extractor;

import compontent.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.apache.spark.ml.feature.CountVectorizer;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;

public class CounterVector extends Component{

    private Tokenizer tokenizer = new Tokenizer();
    private CountVectorizerModel model;
    private String[] vocabulary;

    public void run(){
        Dataset dataset = inputs.get("data").getDataset();
        if (model == null)
            model = new CountVectorizer()
                    .setInputCol("text")
                    .setOutputCol("feature")
                    .setVocabSize(3)
                    .setMinDF(2)
                    .fit(dataset);
        Dataset data = model.transform(dataset);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("vocabulary")) {
            vocabulary = parameters.getJSONObject("vocabulary").getString("value").split(" ");
            model = new CountVectorizerModel(vocabulary);
        }
        if(parameters.has("inputCol"))
            model.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            model.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
    }


    @Test
    public void test() throws Exception{

        SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("local");

        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        DataFrameReader reader = spark.read().format("jdbc") ;
        String url = String.format("jdbc:mysql://%s:%d/%s", "localhost", 3306, "shop");
        reader.option("url",url);
        reader.option("dbtable", "(SELECT shohin_bunrui from shohin1) as tmp");

        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user", "root");
        reader.option("password", "ma0722");
        Dataset<Row> dataset = reader.load();
        dataset.show();

        Encoder<String> encoder = Encoders.STRING();

        Dataset data = dataset.map(new MapFunction<Row, String>() {
            public String call(Row row) {
                int index = row.fieldIndex("shohin_bunrui");
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

        tokenizer.setInputCol("value").setOutputCol("new_value");
        Dataset wordsData = tokenizer.transform(data);
        wordsData.show();
        model = new CountVectorizer()
                .setInputCol("new_value")
                .setOutputCol("features")
//                .setVocabSize(3)
//                .setMinDF(2)
                .fit(wordsData);
        model.transform(wordsData).show();
    }

}
