import java.nio.file.*;
import java.security.*;
import java.security.spec.*;
import javax.crypto.Cipher;

public class Decryptor {
    public static void decryptFile(String inputFilePath, String outputFilePath, String privateKeyPath) throws Exception {
        byte[] privateKeyBytes = Files.readAllBytes(Paths.get(privateKeyPath));
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedContent = Files.readAllBytes(Paths.get(inputFilePath));
        byte[] decryptedContent = cipher.doFinal(encryptedContent);

        Files.write(Paths.get(outputFilePath), decryptedContent);
    }
}
