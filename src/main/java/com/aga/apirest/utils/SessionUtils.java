package com.aga.apirest.utils;

import java.util.ArrayList;

public class SessionUtils {

    private static ArrayList<Integer>  session = new ArrayList<>();

    public static boolean addSession(Integer x) {
        for (Integer z : session) {
            if (z.equals(x)) {
                return false;
            }
        }
        session.add(x);
        return true;
    }

    public static void delete(Integer x) {
        session.remove(x);
    }

    public static void resetSession(){
        session.clear();
    }
}
