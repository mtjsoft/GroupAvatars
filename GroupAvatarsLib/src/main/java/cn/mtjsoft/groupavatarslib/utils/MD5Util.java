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
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] inputByteArray = input.getBytes();
            messageDigest.update(inputByteArray);
            byte[] resultByteArray = messageDigest.digest();
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException var4) {
            Log.e("MD5Util", "stringMD5()... error : " + var4.getMessage());
            return "";
        }
    }

    private static String byteArrayToHex(byte[] byteArray) {
        char[] hexDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        char[] resultCharArray = new char[byteArray.length * 2];
        int index = 0;
        byte[] var4 = byteArray;
        int var5 = byteArray.length;
        for(int var6 = 0; var6 < var5; ++var6) {
            byte b = var4[var6];
            resultCharArray[index++] = hexDigits[b >>> 4 & 15];
            resultCharArray[index++] = hexDigits[b & 15];
        }
        String result;
        for(result = new String(resultCharArray); result.length() < 16; result = "0" + result) {
        }
        return result;
    }
}
