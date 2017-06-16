package util;

public class UtilManager {

    private static UtilManager utilManager;
    private HDFSFileUtil hdfsFileUtil;
    private JSONUtil jsonUtil;
    private MongoUtil mongoUtil;
    private SparkUtil sparkUtil;


    private UtilManager(){}

    public static UtilManager getUtilManager() {
        if(utilManager == null)
            utilManager = new UtilManager();
        return utilManager;
    }

    public HDFSFileUtil getHDFSFileUtil() {
        if(hdfsFileUtil == null)
            hdfsFileUtil = new HDFSFileUtil();
        return hdfsFileUtil;
    }

    public JSONUtil getJsonUtil() {
        if(jsonUtil == null)
            jsonUtil = new JSONUtil();
        return jsonUtil;
    }

    public MongoUtil getMongoUtil() {
        if(mongoUtil == null)
            mongoUtil = new MongoUtil();
        return mongoUtil;
    }

    public SparkUtil getSparkUtil() {
        if (sparkUtil == null)
            sparkUtil = new SparkUtil();
        return sparkUtil;
    }
}
