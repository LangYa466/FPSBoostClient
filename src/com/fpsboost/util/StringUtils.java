package com.fpsboost.util;

import com.fpsboost.module.handlers.ModuleHandle;

import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StringUtils {

    public static String findLongestModuleName(List<ModuleHandle> modules) {
        return Collections.max(modules, Comparator.comparing(module -> (module.getName() + (!module.getValues().isEmpty() ? " " + module.getSuffix() : "")).length())).getName();
    }

    public static String getLongestModeName(List<String> listOfWords) {
        String longestWord = null;
        for (String word : listOfWords) {
            if (longestWord == null || word.length() > longestWord.length()) {
                longestWord = word;
            }
        }
        return longestWord != null ? longestWord : "";
    }

    public static String b64(Object o) {
        return Base64.getEncoder().encodeToString(String.valueOf(o).getBytes());
    }

}