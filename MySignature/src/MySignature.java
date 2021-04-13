import java.security.*;
import javax.crypto.*;
import java.util.*; 

public class MySignature {
    private static MySignature instance = null;
    private static MessageDigest firstMessageDigest;
    private static Cipher cipherMethod;
    private static KeyPair myKeys;

    //metodo 1
    //get Instance()-> instancia a classe MS e gera as chaves
    private static MySignature getInstance (String text) throws NoSuchAlgorithmException, NoSuchPaddingException {
        if (instance == null){
            instance = new MySignature();
        }
        String[] spliting = text.split("with");
        String digest = spliting[0];
        String method = spliting[1];
        System.out.println("Type of hash on message digest " + digest);
        System.out.println("Method used " + method);//depois escrever melhor

        cipherMethod = Cipher.getInstance(method);
        firstMessageDigest =  MessageDigest.getInstance(digest);// faz o primeiro digest
        // agora fazer as chaves privadas e publicas
        KeyPairGenerator keys=  KeyPairGenerator.getInstance(method);
        keys.initialize(2048);// nao sei q numero escolher
        myKeys=keys.generateKeyPair();
        System.out.println(myKeys);
        return instance;
	}

    // metodo 2
    //initSign() -> inicializa a assinatura digital
    public static void initSign(PrivateKey key) throws InvalidKeyException {		
        cipherMethod.init(Cipher.ENCRYPT_MODE, key);
    }
    //metodo 3
    // sign()-> com o digest da informação, assina com a chave privada de quem produziu essa informação gerando a assinatura
    public static byte[] sign() throws IllegalBlockSizeException, BadPaddingException {
        return cipherMethod.doFinal(firstMessageDigest.digest());
        }   

    //metodo 4
    //update()-> update digest using specified array of bytes
    public static void update(byte[] data) {
	    firstMessageDigest.update(data);
	}

    //metodo 5
    //initVerify -> inicializa a verificação, temos q ter a chave publica
    public static void initVerify(PublicKey key) throws InvalidKeyException {  
        cipherMethod.init(Cipher.DECRYPT_MODE, key);
    }

    //metodo 6
    //verify -> pega os dois digest gerados e compara
    public static boolean verify(byte[] signature) throws IllegalBlockSizeException, BadPaddingException {
        return Arrays.equals(firstMessageDigest.digest(), cipherMethod.doFinal(signature));
    }


    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in);  
        System.out.println("Hello, World!");
        System.out.print("Enter text: ");  
        String text= sc.nextLine();  
        System.out.println(text);
        byte[] textSnippet=text.getBytes("UTF8");
        System.out.print("Enter pattern: ");  
        String pattern= sc.nextLine(); 
        System.out.println(pattern);
        // Criacao da assinatura
        //inicializa
        getInstance(pattern);
        //inicializa a assinatura digital
        initSign(myKeys.getPrivate());
        //atualiza com nosso texto para termos o message digest
        update(textSnippet);
        //com o digest da informação, assina com a chave privada de quem produziu essa informação gerando a assinatura
        byte[] firstSignature = sign();
        System.out.println(firstSignature);// em hexadecimal
        
        // verificao da assinat
        initVerify(myKeys.getPublic());
        update(textSnippet);
        if(verify(firstSignature)==true){
            System.out.print("Same! "); 
        }else{
            System.out.print("Different "); }
        sc.close();
    }
}
