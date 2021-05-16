package ui;

import java.util.ArrayList;
import java.nio.file.NoSuchFileException;
import java.security.PrivateKey;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        PrivateKeyHandler pkh = null;


        int selectedOption = 0;
        
        System.out.println(HEADER);
        Scanner sc= new Scanner(System.in); 
        PrivateKey privateK;
        
        while (user == null) {
            System.out.println("Insira seu email:");
            String userEmail = sc.nextLine();
            user = new User(userEmail);
            if (!user.isValid()){
                System.out.println("(2005) Login name "+userEmail+ " não identificado.");
                user = null;
            } else if (user.isValid() && user.isBlocked()){
                System.out.println("(2004) Login name "+ userEmail + " identificado com acesso bloqueado.");
                user = null;
            }
            else{
                System.out.println("(2003) Login name "+ userEmail + " lidentificadocom acesso liberado. ");
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
            //user01@inf1416.puc-rio.br
            System.out.println(" Senha selecionada : "+keyBoard.getSelectedPassword());//TODO APAGAR AQUI E NO MODEL USER -- APENaS PARA EBUG
            ArrayList<ArrayList<String>> password = keyBoard.getSelectedPassword();
            boolean isPasswordValid = user.getIsPasswordValid(password);
            if (!isPasswordValid){//TODO TEM QUE VER sE TA BLOQUEADO
                //TODO MENSAGEM 3004
                //TODO Contabilizar eros   
            } else if (isPasswordValid && user.isBlocked()) {//TODO VER SE TA BLOQUEADO COM o pRIMEIROluca
                //TODO mensagem 3007
                user = null;
            } else{
                //TODO mensagem 3003
            } 
            //TODO mensagem 3002
            System.out.println("(3002) Autenticação etapa 2 encerrada para .");

        }
        

           
        while (pkh == null){
            System.out.println("Insira o path para sua chave privada:");
            pkh = new PrivateKeyHandler(sc.nextLine());
            if(! pkh.isInitialized()) {
                pkh = null;
            }
        }

        privateK = null;
        while(privateK == null) {
            System.out.println("Insira o path para sua chave privada:");
            privateK = pkh.getPrivateKey(sc.nextLine());
        }


        
    }
}
