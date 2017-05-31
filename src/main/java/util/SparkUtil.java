package util;


import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class SparkUtil {

    private static JavaSparkContext sc;
    private static HDFSFileUtil hdfsFileUtil;
    static {
        try{
            Properties properties = new Properties();
            properties.load(new FileInputStream("cluster.properties"));
            String master = properties.getProperty("spark_master_ip");
            String port = properties.getProperty("spark_port");
            SparkConf conf = new SparkConf().setAppName("data-platform").setMaster("spark://" + master + ":" + port);
            sc = new JavaSparkContext(conf);
            hdfsFileUtil = new HDFSFileUtil();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JavaRDD readFileFromHDFS(String path) throws Exception {
        if(!hdfsFileUtil.checkFile(path))
            throw new FileNotFoundException(path + "not found on hadoop!");
        return sc.textFile(hdfsFileUtil.HDFSPath(path));
    }


}
