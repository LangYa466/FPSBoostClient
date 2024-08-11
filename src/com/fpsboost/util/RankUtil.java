package com.fpsboost.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LangYa
 * @since 2024/07/13/下午12:34
 */
public class RankUtil {

    public static Map<String,String> tokens = new HashMap<>();

    public static String getRank(String username) {
        return tokens.get(username);
    }

}
