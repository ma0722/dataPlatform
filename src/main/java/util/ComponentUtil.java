package util;

import component.Component;
import org.json.JSONObject;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;

public class ComponentUtil {

    static private HashMap<String, String> componentPath;

    static {
        componentPath = new HashMap<String, String>();
        try{

            JSONObject componentInfo = JSONUtil.jsonRead("src/main/resources/static/component.json");
            Iterator iterator = componentInfo.keys();
            while(iterator.hasNext()){
                String key = (String)iterator.next();
                String value = componentInfo.getJSONObject(key).getString("class");
                componentPath.put(key, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static public Component generateComponent(String type) throws Exception{
        if(!componentPath.containsKey(type))
            return null;
        String classPath= componentPath.get(type);
        Class clazz = Class.forName(classPath);
        Constructor constructor = clazz.getConstructor();
        return (Component) constructor.newInstance();
    }


}
