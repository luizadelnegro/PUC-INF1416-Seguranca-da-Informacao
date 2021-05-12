package ui;

import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import models.PhoneticKeyBoard;
import models.User;

public class Console {
    static final String HEADER = "" +
    "**************************************" +
    "\tINF1416 - Segurança da informação\t" +
    "**************************************\n" +
    "\tTrabalho 4 - Digital Vault 2021-mm-dd\n" +
    "\tGrupo: Luiza e Lucas\n" + 
    "**************************************\n\n";

    private static User user;

    private static final Logger LOGGER = Logger.getLogger(Console.class.getName());

    public static void main(String args[]) {
        int selectedOption = 0;
        
        System.out.println(HEADER);
        Scanner sc= new Scanner(System.in); 
        
        while (user == null) {
            System.out.println("Insira seu email:");
            String userEmail = sc.nextLine();
            user = new User(userEmail);
            if (! user.isValid()) {
                LOGGER.log(Level.SEVERE, "Wrong email! " + userEmail);
                user = null;
            }
        }
        PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
        while(selectedOption != 7) {
            System.out.println(keyBoard.getAsSingleString() + "7- END");
            selectedOption = sc.nextInt();
            if (selectedOption > 0 && selectedOption < 7) {
                keyBoard.pressGroup(selectedOption);
                keyBoard.randomizeKeys();
            }

        }

        
    }
}
