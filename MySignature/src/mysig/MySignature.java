package mysig;

import java.security.*;
import javax.crypto.*;
import java.util.*; 

public class MySignature {
    private MessageDigest digest;
    private Cipher cipherMethod;

    private static final Map<String,String> mapAlgorithmName = Map.of("SHA1", "SHA-1",
        "SHA256", "SHA-256",
        "SHA512", "SHA-512",
        "MD5", "MD5"
    );
    
    public static MySignature getInstance (String instanceType) throws NoSuchAlgorithmException, NoSuchPaddingException {
        MySignature instance = new MySignature();
        if(!mapAlgorithmName.containsKey(instanceType.split("with")[0])) {
            throw new NoSuchAlgorithmException();
        }
        instance.digest =  MessageDigest.getInstance(mapAlgorithmName.get(instanceType.split("with")[0]));
        instance.cipherMethod = Cipher.getInstance(instanceType.split("with")[1]);
        return instance;
	}

    public void initSign(PrivateKey key) throws InvalidKeyException {		
        this.cipherMethod.init(Cipher.ENCRYPT_MODE, key);
    }

    public byte[] sign() throws IllegalBlockSizeException, BadPaddingException {
        return this.cipherMethod.doFinal(this.digest.digest());
    }   

    public void update(byte[] data) {
	    this.digest.update(data);
	}

    public void initVerify(PublicKey key) throws InvalidKeyException {  
        this.cipherMethod.init(Cipher.DECRYPT_MODE, key);
    }

    public boolean verify(byte[] signature) throws IllegalBlockSizeException, BadPaddingException {
        byte[] d = this.digest.digest(); // Only for debugging
        System.out.println("Digested expected:\t" + Util.toHex(d));  // Must update before with plain text
        byte[] c = this.cipherMethod.doFinal(signature);
        System.out.println("Decrypted:\t\t" + Util.toHex(c));
        return Arrays.equals(d, c);
    }
}
