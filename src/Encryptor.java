import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.Cipher;

public class Encryptor {
    public static void encryptFile(String inputFilePath, String outputFilePath, String publicKeyPath) throws Exception {
        byte[] publicKeyBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] fileContent = Files.readAllBytes(Paths.get(inputFilePath));
        byte[] encryptedContent = cipher.doFinal(fileContent);

        Files.write(Paths.get(outputFilePath), encryptedContent);
    }

    public static void encryptString(String inputString, String publicKeyPath, String outputFilePath) throws Exception {
        byte[] publicKeyBytes = Files.readAllBytes(Paths.get(publicKeyPath));
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] encryptedContent = cipher.doFinal(inputString.getBytes());

        Files.write(Paths.get(outputFilePath), encryptedContent);
    }

    public static void generateKeys(String publicKeyPath, String privateKeyPath) throws Exception {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair pair = keyGen.generateKeyPair();

        try (FileOutputStream fos = new FileOutputStream(publicKeyPath)) {
            fos.write(pair.getPublic().getEncoded());
        }

        try (FileOutputStream fos = new FileOutputStream(privateKeyPath)) {
            fos.write(pair.getPrivate().getEncoded());
        }
    }
}