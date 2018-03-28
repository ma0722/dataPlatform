package component.feature.extractor;

import component.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.IDFModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.json.JSONObject;
import org.junit.Test;

public class TF_IDF extends Component {


    private Tokenizer tokenizer;
    private HashingTF hashingTF;
    private IDF idf;

    public TF_IDF() {
        tokenizer = new Tokenizer();
        hashingTF = new HashingTF();
        idf = new IDF();
    }

    public void run() throws Exception {
        tokenizer.setOutputCol("tokenizer_output");
        Dataset dataset = inputs.get("data").getDataset();
        Dataset wordsData = tokenizer.transform(dataset);
        hashingTF.setInputCol("tokenizer_output").setOutputCol("raw_features_TF_IDF");
        Dataset featurizedData = hashingTF.transform(wordsData);
        idf.setInputCol("raw_features_TF_IDF");
        IDFModel idfModel = idf.fit(featurizedData);
        Dataset dataset1 = idfModel.transform(featurizedData);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(dataset1);
        if(outputs.containsKey("model"))
            outputs.get("model").setModel(idfModel);
    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("inputCol"))
            tokenizer.setInputCol(parameters.getJSONObject("inputCol").getString("value"));
        if(parameters.has("outputCol"))
            idf.setOutputCol(parameters.getJSONObject("outputCol").getString("value"));
        if(parameters.has("numFeatures"))
            hashingTF.setNumFeatures(parameters.getJSONObject("numFeatures").getInt("value"));
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
        hashingTF.setInputCol("new_value").setOutputCol("raw_features_TF_IDF");
        Dataset featurizedData = hashingTF.transform(wordsData);
        featurizedData.show();
    }
}
