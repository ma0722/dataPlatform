import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.FileInputStream;


public class Main {

    public static void main(String[] args) throws Exception{
        HDFSFileUtil hdfsFileUtil = new HDFSFileUtil();
        SparkUtil sparkUtil = new SparkUtil();
        Dataset<Row> dataset = sparkUtil.readData("data/data.json", "LOCAL");
    }

}
