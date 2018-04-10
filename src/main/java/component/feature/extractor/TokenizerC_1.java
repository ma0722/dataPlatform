package component.feature.extractor;


import component.Component;
import org.ansj.library.DicLibrary;
import org.ansj.library.StopLibrary;
import org.ansj.recognition.impl.StopRecognition;
import org.apache.spark.sql.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.HDFSFileUtil;
import util.SparkUtil;

import java.io.Serializable;



public class TokenizerC_1 extends Component implements Serializable{

    private StopRecognition filter = new StopRecognition();
    private String inputCol;
    private String outputCol;

    public void run(){
        try {
            Dataset dataset = inputs.get("data").getDataset();

            String path = "/tmp";
            dataset.show();
            while (HDFSFileUtil.checkFile(path))
                HDFSFileUtil.delFile(path, true);
//            dataset.coalesce(1).write().csv(HDFSFileUtil.HDFSPath(path));
            dataset.write().csv(HDFSFileUtil.HDFSPath(path));

            String[] columns = dataset.columns();
            HDFSTokenizer2 hdsft = new HDFSTokenizer2();
//            HDFSTokenizer hdsft = new HDFSTokenizer();

            int index = -1;
            for (int i = 0; i < columns.length; i++) {
                if (columns[i].equals(inputCol))
                    index = i;
            }
            if (index == -1) return;

            String output = null;
            for (String filePath: HDFSFileUtil.listPaths(path)) {
                System.out.println(filePath);
                if (!filePath.endsWith(".csv"))
                    continue;
                output = filePath.substring(0, filePath.length() - 4) + "_tokenizer" + ".csv";
                if (HDFSFileUtil.checkFile(output))
                    HDFSFileUtil.delFile(output, true);
                String[] args = {filePath, output, String.valueOf(index), String.valueOf(columns.length)};
                hdsft.submit(args);
                HDFSFileUtil.delFile(filePath, false);
            }

            Dataset tokenizerDataset = SparkUtil.spark.read().csv(output);

            tokenizerDataset.show();

            for (int i = 0; i < columns.length + 1; i++) {
                String colName = "_c" + String.valueOf(i);
                if (i == columns.length)
                    tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, outputCol);
                else {
                    tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, columns[i]);
                }
            }

            tokenizerDataset.show();
            if(outputs.containsKey("data"))
                outputs.get("data").setDataset(tokenizerDataset);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setParameters(JSONObject parameters) throws JSONException {

        if(parameters.has("dictPath") && !parameters.getJSONObject("dictPath").getString("value").equals(""))
            DicLibrary.put("dict", parameters.getJSONObject("dictPath").getString("value"));
        if(parameters.has("stopwordsPath") && !parameters.getJSONObject("stopwordsPath").getString("value").equals(""))
            StopLibrary.put("stop", parameters.getJSONObject("stopwordsPath").getString("value"));
        if(parameters.has("inputCol"))
            this.inputCol = parameters.getJSONObject("inputCol").getString("value");
        if(parameters.has("outputCol"))
            this.outputCol = parameters.getJSONObject("outputCol").getString("value");
    }


    @Test
    public void test() throws Exception {
        DataFrameReader sqlreader = SparkUtil.spark.read().format("jdbc") ;
        String url = String.format("jdbc:mysql://%s:%d/%s", "localhost", 3306, "shop");
        sqlreader.option("url",url);
        sqlreader.option("dbtable", "(SELECT shohin_bunrui from shohin1) as tmp");

        sqlreader.option("driver","com.mysql.jdbc.Driver");
        sqlreader.option("user", "root");
        sqlreader.option("password", "ma0722");
        Dataset<Row> dataset = sqlreader.load();
        dataset.show();

        inputCol = "shohin_bunrui";
        outputCol = "output";
        String path = "/tmp";

        if (HDFSFileUtil.checkFile(path))
            HDFSFileUtil.delFile(path, true);
        dataset.coalesce(1).write().csv(HDFSFileUtil.HDFSPath(path));

        String[] columns = dataset.columns();
        int index = -1;
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].equals(inputCol))
                index = i;
        }
        if (index == -1) return;
        HDFSTokenizer hdsft = new HDFSTokenizer();

        String output = null;
        for (String filePath: HDFSFileUtil.listPaths(path)) {
            System.out.println(filePath);
            if (!filePath.endsWith(".csv"))
                continue;
            output = filePath.substring(0, filePath.length() - 4) + "_tokenizer";
            if (HDFSFileUtil.checkFile(output))
                HDFSFileUtil.delFile(output, true);
            String[] args = {filePath, output, String.valueOf(index)};
            hdsft.submit(args);
            HDFSFileUtil.delFile(filePath, false);
        }

        Dataset tokenizerDataset = SparkUtil.spark.read().csv(output);
        tokenizerDataset.show();
        for (int i = 0; i < columns.length + 1; i++) {
            String colName = "_c" + String.valueOf(i);
            if (i == columns.length)
                tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, outputCol);
            else {
                tokenizerDataset = tokenizerDataset.withColumnRenamed(colName, columns[i]);
            }
        }
        tokenizerDataset.show();
    }
}
