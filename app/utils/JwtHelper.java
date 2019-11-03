package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;

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
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(JsonHelper.toJsonObject(payload)));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public JWSObject parse(String jwt) throws ParseException {
        JWSObject jwsObject = JWSObject.parse(jwt);
        return jwsObject;
    }

    private static HashMap objectToMap(Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(obj, HashMap.class);
    }
}
