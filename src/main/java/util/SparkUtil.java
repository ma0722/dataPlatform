package util;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import support.DataType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

public class SparkUtil {

    private static JavaSparkContext sc;
    private static SparkSession spark;
    private static HDFSFileUtil hdfsFileUtil;

    static {
        try{
            Properties properties = new Properties();
            properties.load(SparkUtil.class.getResourceAsStream("/cluster.properties"));
            String master = properties.getProperty("spark_master_ip");
            String port = properties.getProperty("spark_port");
            SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port);
            spark = SparkSession.builder().config(conf).getOrCreate();
            sc = new JavaSparkContext(conf);
            hdfsFileUtil = new HDFSFileUtil();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Dataset<Row> readFromHDFS(String path) throws Exception {
        if(!hdfsFileUtil.checkFile(path))
            throw new FileNotFoundException(path + "not found on hadoop!");
        return spark.read().load(hdfsFileUtil.HDFSPath(path));
    }

    public Dataset<Row> readFromLocal(String path) {
        return spark.read().load(path);
    }

    public Dataset<Row> readFromSQL(String sql) {
        return spark.sql(sql);
    }

    public Dataset<Row> readData(String dataPath, String dataType) throws Exception{
        if (dataType.equals(DataType.HDFS.toString())) {
            return readFromHDFS(dataPath);
        } else if (dataType.equals(DataType.LOCAL.toString())){
            return readFromLocal(dataPath);
        } else if (dataType.equals(DataType.SQL.toString())) {
            return readFromSQL(dataPath);
        } else {
            return null;
        }
    }
}
