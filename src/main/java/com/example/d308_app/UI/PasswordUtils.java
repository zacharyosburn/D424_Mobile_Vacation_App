package com.example.d308_app.UI;

import java.security.MessageDigest;

public class PasswordUtils {
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encoded = digest.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : encoded) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isValidPassword(String password){
        boolean hasNumber = false;
        boolean hasSpecialChar = false;
        String specialChars = "!@#$%^&*?";

        if(password == null || password.length() < 8){
            return false;
        }

        for(char i : password.toCharArray()){
            if(Character.isDigit(i)){
                hasNumber = true;
            } else if(specialChars.indexOf(i) >= 0) {
                hasSpecialChar = true;
            }
        }
        return hasNumber && hasSpecialChar;
    }
    public static boolean verifyPassword(String password, String hash) {
        return hashPassword(password).equals(hash);
    }
}
