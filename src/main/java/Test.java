import unused.HDFSTokenizer;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.ml.feature.CountVectorizer;
import org.apache.spark.ml.feature.CountVectorizerModel;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.*;

public class Test implements Serializable{

    @org.junit.Test
    public void test() {

        Tokenizer tokenizer = new Tokenizer().setOutputCol("temp");
        CountVectorizer countVectorizer = new CountVectorizer().setVocabSize(3).setMinDF(2).setInputCol("temp");
        CountVectorizerModel model;

        DataFrameReader reader = SparkUtil.spark.read().format("jdbc");
        String url = String.format("jdbc:mysql://%s:%d/%s", "localhost", 3306, "shop");
        reader.option("url", url);
        reader.option("dbtable", "(SELECT shohin_bunrui from shohin1) as tmp");

        reader.option("driver", "com.mysql.jdbc.Driver");
        reader.option("user", "root");
        reader.option("password", "ma0722");

        Dataset<Row> dataset = reader.load();
        dataset.show();

        dataset.registerTempTable("tmp");

        UDF1 udf = new UDF1<String, String>() {
            public String call(String s) {
                Result result = ToAnalysis.parse(s);
                StringBuffer sb = new StringBuffer();
                for (Term term : result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        };

        SparkUtil.spark.udf().register("t", udf, DataTypes.StringType);


        Dataset dd = SparkUtil.spark.sql("select shohin_bunrui, scala(shohin_bunrui) as v from tmp");
//        dd.show();
        dd.collect();
    }

    @org.junit.Test
    public void test2() {

        Tokenizer tokenizer = new Tokenizer().setOutputCol("temp");
        CountVectorizer countVectorizer = new CountVectorizer().setVocabSize(3).setMinDF(2).setInputCol("temp");
        CountVectorizerModel model;

        DataFrameReader reader = SparkUtil.spark.read().format("jdbc") ;

        String url = String.format("jdbc:mysql://%s:%d/%s", "10.109.247.63", 3306, "db_weibo");
        reader.option("url",url);
        reader.option("dbtable", "(SELECT weibo_content from weibo_original limit 30) as tmp");
        reader.option("driver","com.mysql.jdbc.Driver");
        reader.option("user", "root");
        reader.option("password", "hadoop");

        Dataset<Row> dataset = reader.load();
        dataset.show();

        dataset.registerTempTable("tmp");

        UDF1 udf = new UDF1<String, String>() {
            public String call(String s) {
                Result result = ToAnalysis.parse(s);
                StringBuffer sb = new StringBuffer();
                for (Term term : result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        };

        SparkUtil.spark.udf().register("t", udf, DataTypes.StringType);


        Dataset dd = SparkUtil.spark.sql("select weibo_content, t(weibo_content) as v from tmp");
        dd.collect();
    }

    @org.junit.Test
    public void test4() throws Exception{

        String path = "/tmp8";
        Dataset dataset = SparkUtil.readFromHDFS("/weibo.csv", "csv");
        dataset.show();
        if (HDFSFileUtil.checkFile(path))
            HDFSFileUtil.delFile(path, true);
        dataset.coalesce(1).write().csv(HDFSFileUtil.HDFSPath(path));

        String[] columns = dataset.columns();
        HDFSTokenizer hdsft = new HDFSTokenizer();

        for (String filePath: HDFSFileUtil.listPaths(path)) {
            System.out.println(filePath);
            if (!filePath.endsWith(".csv"))
                continue;
            String output = filePath.substring(0, filePath.length() - 4) + "_tokenizer";
            if (HDFSFileUtil.checkFile(output))
                HDFSFileUtil.delFile(output, true);
            String[] args = {filePath, output};
            hdsft.submit(args);
            HDFSFileUtil.delFile(filePath, false);
        }

        Dataset tokenizerDataset = SparkUtil.spark.read().csv(HDFSFileUtil.HDFSPath(path));
        tokenizerDataset.show();
        for (int i = 0; i < columns.length + 1; i++) {
            String colName = "_c" + String.valueOf(i);
            if (i == columns.length)
                tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, "tokenizer");
            else {
                tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, columns[i]);
            }
        }
        tokenizerDataset.show();
    }

    @org.junit.Test
    public void t(){
        System.out.println("f49022a55a0.csv".endsWith(".csv"));
        for (String str: "part-r-00000-0568cb7a-4da5-4bb2-b781-8f49022a55a0.csv".split("[.]"))
            System.out.println("=====" + str);
        System.out.println("part-r-00000-0568cb7a-4da5-4bb2-b781-8f49022a55a0.csv".split("[.]")[0]);
    }



    public static void main(String[] args) throws Exception {
        String result = execCmd("java -version", null);
        System.out.println(result);
    }

    /**
     * 执行系统命令, 返回执行结果
     *
     * @param cmd 需要执行的命令
     * @param dir 执行命令的子进程的工作目录, null 表示和当前主进程工作目录相同
     */
    public static String execCmd(String cmd, File dir) throws Exception {
        StringBuilder result = new StringBuilder();

        Process process = null;
        BufferedReader bufrIn = null;
        BufferedReader bufrError = null;

        try {
            // 执行命令, 返回一个子进程对象（命令在子进程中执行）
            process = Runtime.getRuntime().exec(cmd, null, dir);

            // 方法阻塞, 等待命令执行完成（成功会返回0）
            process.waitFor();

            // 获取命令执行结果, 有两个结果: 正常的输出 和 错误的输出（PS: 子进程的输出就是主进程的输入）
            bufrIn = new BufferedReader(new InputStreamReader(process.getInputStream(), "UTF-8"));
            bufrError = new BufferedReader(new InputStreamReader(process.getErrorStream(), "UTF-8"));

            // 读取输出
            String line = null;
            while ((line = bufrIn.readLine()) != null) {
                result.append(line).append('\n');
            }
            while ((line = bufrError.readLine()) != null) {
                result.append(line).append('\n');
            }

        } finally {
            closeStream(bufrIn);
            closeStream(bufrError);

            // 销毁子进程
            if (process != null) {
                process.destroy();
            }
        }

        // 返回执行结果
        return result.toString();
    }

    private static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception e) {
                // nothing
            }
        }
    }

}
