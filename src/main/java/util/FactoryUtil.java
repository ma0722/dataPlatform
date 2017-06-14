package util;


public class FactoryUtil {

    private static FactoryUtil factoryUtil;
    private HDFSFileUtil hdfsUtil;
    private JSONUtil jsonUtil;
    private MongoUtil mongoUtil;
    private SparkUtil sparkUtil;

    private FactoryUtil(){}

    public static FactoryUtil getFactory() {
        if (factoryUtil == null)
            factoryUtil = new FactoryUtil();
        return factoryUtil;
    }

    public HDFSFileUtil getHDFSUtil() {
        if (this.hdfsUtil == null)
            this.hdfsUtil = new HDFSFileUtil();
        return this.hdfsUtil;
    }

    public JSONUtil getJsonUtil(){
        if (this.jsonUtil == null)
            this.jsonUtil = new JSONUtil();
        return this.jsonUtil;
    }

    public MongoUtil getMongoUtil(){
        if (this.mongoUtil == null)
            this.mongoUtil = new MongoUtil();
        return this.mongoUtil;
    }

    public SparkUtil getSparkUtil(){
        if (this.sparkUtil == null)
            this.sparkUtil = new SparkUtil();
        return this.sparkUtil;
    }
}
