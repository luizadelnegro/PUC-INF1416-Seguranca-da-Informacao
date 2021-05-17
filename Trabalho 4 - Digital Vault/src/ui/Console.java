package ui;

import java.util.ArrayList;
import java.io.UnsupportedEncodingException;
import java.nio.file.NoSuchFileException;
import java.security.NoSuchAlgorithmException;
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


    public static void main(String args[]) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        RegistrosLogger.log(1001, true); // Iniciado
        PrivateKeyHandler pkh = null;


        int selectedOption = 0;
        
        System.out.println(HEADER);
        Scanner sc= new Scanner(System.in); 
        PrivateKey privateK;

        int tentativas=0;
        boolean validatedPassword=false;
        boolean blocked=false;
        //boolean authorized=false;
        //RegistrosLogger.log(2001, true); // Inicio aut 1
        
        while (user == null) {
            /////////////////////// ETAPA DO EMAIL
            System.out.println("Insira seu email:");
            String userEmail = sc.nextLine();
            user = new User(userEmail);
            if (!user.isValid()){
                //usuario errado
                user = null;
            } else if (user.isValid() && user.isBlocked()){
                user = null;
            }
            else{
                //PASSOU MENSAGEM DE PASSOU
            }
            /////////////////////// ETAPA DA SENHA
            // if(user.isBlocked()==true){
            //     System.out.println("Bloqueado antes da senha");
            //     user=null;
            // }
            else{        
                while(tentativas<=3 &&validatedPassword==false && blocked==false){
                    PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
                    while(selectedOption != 7) {
                        System.out.println(keyBoard.getAsSingleString() + "7- END");
                        selectedOption = sc.nextInt();
                        if (selectedOption > 0 && selectedOption < 7) {
                            keyBoard.pressGroup(selectedOption);
                            keyBoard.randomizeKeys();
                        }
                    }
                    selectedOption = 0;
                    ArrayList<ArrayList<String>> password = keyBoard.getSelectedPassword();
                    System.out.println(" SENHA SELECIONADA"+password);
                    boolean isPasswordValid = user.getIsPasswordValid(password);
                    if (!isPasswordValid){
                        //RegistrosLogger.log(3004, true);
                        tentativas+=1;
                        if(tentativas==3){
                            System.out.println("BLOQUED MENU");
                            user.blockUser();
                            user=null;
                            blocked=true;
                        }
                        System.out.println("TENTATIVA"+tentativas); //mensagem das tentativas  
                    } else{
                        validatedPassword=true;
                    }    
                }
                //saiu da senha
            }
            //user01@inf1416.puc-rio.br
 
            }
            //RegistrosLogger.log(3002, true); // Fim aut 2
            System.out.println("SAIU DA SENHA"); 
        

        
        //RegistrosLogger.log(3002, true); // Fim aut 2

        //RegistrosLogger.log(4001, true); // Inicio aut 3
        while (pkh == null){
            System.out.println("Insira o path para sua chave privada:");
            pkh = new PrivateKeyHandler(sc.nextLine());
            if(! pkh.isInitialized()) {
                pkh = null;
            }
        }
        //RegistrosLogger.log(4002, true); // Fim aut 2

        //RegistrosLogger.log(1002, true); // Finalizado
        
    }
}
