package models;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class DigitalVaultFile {
    

    private Path env;
    private Path enc;
    private Path asd;

    public DigitalVaultFile(String fileEnv, String fileEnc, String fileAsd) {
        this.env = Paths.get(fileEnv);
        this.enc = Paths.get(fileEnc);
        this.asd = Paths.get(fileAsd);
    }

    public byte[] decrypt(PrivateKey privateKey) throws Exception {
        // Decrypt .env and get seet to prng
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        c.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] pkcs = c.doFinal(Files.readAllBytes(this.env));
    
        // Get DES key
        SecureRandom prng = SecureRandom.getInstance("SHA1PRNG");
        prng.setSeed(pkcs);
        KeyGenerator prngKG = KeyGenerator.getInstance("DES");
        prngKG.init(56, prng);
        SecretKey prngKey = prngKG.generateKey();
        
        // Finally decrypt file
        Cipher encC = Cipher.getInstance("DES/ECB/PKCS5Padding");
        encC.init(Cipher.DECRYPT_MODE, prngKey);
        return encC.doFinal(Files.readAllBytes(this.enc));
    }

    public boolean isFileValid(byte[] decryptedFile, PublicKey publicKey) throws Exception {
        Signature sig = Signature.getInstance("SHA1withRSA");
        sig.initVerify(publicKey);
        sig.update(decryptedFile);
        return sig.verify(Files.readAllBytes(this.asd));
        
        // PS: WHY IS THE FOLLOWING NOT WORKING ?
        // MessageDigest digest = MessageDigest.getInstance("SHA-1");
        // Cipher cipherMethod = Cipher.getInstance("RSA");

        // digest.update(decryptedFile);
        // cipherMethod.init(Cipher.ENCRYPT_MODE, publicKey);
        
        // byte[] sig = cipherMethod.doFinal(digest.digest());
        // System.out.println(new String(Files.readAllBytes(this.env), StandardCharsets.UTF_8));
        // return Arrays.equals(sig, Files.readAllBytes(this.env));
    }


    
}
