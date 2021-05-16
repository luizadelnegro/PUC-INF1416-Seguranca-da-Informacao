package ui;

import java.nio.file.NoSuchFileException;
import java.security.PrivateKey;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import controllers.RegistrosLogger;

import models.PhoneticKeyBoard;
import models.User;
import controllers.PrivateKeyHandler;

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
        RegistrosLogger.log(1001, true); // Iniciado
        PrivateKeyHandler pkh = null;


        int selectedOption = 0;
        
        System.out.println(HEADER);
        Scanner sc= new Scanner(System.in); 
        PrivateKey privateK;

        RegistrosLogger.log(2001, true); // Inicio aut 1
        while (user == null) {
            System.out.println("Insira seu email:");
            String userEmail = sc.nextLine();
            user = new User(userEmail);
            if (! user.isValid()) {
                LOGGER.log(Level.SEVERE, "Wrong email! " + userEmail);
                user = null;
            }
        }
        RegistrosLogger.log(2002, true); // Fim aut 2

        RegistrosLogger.log(3001, true); // Inicio aut 2
        // PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
        // while(selectedOption != 7) {
        //     System.out.println(keyBoard.getAsSingleString() + "7- END");
        //     selectedOption = sc.nextInt();
        //     if (selectedOption > 0 && selectedOption < 7) {
        //         keyBoard.pressGroup(selectedOption);
        //         keyBoard.randomizeKeys();
        //     }

        // }
        RegistrosLogger.log(3002, true); // Fim aut 2

        RegistrosLogger.log(4001, true); // Inicio aut 3
        while (pkh == null){
            System.out.println("Insira o path para sua chave privada:");
            pkh = new PrivateKeyHandler(sc.nextLine());
            if(! pkh.isInitialized()) {
                pkh = null;
            }
        }
        RegistrosLogger.log(4002, true); // Fim aut 2

        RegistrosLogger.log(1002, true); // Finalizado
        
    }
}
