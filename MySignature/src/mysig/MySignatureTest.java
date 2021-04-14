package mysig;

import java.util.Scanner;
import java.math.BigInteger;
import java.security.*; 




public class MySignatureTest {
    static final String HEADER = "" +
    "**************************************" +
    "\tINF1416 - Segurança da informação\t" +
    "**************************************\n" +
    "\tTrabalho 1 - MySignature 2021-04-13\n" +
    "\tGrupo: Luiza e Lucas\n" + 
    "**************************************\n\n";

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

        System.out.println("Generating keys: ");
        keys.initialize(2048);
        KeyPair myKeys = keys.genKeyPair();
        System.out.println("-----------------------------");
        System.out.println("Public key: " + Util.toHex(myKeys.getPublic().getEncoded()));
        System.out.println("Private key: " + Util.toHex(myKeys.getPrivate().getEncoded()));
        System.out.println("-----------------------------\n");
        

        System.out.println("Making digital signature");
        sig.initSign(myKeys.getPrivate());
        byte[] firstSigned = sig.sign();
        
        System.out.println("Digital signature:" + Util.toHex(firstSigned));
        System.out.println("\nVerificando assinatura");
        
        sig.update(textSnippet);    // Insert plain text again to verify -- IMPORTANT!       
        sig.initVerify(myKeys.getPublic());
        if(sig.verify(firstSigned)) {
            System.out.println("\n\t\t\t\tVERIFICADO!");
        }
        else {
            System.out.println("\n\t\t\t\tERRO!");
        }
        sc.close();
    }
}

