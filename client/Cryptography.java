package client;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import compute.Task;

public class Cryptography implements Task<String>, Serializable {
    
    private String text;
    private String encryptedText;
    private String key;

    @Override
    public String execute() {
        System.out.println("\n\nExecuting Cryptography...");
        this.encryptedText = encryptText(text, key);
        if (this.encryptedText == null) {
            System.out.println("Error encrypting text.");
            return null;
        }
        System.out.println("Cryptography executed.\n\n");
        return this.encryptedText;
    }

    public Cryptography(String text) {
        this.text = text;
        this.key = "0123456789123456";
    }

    public static String encryptText(String text, String key) {
        try {
            System.out.println("Encrypting text...");
            Cipher cipher = Cipher.getInstance("AES");
            SecretKey secretKey = generateSecretKey(key);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            System.out.println("Text encrypted.");
            byte[] encryptedBytes = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return new String(encryptedBytes, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static SecretKey generateSecretKey(String key) throws NoSuchAlgorithmException {
        // Generate a 128-bit AES secret key from the provided key bytes
        byte[] keyBytes = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        return secretKeySpec;
    }
}
