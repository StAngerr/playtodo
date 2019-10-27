package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import net.minidev.json.JSONObject;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.HashMap;

public class JwtHelper {
    private static JwtHelper instance;
    private static byte[] secret = new byte[32];

    private JwtHelper() {
        SecureRandom random = new SecureRandom();
        random.nextBytes(secret);
    }

    public static JwtHelper getInstance() {
        if (instance == null) {
            instance = new JwtHelper();
        }
        return instance;
    }

    public <T> String generateJwt(T payload) throws JOSEException, IllegalAccessException {
        JWSSigner signer = new MACSigner(secret);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(toJsonObject(payload)));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public static void parse(String jwt) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(jwt);
    }

    private static HashMap objectToMap(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, HashMap.class);
    }

    private static <T> JSONObject toJsonObject(T obj) throws IllegalAccessException {
        JSONObject result = new JSONObject();
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            result.put(field.getName(), field.get(obj));
        }
        return result;
    }
}
