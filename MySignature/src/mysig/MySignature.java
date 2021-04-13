package mysig;

import java.security.*;
import javax.crypto.*;
import java.util.*; 

public class MySignature {
    private MessageDigest digest;
    private Cipher cipherMethod;
    
    public static MySignature getInstance (String instanceType) throws NoSuchAlgorithmException, NoSuchPaddingException {
        MySignature instance = new MySignature();
        instance.digest =  MessageDigest.getInstance(instanceType.split("with")[0]);
        instance.cipherMethod = Cipher.getInstance(instanceType.split("with")[1]);
        return instance;
	}

    public byte[] getDigest() {
        return this.digest.digest();
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
        return Arrays.equals(this.digest.digest(), this.cipherMethod.doFinal(signature));
    }
}
