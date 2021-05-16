package ui;

import java.util.ArrayList;
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
        System.out.println("(1001) Sistema Iniciado.");
        System.out.println("(2001) Identificacao etapa 1 iniciada.");
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

            System.out.println("(2002) Autenticação etapa 1 encerrada. ");
           
           


            System.out.println("(3001) Autenticação etapa 2 iniciada para "+  user.getEmail() + ".");
            PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
            while(selectedOption != 7) {
                System.out.println(keyBoard.getAsSingleString() + "7- END");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption < 7) {
                    keyBoard.pressGroup(selectedOption);
                    keyBoard.randomizeKeys();
                }
            }
//lucasdamo1@gmail.com
            System.out.println(" Senha selecionada : "+keyBoard.getSelectedPassword());//TODO APAGAR AQUI E NO MODEL USER -- APENaS PARA EBUG
            ArrayList<ArrayList<String>> password = keyBoard.getSelectedPassword();
           // System.out.println(user.getPossibilities(password));
            boolean isPasswordValid = user.getIsPasswordValid(password);
            if (!isPasswordValid){//TODO TEM QUE VER sE TA BLOQUEADO
                System.out.print("(3004) Primeiro erro da senha pessoal contabilizadopara "  + user.getEmail() + ".");
                //TODO Contabilizar eros
                
            } else if (isPasswordValid && user.isBlocked()) {//TODO VER SE TA BLOQUEADO COM o pRIMEIROluca
                System.out.println("(3007) Acesso do usuario " + user.getEmail() + " bloqueadopela autenticação etapa 2." );
                user = null;
            }
            else{
                System.out.println("(3003) Senha pessoal verificada positivamente para " + user.getEmail() + ".");
            } 
    
            System.out.println("(3002) Autenticação etapa 2 encerrada para "+  user.getEmail() + ".");
    





        }

       
        
    }
}
