package utils;

import net.minidev.json.JSONObject;

import java.lang.reflect.Field;

public class JsonHelper {
    public static <T> JSONObject toJsonObject(T obj) throws IllegalAccessException {
        JSONObject result = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            result.put(field.getName(), field.get(obj));
        }
        return result;
    }
}
