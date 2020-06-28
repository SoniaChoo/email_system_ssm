package org.example.util;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static void main(String[] args) {
        String prefix = "7tian";
        String lifetime = "7";
        int count = 200;
        List<String> codeList = generateInvitationCode(prefix, count);
        // INSERT INTO `email_system`.`invitation` (`invitation_id`, `invitation_code`, `invitation_lifetime`) VALUES ('10', 'dddddddd', '1');

        String insert = "INSERT INTO `email_system`.`invitation` (`invitation_id`, `invitation_code`, `invitation_lifetime`) VALUES\n";
        for (String s : codeList) {
            System.out.println(s);

            String id = UUID.randomUUID().toString();
            insert+=" ('"+id+"', '"+s+"', '"+lifetime+"'),\n";
        }
        System.out.println("\n\n\n");
        System.out.println(insert);
    }
    public static String getCaptchCodeByRegex(String html) {
        Pattern p = Pattern.compile("\\d{6}"); // 6位数字
        Matcher m = p.matcher(html);

        if (m.find()) {
            return m.group();
        }
        return "";
    }

    public static List<String> generateInvitationCode(String prefix, int count) {
        List<String> codeList = new ArrayList<String>(count);
        for (int i = 0; i < count; i++) {
            String[] split = UUID.randomUUID().toString().split("-");
            if (split.length != 4) {
                System.out.println("UUID format error!");
                return null;
            }
            String uuidCode = split[0]+split[1]+split[2];
            codeList.add(prefix+uuidCode);
        }
        return codeList;
    }
}
