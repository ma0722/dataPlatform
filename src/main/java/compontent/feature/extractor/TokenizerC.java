package compontent.feature.extractor;


import compontent.Component;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.recognition.impl.FilterRecognition;
import org.ansj.splitWord.analysis.ToAnalysis;
//import org.ansj.splitWord.analysis.BaseAnalysis;
//import org.ansj.splitWord.analysis.NlpAnalysis;
import org.ansj.library.UserDefineLibrary;
import org.apache.spark.api.java.function.MapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.spark.sql.Encoder;


public class TokenizerC extends Component{

    private FilterRecognition filter = new FilterRecognition();
    private String inputCol;

    public void run(){

        Dataset dataset = inputs.get("data").getDataset();
        Encoder<String> encoder = Encoders.STRING();
        Dataset data = dataset.map(new MapFunction <Row, String>() {
            public String call(Row row) {
                int index = row.fieldIndex(inputCol);
                String s = (String)row.get(index);
                Result result = ToAnalysis.parse(s).recognition(filter);
                StringBuffer sb = new StringBuffer();
                for (Term term: result.getTerms()) {
                    sb.append(term.getName());
                    sb.append(" ");
                }
                return sb.toString();
            }
        }, encoder);
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(data);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("dictPath"))
            UserDefineLibrary.loadLibrary(UserDefineLibrary.FOREST, parameters.getJSONObject("dictPath").getString("value"));
        if(parameters.has("stopwordsPath"))
            UserDefineLibrary.loadLibrary(UserDefineLibrary.FOREST, parameters.getJSONObject("dicPath").getString("value"));
        if(parameters.has("inputCol"))
            this.inputCol = parameters.getJSONObject("inputCol").getString("value");
    }

}
