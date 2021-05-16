package controllers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;



public class PrivateKeyHandler {
    
    private byte[] encryptedKey;

    public PrivateKeyHandler(String pathToPrivateKey) {
        try {
            this.encryptedKey = Files.readAllBytes(Paths.get(pathToPrivateKey));  
        } catch (IOException e) {
            
        }
    }

    public Boolean isInitialized() {
        return this.encryptedKey != null;
    }

    public PrivateKey getPrivateKey(String passphrase) {
        SecureRandom sha1prng;
        KeyGenerator kg;
        Cipher cipherMethod;
        PKCS8EncodedKeySpec keyspec;
        try {
            sha1prng = SecureRandom.getInstance("SHA1PRNG");
            kg = KeyGenerator.getInstance("DES");

            cipherMethod = Cipher.getInstance("DES/ECB/PKCS5Padding");
            sha1prng.setSeed(passphrase.getBytes());
            kg.init(56, sha1prng);

            cipherMethod.init(Cipher.DECRYPT_MODE, kg.generateKey());
            byte[] finalEn = cipherMethod.doFinal(this.encryptedKey);
            String b64 = new String(finalEn, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("\n", "")
                .replace("-----END PRIVATE KEY-----", "");
            byte[] finalDe = Base64.getDecoder().decode(b64);
            keyspec = new PKCS8EncodedKeySpec(finalDe);

            return KeyFactory.getInstance("RSA").generatePrivate(keyspec);
        } catch (InvalidKeyException | InvalidKeySpecException | NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            e.printStackTrace();

            return null;
        }
    }

    public static Boolean isPrivateKeyValid(PrivateKey privateK, PublicKey publicK) throws NoSuchAlgorithmException, NoSuchPaddingException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        Cipher cipherMethod = Cipher.getInstance("RSA");
        byte[] randomArray;
        byte[] digestedArray;
        byte[] decriptedDigest;

        // Create random array and get digest of it
        randomArray = new byte[2048];
        new Random().nextBytes(randomArray);
        digest.update(randomArray);
        digestedArray = digest.digest();

        try {
            // Encrypt with private key
            cipherMethod.init(Cipher.ENCRYPT_MODE, privateK);
            byte[] sigPrivateK = cipherMethod.doFinal(digestedArray);
            
            // Decrypt with public key
            cipherMethod.init(Cipher.DECRYPT_MODE, publicK);
            decriptedDigest = cipherMethod.doFinal(sigPrivateK);
        } catch (IllegalBlockSizeException | InvalidKeyException | BadPaddingException e) {
            e.printStackTrace();
            return false;
        }

        // Check if decrypted digest equals generated digest
        return Arrays.equals(decriptedDigest, digestedArray);

    }


}
