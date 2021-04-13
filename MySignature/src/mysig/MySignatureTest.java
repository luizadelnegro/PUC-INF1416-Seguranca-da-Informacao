package mysig;

import java.util.Scanner;
import java.math.BigInteger;
import java.security.*; 




public class MySignatureTest {
    static final String HEADER = "" +
    "**************************************" +
    "\tINF1416 - Segurança da informação\n" +
    "Trabalho 1 - MySignature 2021-04-13\n" +
    "Grupo: Luiza e Lucas\n" + 
    "**************************************";

    public static String toHex(byte[] bytes){
        return String.format("%032x", new BigInteger(1, bytes));
    }

    public static void main(String[] args) throws Exception {
        
        Scanner sc= new Scanner(System.in); 
        
        System.out.println(HEADER);
                
        System.out.print("Enter pattern: ");
        String method = sc.nextLine();

        System.out.println("Type of hash on message digest " + method.split("with")[0]);
        System.out.println("Method used " + method.split("with")[1]);

        MySignature sig = MySignature.getInstance(method);
        KeyPairGenerator keys = KeyPairGenerator.getInstance(method.split("with")[1]);

        System.out.println("Enter text to digest: ");
        byte[] textSnippet = sc.nextLine().getBytes("UTF8");
        
        sig.update(textSnippet);

        keys.initialize(2048);
        KeyPair myKeys = keys.genKeyPair();

        System.out.println("Criando assinatura");
        sig.initSign(myKeys.getPrivate());
        byte[] firstSignature = sig.sign();

        System.out.println("Message digest:" + MySignatureTest.toHex(sig.getDigest()));
        System.out.println("Assinatura digital:" + MySignatureTest.toHex(firstSignature));
        System.out.println("Verificando assinatura");
        
        sig.update(textSnippet);    // Para verificar re-insere o texto para o digest       
        sig.initVerify(myKeys.getPublic());
        if(sig.verify(firstSignature) == true) {
            System.out.println("VERIFICADO!");
        }
        else {
            System.out.println("ERRO!");
        }
        sc.close();
    }
}

