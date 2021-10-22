package cn.mtjsoft.groupavatarslib.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mtj
 * @date 2021/10/21
 * @desc
 * @email mtjsoft3@gmail.com
 */
public class MD5Util {
    public MD5Util() {
    }

    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        md5.update(data);
        return md5.digest();
    }

    public static String stringMD5(String input) {
        try {
            byte[] resultByteArray = encryptMD5(input.getBytes());
            return bytesToHexString(resultByteArray);
        } catch (Exception var4) {
            Log.e("MD5Util", "stringMD5()... error : " + var4.getMessage());
            return "";
        }
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
