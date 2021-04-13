import java.security.*;
import javax.crypto.*;
import java.util.*; 

public class MySignature {
    private static MySignature instance = null;

//metodo 1
//get Instance()-> instancia a classe MS e gera as chaves
    public static MySignature getInstance (String text) {
        if (instance==null){
            instance= new MySignature();
        }
        String[] spliting = text.split("with");
        String digest = spliting[0];
        String method = spliting[1];

        return instance;
	}
    public void generateKeys(String seed){
        
    }



// metodo 2
//initSign() -> inicializa a assinatura digital

//metodo 3
//update()-> pega o Message Digest

//metodo 4
// sign()-> com o digest da informação, assina com a chave privada de quem produziu essa informação gerando a assinatura

//metodo 5
//initVerify -> inicializa a verificação, temos q ter a chave publica

//metodo 6
//verify -> pega os dois digest gerados e compara


    public static void main(String[] args) throws Exception {
        Scanner sc= new Scanner(System.in);  
        System.out.println("Hello, World!");
        System.out.print("Enter text: ");  
        String text= sc.nextLine();  
        System.out.println(text);
        System.out.print("Enter signature pattern: ");  
        String signature= sc.nextLine(); 
        System.out.println(signature);






    }
}
