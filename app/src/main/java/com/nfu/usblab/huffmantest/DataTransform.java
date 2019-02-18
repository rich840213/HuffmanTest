package com.nfu.usblab.huffmantest;

import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

public class DataTransform {

    private static Map<String, String> digiMap = new HashMap<>();

    static {
        digiMap.put("0", "0000");
        digiMap.put("1", "0001");
        digiMap.put("2", "0010");
        digiMap.put("3", "0011");
        digiMap.put("4", "0100");
        digiMap.put("5", "0101");
        digiMap.put("6", "0110");
        digiMap.put("7", "0111");
        digiMap.put("8", "1000");
        digiMap.put("9", "1001");
        digiMap.put("a", "1010");
        digiMap.put("b", "1011");
        digiMap.put("c", "1100");
        digiMap.put("d", "1101");
        digiMap.put("e", "1110");
        digiMap.put("f", "1111");
    }

    /**
     * Hex轉Binary
     *
     * @param s
     * @return
     */
    public static String hexToBin(String s) {
        char[] hex = s.toCharArray();
        String binaryString = "";
        for (char h : hex) {
            binaryString = binaryString + digiMap.get(String.valueOf(h));
        }

        return binaryString;
    }

    public static boolean isNumeric(String s) {
        if (!s.isEmpty()) {
            return s.matches("^[0-9]*$");
        }

        return false;
    }

    /**
     * String轉Byte數組
     *
     * @param str
     * @return
     */
    public static byte[] strToByteArray(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();

        return byteArray;
    }

    /**
     * Byte數組轉String
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToStr(byte[] byteArray) {
        if (byteArray == null) {
            return null;
        }
        String str = new String(byteArray);

        return str;
    }

    /**
     * Byte數組轉十六進制String
     *
     * @param byteArray
     * @return
     */
    public static String byteArrayToHexStr(byte[] byteArray) {
        System.out.println(byteArray.length);

        if (byteArray == null) {
            return null;
        }
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[byteArray.length * 2];
        for (int j = 0; j < byteArray.length; j++) {
            int v = byteArray[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }

        return new String(hexChars);
    }

    /**
     * 十六進制String轉Byte數組
     *
     * @param str
     * @return
     */
    public static byte[] hexStrToByteArray(String str) {
        if (str == null) {
            return null;
        }
        if (str.length() == 0) {
            return new byte[0];
        }
        byte[] byteArray = new byte[str.length() / 2];
        for (int i = 0; i < byteArray.length; i++) {
            String subStr = str.substring(2 * i, 2 * i + 2);
            byteArray[i] = ((byte) Integer.parseInt(subStr, 16));
        }

        return byteArray;
    }

    /**
     * 字串md5編碼
     *
     * @param string
     * @return
     */
    public static String md5(String string) {
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes("UTF-8"));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10)
                    hex.append("0");
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return string;
        }
    }
}
