package util;

import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

public class JSONUtil {

    static public JSONObject jsonRead(String path) {
        try {
            JSONObject jsonobj = new JSONObject(new JSONTokener(new FileReader(new File(path))));
            return jsonobj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
