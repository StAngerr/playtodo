package utils;

import models.User;
import net.minidev.json.JSONObject;
import org.json.simple.JSONArray;
import utils.collections.MyList;

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

    public static JSONArray usersToJson(MyList<User> users) {
        JSONArray result = new JSONArray();
        for (User user : users) {
            result.add(user.asJson());
        }
        return result;
    }
}
