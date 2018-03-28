package component.data;

import component.Component;
import org.apache.spark.sql.Dataset;
import org.json.JSONException;
import org.json.JSONObject;

public class DataSplit extends Component {

    private double trainingDataSize;

    public void run() throws  Exception {
        Dataset dataset = inputs.get("data").getDataset();
        Dataset[] splitDatas = dataset.randomSplit(new double[]{trainingDataSize, 1 - trainingDataSize});
        if(outputs.containsKey("trainingData"))
            outputs.get("data").setDataset(splitDatas[0]);
        if(outputs.containsKey("testData"))
            outputs.get("testData").setDataset(splitDatas[1]);

    }

    public void setParameters(JSONObject parameters) throws JSONException {
        if(parameters.has("trainingDataSize"))
            this.trainingDataSize = parameters.getJSONObject("trainingDataSize").getDouble("value");

    }

}
