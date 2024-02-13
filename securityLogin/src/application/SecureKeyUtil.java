package application;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class SecureKeyUtil {

    private static final String MASTER_KEY = "your-secure-master-key";

    public static String encryptKey(String key) throws Exception {
        Key aesKey = new SecretKeySpec(MASTER_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(key.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decryptKey(String encryptedKey) throws Exception {
        Key aesKey = new SecretKeySpec(MASTER_KEY.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedKey));
        return new String(decrypted);
    }
}