package component.data;

import component.Component;
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
        if(parameters.has("dataPath"))
            this.path = parameters.getJSONObject("dataPath").getString("value");
    }

    @Test
    public void test() throws Exception{
        this.path = "/tmp/weibo.csv";
        this.dataFormat = "csv";
        run();
    }

}
