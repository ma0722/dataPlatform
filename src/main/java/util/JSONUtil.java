package util;

import org.json.JSONObject;
import org.json.JSONTokener;
import org.mortbay.util.ajax.JSON;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
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


    static public void jsonWrite(String filePath, String content) {
        try {
            File file = new File(filePath);
            PrintStream ps = new PrintStream(new FileOutputStream(file));
            ps.print(content);// 往文件里写入字符串
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
