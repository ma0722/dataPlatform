package util;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.json.JSONObject;

import java.util.*;

public class MongoUtil {

    private static MongoClient mongoClient;
    private static MongoDatabase mongoDatabase;

    static {
        try {
            Properties properties = new Properties();
            properties.load(MongoClient.class.getResourceAsStream("/mongo.properties"));
            final String host = properties.getProperty("host");
            final int port = Integer.valueOf(properties.getProperty("port"));
            final String dataBase = properties.getProperty("dataBase");
//            final String username = properties.getProperty("username");
//            final String password = properties.getProperty("password");
//            ServerAddress serverAddress = new ServerAddress(host, port);
//            MongoCredential mongoCredential = MongoCredential.createScramSha1Credential(username, dataBase, password.toCharArray());
//            List<ServerAddress> addrs = new ArrayList<ServerAddress>();
//            addrs.add(serverAddress);
//            List<MongoCredential> credentials = new ArrayList<MongoCredential>();
//            credentials.add(mongoCredential);
//            mongoClient = new MongoClient(addrs, credentials);
            mongoClient = new MongoClient(host, port);
            mongoDatabase = mongoClient.getDatabase(dataBase);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void insert_model_info(Map<String, Object> values) {
        MongoCollection<Document> collection =  mongoDatabase.getCollection("model_info");
        Document document  = new Document(values);
        collection.insertOne(document);
    }

    public static void main(String[] args) {
        MongoCollection<Document> collection =  mongoDatabase.getCollection("model_info");
        FindIterable<Document> findIterable = collection.find();
        MongoCursor<Document> mongoCursor = findIterable.iterator();
        while(mongoCursor.hasNext()){
            System.out.println(mongoCursor.next());
        }
    }

}
