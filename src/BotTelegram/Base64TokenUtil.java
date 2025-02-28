/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/File.java to edit this template
 */
package BotTelegram;

/**
 *
 * @author kagam
 */
import java.util.Base64;

public class Base64TokenUtil {
//    public static void main(String[] args) {
//        for (int i = 258; i < 400; i++) {
//            System.out.println(i);
//        }
//    }
    // Mã hóa token
    public static String encodeToken(String token) {
        return Base64.getEncoder().encodeToString(token.getBytes());
    }

    // Giải mã token
    public static String decodeToken(String encodedToken) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedToken);
        return new String(decodedBytes);
    }

    public static void main(String[] args) {
        String token = "";
        System.out.println("Token gốc: " + token);

        // Mã hóa token
        String encodedToken = encodeToken(token);
        System.out.println("Token mã hóa (Base64): " + encodedToken);

        // Giải mã token
        String decodedToken = decodeToken(encodedToken);
        System.out.println("Token giải mã: " + decodedToken);
    }
}

