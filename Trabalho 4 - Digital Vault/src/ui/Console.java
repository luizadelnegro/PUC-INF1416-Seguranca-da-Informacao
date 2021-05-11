package ui;

import java.util.Scanner;
import models.User;

public class Console {
    static final String HEADER = "" +
    "**************************************" +
    "\tINF1416 - Segurança da informação\t" +
    "**************************************\n" +
    "\tTrabalho 4 - Digital Vault 2021-mm-dd\n" +
    "\tGrupo: Luiza e Lucas\n" + 
    "**************************************\n\n";
    public static void main(String args[]) {
        System.out.println(HEADER);
        Scanner sc= new Scanner(System.in); 
        
        System.out.println("Insira seu email:");
        String user_email = sc.nextLine();
        User user = new User(user_email);

        
    }
}
