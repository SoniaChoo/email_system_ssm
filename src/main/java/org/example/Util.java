package org.example;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    public static String getCaptchCodeByRegex(String html) {
        //String html = "<html style=\"background:#f7f7f7\">\r\n<head>\r\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" />\r\n<title>\u767e\u5ea6\u5e10\u53f7\u2014\u90ae\u7bb1\u5b89\u5168\u9a8c\u8bc1</title>\r\n</head>\r\n<body>\r\n<div style=\"background:#f7f7f7;overflow:hidden\">\r\n<div>\r\n<img src=\"https://passport.baidu.com/img/logo.gif\" alt=\"\" class=\"logo\" ellpadding=\"0\" cellspacing=\"0\" style=\"margin:40px 0 0 60px\" />\r\n</div>\r\n<div style=\"background:#fff;border:1px solid #ccc;margin:2%;padding:0 30px\">\r\n<div style=\"line-height:40px;height:40px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;font-size:14px;line-height:30px;color:#333;font-family:arial,sans-serif;font-weight:bold\">\u4eb2\u7231\u7684\u7528\u6237\uff1a</p>\r\n<div style=\"line-height:20px;height:20px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'\u5b8b\u4f53',arial,sans-serif\">\u60a8\u597d\uff01\u611f\u8c22\u60a8\u4f7f\u7528\u767e\u5ea6\u670d\u52a1\uff0c\u60a8\u7684\u8d26\u53f7\uff08XX***\u4f1a\u5458\uff09\u6b63\u5728\u8fdb\u884c\u90ae\u7bb1\u9a8c\u8bc1\uff0c\u672c\u6b21\u8bf7\u6c42\u7684\u9a8c\u8bc1\u7801\u4e3a\uff1a</p>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'\u5b8b\u4f53',arial,sans-serif\"><b style=\"font-size:18px;color:#f90\">223353</b><span style=\"margin:0;padding:0;margin-left:10px;line-height:30px;font-size:14px;color:#979797;font-family:'\u5b8b\u4f53',arial,sans-serif\">(\u4e3a\u4e86\u4fdd\u969c\u60a8\u5e10\u53f7\u7684\u5b89\u5168\u6027\uff0c\u8bf7\u57281\u5c0f\u65f6\u5185\u5b8c\u6210\u9a8c\u8bc1\u3002)</span></p>\r\n<div style=\"line-height:80px;height:80px\">&nbsp;</div>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'\u5b8b\u4f53',arial,sans-serif\">\u767e\u5ea6\u5e10\u53f7\u56e2\u961f</p>\r\n<p style=\"margin:0;padding:0;line-height:30px;font-size:14px;color:#333;font-family:'\u5b8b\u4f53',arial,sans-serif\">2020\u5e7406\u670821\u65e5</p>\r\n<div style=\"line-height:20px;height:20px\">&nbsp;</div>\r\n<div style=\"border-top:1px dashed #dfdfdf;padding:30px 0;overflow:hidden\">\r\n<div style=\"float:left;width:110px\">\r\n<img src=\"https://passport.baidu.com/export/app/img/qrcode_android.png\" style=\"border:1px solid #dfdfdf;padding:5px\" />\r\n</div>\r\n<div style=\"overflow:hidden\">\r\n<p style=\"text-indent:2em;color:#666;font-size:14px\">\u7ed1\u5b9a<a href=\"https://passport.baidu.com/export/app/index.html\" style=\"font-size:16px;color:#36c;text-decoration:none;font-weight:bold\" target=\"_blank\">\u767e\u5ea6\u5e10\u53f7\u7ba1\u5bb6</a>\uff0c\u6536\u53d6\u9a8c\u8bc1\u7801\u4e0d\u518d\u7b49\u5f85\uff01</p>\r\n<p style=\"text-indent:2em;color:#666;font-size:14px\">\u8d76\u7d27\u626b\u63cf\u4e0b\u8f7d\u5427</p>\r\n</div>\r\n</div>\r\n</div>\r\n</div>\r\n</body>\r\n</html>";
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
