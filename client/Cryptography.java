package client;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import compute.Task;

public class Cryptography implements Task<String>, Serializable {
    
    private String imagePath;
    private String encryptedImagePath;
    private String key;

    @Override
    public String execute() {
        System.out.println("\n\nExecuting Cryptography...");
        encryptImage(this.imagePath, this.encryptedImagePath, this.key);
        System.out.println("Cryptography executed.\n\n");
        return this.encryptedImagePath;
    }

    public Cryptography(String imagePath) {
        this.imagePath = imagePath;
        this.encryptedImagePath = "imagem_encrypted.png";
        this.key = "0123456789123456";
    }

    public static void encryptImage(String imagePath, String encryptedImagePath, String key) {
        try {
            System.out.println("Encrypting image...");
            // Lê a imagem em um array de bytes
            byte[] imageBytes = Files.readAllBytes(Paths.get(imagePath));

            // Gera uma chave secreta baseada na chave fornecida
            SecretKey secretKey = generateSecretKey(key);

            // Cria um objeto de cifra para criptografia
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            // Criptografa o array de bytes da imagem
            byte[] encryptedBytes = cipher.doFinal(imageBytes);

            // Escreve os bytes criptografados em um novo arquivo
            Path encryptedPath = Paths.get(encryptedImagePath);
            Files.write(encryptedPath, encryptedBytes);

            System.out.println("Image encryption completed.");

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public static void decryptImage(String encryptedImagePath, String decryptedImagePath, String key) {
        try {
            // Read the encrypted image file into a byte array
            byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedImagePath));

            // Generate a secret key based on the provided key
            SecretKey secretKey = generateSecretKey(key);

            // Create a cipher object for decryption
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            // Decrypt the encrypted byte array
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            // Write the decrypted bytes to a new file
            Path decryptedPath = Paths.get(decryptedImagePath);
            Files.write(decryptedPath, decryptedBytes);

            System.out.println("Image decryption completed.");

        } catch (IOException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }
    }

    public static SecretKey generateSecretKey(String key) throws NoSuchAlgorithmException {
        // Generate a 128-bit AES secret key from the provided key bytes
        byte[] keyBytes = key.getBytes();
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        return secretKeySpec;
    }
}
