package application;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

public class DESUtil {

    //encrypt string key to a SecretKey
    private static SecretKey getKey(String keyString) throws Exception {
        byte[] key = keyString.getBytes("UTF-8");
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        key = sha.digest(key);
        key = Arrays.copyOf(key, 8);//using only 8
        return new SecretKeySpec(key, "DES");
    }

    public static String encrypt(String plainText, String keyString) throws Exception {
        SecretKey key = getKey(keyString);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String encryptedText, String keyString) throws Exception {
        SecretKey key = getKey(keyString);
        Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainTextBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(plainTextBytes);
    }
}