package component.model.estimate;

import component.Component;
import org.apache.spark.ml.Model;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.sql.Dataset;
import org.json.JSONObject;


public class MutClassificationEvaluatorC extends Component {

    private MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator();
    public void run() throws Exception {
        Model model = inputs.get("model").getModel();
        Dataset dataset = inputs.get("data").getDataset();
        double value = evaluator.evaluate(dataset);
        if(outputs.containsKey("value"))
            outputs.get("value").getOthers().put(evaluator.getMetricName(), value);

    }

    public void setParameters(JSONObject parameters) throws Exception {
        if(parameters.has("metricName"))
            evaluator.setMetricName(parameters.getJSONObject("metricName").getString("value"));
    }
}
