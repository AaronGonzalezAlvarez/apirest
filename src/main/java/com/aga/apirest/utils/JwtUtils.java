package com.aga.apirest.utils;

import com.aga.apirest.models.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

public class JwtUtils {
    private static Gson gson = new Gson();

    private static String SecretkeyStatic = "sdfgsdgsdfgsdtyqwerfqeryrtuiuyodfgbcbncvbndfhxczbdhg";
    public static String getJWT(User user) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(user);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 5);
            Date fechaExpiracion = calendar.getTime();
            SecretKey key = Keys.hmacShaKeyFor(SecretkeyStatic.getBytes());
            String token = Jwts.builder()
                    .setSubject(json)
                    .setIssuedAt(new Date())
                    .setExpiration(fechaExpiracion)
                    .signWith(key)
                    .compact();
            return token;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String devolverValoresJWT(String jwt) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(SecretkeyStatic.getBytes())
                    .build()
                    .parseClaimsJws(jwt);
            Claims claims = claimsJws.getBody();
            String username = claims.getSubject();

            ObjectMapper objectMapper = new ObjectMapper();
            User deserializedPerson = objectMapper.readValue(username, User.class);
            String json = objectMapper.writeValueAsString(deserializedPerson);
            return json;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static User getUserforJSON(String json){
        try {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, User.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String checkJWTForEndPointReturnId(String jwt){
        if(jwt.isEmpty()){
            return "header vacío";
        }
        String jwtData;
        try {
            jwtData = devolverValoresJWT(jwt);
        }catch (Exception e){
            return "JWT corructo";
        }
        User userForJSON = getUserforJSON(jwtData);
        return String.valueOf(userForJSON.getId());
    }

    public static String checkJWTForEndPointReturnRol(String jwt){
        if(jwt.isEmpty()){
            return "header vacío";
        }
        String jwtData;
        try {
            jwtData = devolverValoresJWT(jwt);
        }catch (Exception e){
            return "JWT corructo";
        }
        User userForJSON = getUserforJSON(jwtData);
        return String.valueOf(userForJSON.getRol());
    }
}
