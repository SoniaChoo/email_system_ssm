package org.example.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String getCaptchCodeByRegex(String html) {
        Pattern p = Pattern.compile("\\d{6}"); // 6位数字
        Matcher m = p.matcher(html);

        if (m.find()) {
            return m.group();
        }
        return "";
    }
}
