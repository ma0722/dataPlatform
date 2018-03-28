import Base.Graph;
import org.json.JSONObject;
import util.JSONUtil;



public class Main {

    public static void main(String[] args) throws Exception {

        JSONObject componets = JSONUtil.jsonRead("src/main/resources/a.json");
        JSONObject links = JSONUtil.jsonRead("src/main/resources/b.json");
        Graph graph = new Graph(componets, links);
        graph.run();
    }

}
