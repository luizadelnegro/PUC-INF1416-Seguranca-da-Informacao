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
        System.out.println("Method used " + method);
        cipherMethod = Cipher.getInstance(method);
        firstMessageDigest =  MessageDigest.getInstance(digest);
        KeyPairGenerator keys=  KeyPairGenerator.getInstance(method);
        keys.initialize(2048);
        myKeys=keys.generateKeyPair();
        return instance;
	}

    public static void initSign(PrivateKey key) throws InvalidKeyException {		
        cipherMethod.init(Cipher.ENCRYPT_MODE, key);
    }

    public static byte[] sign() throws IllegalBlockSizeException, BadPaddingException {
        return cipherMethod.doFinal(firstMessageDigest.digest());
        }   

    public static void update(byte[] data) {
	    firstMessageDigest.update(data);
	}

    public static void initVerify(PublicKey key) throws InvalidKeyException {  
        cipherMethod.init(Cipher.DECRYPT_MODE, key);
    }

    public static boolean verify(byte[] signature) throws IllegalBlockSizeException, BadPaddingException {
        return Arrays.equals(firstMessageDigest.digest(), cipherMethod.doFinal(signature));
    }

    //convert to hexadecimal
    public static String toHex (byte[] bytes) {
        StringBuffer buffer = new StringBuffer();
        for(int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0x0100 + (bytes[i] & 0x00FF)).substring(1);
            buffer.append((hex.length() < 2 ? "0" : "") + hex);
        }
        return buffer.toString();
    }
    
    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in);  
        System.out.println("INF1416 - Segurança da informação");
        System.out.println("Grupo: Luiza e Lucas");
        System.out.println("********************************************************************");
        System.out.print("Enter text: ");  
        String text= sc.nextLine();  
        byte[] textSnippet=text.getBytes("UTF8");
        System.out.print("Enter pattern: ");  
        String pattern= sc.nextLine(); 
        System.out.println("Criando assinatura");
        System.out.println("getInstance(padrão)-> Inicialização da classe MySignature e dos métodos para criação de chave ");
        getInstance(pattern);
        System.out.println("initSign(chave Privada)->Inicializa assinatura digital com chave privada");
        initSign(myKeys.getPrivate());
        System.out.println("update(mensagem)-> Atualiza o message digest com o texto dado");
        update(textSnippet);
        System.out.println("sign()-> Faz assinatura digital com message digest atualizado");
        byte[] signature = sign();
        System.out.println("Message digest:" +toHex(firstMessageDigest.digest()));
        System.out.println("Assinatura digital:" +toHex(signature));
        System.out.println("********************************************************************");
        System.out.println("Verificando assinatura");
        System.out.println("initVerify(chave publica)-> Inicializa a verificação com a chave publica dada");
        initVerify(myKeys.getPublic());
        System.out.println("update()-> Atualiza message digest com texto");
        update(textSnippet);
        System.out.println("verify(assinatura)-> verifica se message digest gerado pela decriptação da assinatura com a chave publica é o mesmo que o digest gerado depois");
        if(verify(signature)==true){
            System.out.print("Same! "); 
        }else{
            System.out.print("Different!"); }
        System.out.println("Fim!");
        sc.close();
    }
}
