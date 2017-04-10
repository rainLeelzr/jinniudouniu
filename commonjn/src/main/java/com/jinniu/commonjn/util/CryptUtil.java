package com.jinniu.commonjn.util;


import java.security.MessageDigest;

/**
 * 
* @Title: CryptUtil.java
* @Package commons.lander.util
* @Description:  MD5 、 SHA1加密工具
* @author chenwenhao 
* @date 2016-12-1 上午11:12:08
 */
public class CryptUtil {

    private static String digest(String s, String algoritjn) {
        if (s == null || s.length() <= 0) {
            return s;
        }
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
        try {
            byte[] strTemp = s.getBytes("UTF-8");
            MessageDigest mdTemp = MessageDigest.getInstance(algoritjn);
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String sha1(String s) {
        return digest(s, "SHA1");
    }

    public static String md5(String s) {
        return digest(s, "MD5");
    }

    public static String encrptyPassport(String passport) {
        if (passport == null) {
            return null;
        }
        if (passport.length() <= 2) {
            return "**";
        }
        int len = (passport.length() - 2) / 2;
        return passport.substring(0, len) + "**" + passport.substring(len + 2);
    }

    public static String encryptPhone(String phone) {
        if (phone == null) {
            return null;
        }
        if (phone.length() <= 3) {
            return "***";
        }
        char[] c = phone.toCharArray();
        for (int i = 0; i < phone.length() - 3; i++) {
            if (i >= 4) {
                break;
            }
            c[i + 3] = '*';
        }
        return new String(c);
    }

    public static void main(String[] args) {
      //  System.out.println(sha1("456123"));
         System.out.println(md5("fkId=22&type=3&client_secret=f513032c-b3d7-410d-815c-2f5c28f44469"));
    }
}
