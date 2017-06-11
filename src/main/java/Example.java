
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.spark.SparkConf;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import util.SparkUtil;


public class Example {


    public static void main(String[] args) throws Exception{

        Properties properties = new Properties();
        properties.load(Example.class.getResourceAsStream("/cluster.properties"));
        String master = properties.getProperty("spark_master_ip");
        String port = properties.getProperty("spark_port");
        SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port);
        SparkSession spark = SparkSession.builder().config(conf).getOrCreate();

        List<Row> data = Arrays.asList(
                RowFactory.create(Arrays.asList("Hi I heard about Spark".split(" "))),
                RowFactory.create(Arrays.asList("I wish Java could use case classes".split(" "))),
                RowFactory.create(Arrays.asList("Logistic regression models are neat".split(" ")))
        );
        StructType schema = new StructType(new StructField[]{
                new StructField("text", new ArrayType(DataTypes.StringType, true), false, Metadata.empty())
        });
        Dataset<Row> documentDF = spark.createDataFrame(data, schema);

        Word2Vec word2Vec = new Word2Vec()
                .setInputCol("text")
                .setOutputCol("result")
                .setVectorSize(3)
                .setMinCount(0);

        Word2VecModel model = word2Vec.fit(documentDF);
        Dataset<Row> result = model.transform(documentDF);
        result.show();

    }
}
