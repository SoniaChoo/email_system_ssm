package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String getCaptchCodeByRegex(String html) {
        Pattern p = Pattern.compile("\\d{6}");
        Matcher m = p.matcher(html);
        boolean b = m.find();
        String find_result = "";
        if(b){
            find_result=m.group();
        }
        return find_result;
    }
}
