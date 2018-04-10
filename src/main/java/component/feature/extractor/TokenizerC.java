package component.feature.extractor;


import component.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.library.*;
import org.ansj.recognition.impl.StopRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
import org.apache.spark.sql.*;
import org.apache.spark.sql.api.java.UDF1;
import org.apache.spark.sql.types.DataTypes;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.SparkUtil;




public class TokenizerC extends Component {

    private StopRecognition filter = new StopRecognition();
    private String inputCol;
    private String outputCol;

    public void run(){

        Dataset dataset = inputs.get("data").getDataset();
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


        StringBuffer sbSQL = new StringBuffer();
        sbSQL.append("select ");
        for (String col: dataset.columns()) {
            sbSQL.append(col);
            sbSQL.append(", ");
        }
        sbSQL.append("t(");
        sbSQL.append(this.inputCol);
        sbSQL.append(") as ");
        sbSQL.append(this.outputCol);
        sbSQL.append(" from tmp");
        System.out.println(sbSQL.toString());

        Dataset newDataset = SparkUtil.spark.sql(sbSQL.toString());

        newDataset.show();
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(newDataset);
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

}
