package lockd;

import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;

/**
 * @author johnandrewoss
 * @date 01/13/2017
 * @title Crypto.java
 * @purpose Handles the AES encryption/decryption, as well as hashing.
 * 
 */

//WARNING!!! THIS USES ECB WHICH IS INSECURE AND SHOULD BE CHANGED EVENTUALLY.
public class Crypto {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    /*
     * @purpose: Sets the key for the AES algorithm implementation.
     * @param: String mykey: The key to be set.
     */
    private static void setKey(String myKey) {
        try {
            //Should probably be something more than SHA-1.
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(myKey.getBytes("UTF-8"));
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            System.out.println("Error while setting key: " + e.toString());
        }
    }

    /*
     * @purpose: Encrypts the given string.
     * @param: String plaintext: the desired string to be encrypted.
               String secret: the secretKey to encrypt the String with.
     * @returns: The encrypted string, or NULL if it fails.
     */
    public static String encrypt(String plaintext, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder().encodeToString(cipher.doFinal(plaintext.getBytes("UTF-8")));
        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }
    
    /*
     * @purpose: Decrypts the given string.
     * @param: String ciphertext: the desired string to be decrypted.
               String secret: the secretKey to decrypt the ciphertext with.
     * @returns: The decrypted string, or NULL if it fails.
     */ 
    public static String decrypt(String ciphertext, String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder().decode(ciphertext)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }
    
    /*
     * @purpose: Hashes the given string using SHA-256.
     * @param: String plaintext: The text to be hashed.
     * @returns: The hash of the given string, in hex.
     */    
    public static String sha256hash(String plaintext) {
        try{
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            //Converting from bytes to hex
            return (new HexBinaryAdapter()).marshal(sha256.digest(plaintext.getBytes("UTF-8")));
        } catch(Exception e) {
            System.out.println("Error while hashing: " + e.toString());
        }
        return null;
    }
}