package com.stockmarket;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashPassword {

    public static byte[] getSHA(String password) {
        try {
            MessageDigest mD = MessageDigest.getInstance("SHA-256");
            return mD.digest(password.getBytes(StandardCharsets.UTF_8));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toHexString(byte[] mySHA) {
        BigInteger number = new BigInteger(1, mySHA);
        StringBuilder hexString = new StringBuilder(number.toString(16));

        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }
        return hexString.toString();

    }

    public static String hashPassword(String password) {
        return toHexString(getSHA(password));
    }

    public static boolean arePasswordEqual(String password, String hashedPasword) {
        if (hashPassword(password).equals(hashedPasword)) {
            return true;
        }
        return false;
    }







    // public static String hash(String input) {
    //     try {
    //         MessageDigest md = MessageDigest.getInstance("SHA-256");
    //         byte[] bytes = md.digest(input.getBytes());
    //         StringBuilder sb = new StringBuilder();
    //         for (int i = 0; i < bytes.length; i++) {
    //             sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
    //         }
    //         return sb.toString();
    //     } catch (Exception e) {
    //         throw new RuntimeException(e);
    //     }
    // }

    // public static boolean validate(String input, String hash) {
    //     return hash(input).equals(hash);
    // }
}
