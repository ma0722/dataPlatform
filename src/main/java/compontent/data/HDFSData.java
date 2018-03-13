package compontent.data;

import compontent.Component;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import util.SparkUtil;


public class HDFSData extends Component {

    private String path;
    private String dataFormat;

    public void run() throws  Exception {
        Dataset dataset = SparkUtil.readFromHDFS(path, dataFormat);
        dataset.show();
        if(outputs.containsKey("data"))
            outputs.get("data").setDataset(dataset);
    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("dataFormat"))
            this.dataFormat = parameters.getJSONObject("dataFormat").getString("value");
        if(parameters.has("path"))
            this.path = parameters.getJSONObject("path").getString("value");
    }
    
    
    @Test
    public void test() throws Exception{
        this.path = "";
        this.dataFormat = "json";
        run();
    }

}
