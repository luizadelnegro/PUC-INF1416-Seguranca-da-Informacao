package ui;

import java.util.ArrayList;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;

import controllers.RegistrosLogger;
import models.DigitalVaultFile;
import models.NewUser;
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
    private static Scanner sc= new Scanner(System.in); 

    private static final Logger LOGGER = Logger.getLogger(Console.class.getName());

    private static final String CABECALHO = "\nLogin: %s\n" +
        "Grupo: %s\n" + 
        "Nome: %s\n";

    private static final String CORPO1 = "Total de acessos do usuário: total_de _acessos_do_usuario %d\n";

    private static final String MENUPRINCIPALADMIN = "\nMenu Principal:\n\n" +
        "1 – Cadastrar um novo usuário\n" +
        "2 – Alterar senha pessoal e certificado digital do usuário\n" + 
        "3 – Consultar pasta de arquivos secretos do usuário\n" +
        "4 – Sair do Sistema\n";

    private static final String MENUPRINCIPALUSUARIO = "Menu Principal:\n\n" +
        "1 – Alterar senha pessoal e certificado digital do usuário\n" + 
        "2 – Consultar pasta de arquivos secretos do usuário\n" +
        "3 – Sair do Sistema\n";


    public static void cadastrarUsuario(User admin) {
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format("Total de usuarios no sistema: %d", user.getTotalUsuarios());
        String crtPath = null;
        Integer grupo = null;
        Integer selectedOption = 1;
        Boolean samePass = false;

        NewUser nu = new NewUser();
        System.out.println(cabecalho);
        System.out.println(corpo1);
        System.out.println("Formulario de cadastro: \n");

        while(crtPath == null) {
            System.out.println("Caminho do arquivo do certificado digital (EXIT para cancelar): ");
            crtPath = sc.nextLine();
            if(crtPath.equals("EXIT")) return;
            if (!nu.setCrtPath(crtPath)) {
                crtPath = null;
            }
        }
        while(grupo == null) {
            HashMap<Integer, String> gruposPossiveis = NewUser.getGroupsOptions();
            System.out.println("Grupo: " + gruposPossiveis.toString());
            grupo = sc.nextInt();
            if(gruposPossiveis.keySet().contains(grupo)) {
                nu.setGrupo(grupo);
            }
            else {
                grupo = null;
            }
        }
        while(!samePass) {
            samePass = true;
            System.out.println("Senha: ");
            selectedOption = 1;
            while(selectedOption != 0) {
                System.out.println(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                }
            }
            selectedOption = 1;
            System.out.println("Confirmacao senha: ");
            String firstPass = PhoneticKeyBoard.getPassword();
            nu.setPassword(firstPass);
            while(selectedOption != 0) {
                System.out.println(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                }
            }
            String secondPass = PhoneticKeyBoard.getPassword();
            if(!firstPass.equals(secondPass)) {
                samePass = false;
                System.out.println("Senhas nao coincidem!");
            }
            
        }
        if(!nu.saveToDb()) {
            System.out.println("Error!");
        }

    }

    public static void alterarSenhaPessoal() {
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format(Console.CORPO1, user.getTotalDeAcessos());
        String crtPath = null;
        NewUser nu = new NewUser();
        Boolean samePass = false;
        Integer selectedOption;
        
        System.out.println(cabecalho);
        System.out.println(corpo1);
        while(crtPath == null) {
            System.out.println("Caminho do arquivo do certificado digital (EXIT para cancelar): ");
            crtPath = sc.nextLine();
            if(crtPath.equals("EXIT")) return;
            if (!nu.setCrtPath(crtPath)) {
                crtPath = null;
            }
        }

        while(!samePass) {
            samePass = true;
            System.out.println("Senha: ");
            selectedOption = 1;
            while(selectedOption != 0) {
                System.out.println(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                }
            }
            selectedOption = 1;
            System.out.println("Confirmacao senha: ");
            String firstPass = PhoneticKeyBoard.getPassword();
            nu.setPassword(firstPass);
            while(selectedOption != 0) {
                System.out.println(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                }
            }
            String secondPass = PhoneticKeyBoard.getPassword();
            if(!firstPass.equals(secondPass)) {
                samePass = false;
                System.out.println("Senhas nao coincidem!");
            }
        }
        nu.saveToDb();


    }

    public static void consultarPastaDeArquivosSecretos(User u) {
        HashMap<Integer, String> indexFiles = new HashMap<Integer, String>();
        Path folderPath = null;
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format("Total de consultas do usuário: %d", user.getTotalConsultasDeAcessos());

        while (folderPath == null || !folderPath.toFile().exists()) {
            System.out.println("Caminho da pasta: ");
            String inputPath =sc.nextLine();
            if(inputPath.equals("EXIT")) {
                return;
            }
            folderPath = Paths.get(inputPath);
        }
        for (File file : folderPath.toFile().listFiles()) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getAbsolutePath());
            } else {
                // System.out.println("File: " + file.getAbsolutePath());
                // String extension = file.getAbsolutePath().substring(file.getAbsolutePath().indexOf("."));

                switch(file.getName()) {
                    case "index.env":
                        indexFiles.put(1, file.getAbsolutePath());
                        break;
                    case "index.enc":
                        indexFiles.put(2, file.getAbsolutePath());
                        break;
                    case "index.asd":
                        indexFiles.put(3, file.getAbsolutePath());
                        break;
                }
            }
        }

        DigitalVaultFile dvf = new DigitalVaultFile(indexFiles.get(1), indexFiles.get(2), indexFiles.get(3));
        try {
            byte[] indexDecrypted = dvf.decrypt(user.getPrivateKey());
            dvf.isFileValid(indexDecrypted, user.getPublicKey());
            System.out.println(new String(indexDecrypted, StandardCharsets.UTF_8));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        while(true) {
            //RegistrosLogger.log(1001, true); // Iniciado
            PrivateKeyHandler pkh = null;
            int tentativas=0;
            boolean validatedPassword=false;
            boolean isPasswordValid = false;
            int selectedOption = 0;
            
            System.out.println(HEADER);

            PrivateKey privateK;

            //RegistrosLogger.log(2001, true); // Inicio aut 1
            while (user == null) {
                /////////////////////// ETAPA DO EMAIL
                System.out.println("Insira seu email:");
                String userEmail = sc.nextLine();
                user = new User(userEmail);
                if (!user.isValid()){
                    //usuario errado
                    user = null;
                }else if(user.isBlocked()){
                    System.out.println("USU BLOQ");
                    user = null;
                } else if (user.isValid() && ! user.isBlocked()) {
                        while(tentativas<3 &&validatedPassword==false){
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

                            try{
                                isPasswordValid = user.getIsPasswordValid(password);
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                isPasswordValid = false;
                            }
                            if (!isPasswordValid){
                                //RegistrosLogger.log(3004, true);
                                tentativas+=1;
                                if(tentativas==3){
                                    user.blockUser();
                                    user=null;
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
            

            RegistrosLogger.log(4001, true); // Inicio aut 3
            while (pkh == null){
                System.out.println("Insira o path para sua chave privada: ");
                pkh = new PrivateKeyHandler(sc.nextLine());
                if(! pkh.isInitialized()) {
                    pkh = null;
                } else {
                    System.out.println("Insira seu passphrase: ");
                    PrivateKey privateKey = pkh.getPrivateKey(sc.nextLine());
                    if(privateKey == null) {
                        pkh = null;
                    }
                    else {
                        user.setPrivateKey(privateKey);
                        try {
                            System.out.println(PrivateKeyHandler.isPrivateKeyValid(privateKey, user.getPublicKey()));
                        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                            e.printStackTrace();
                            pkh = null;
                        }
                    }
                }
            }
            RegistrosLogger.log(4002, true); // Fim aut 3

            RegistrosLogger.log(4003, user.getLoginName(), true);
            String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
            String corpo1 = String.format(Console.CORPO1, user.getTotalDeAcessos());

            //RegistrosLogger.log(1002, true); // Finalizado
            

            selectedOption = 0;
            while(selectedOption != 4) {
                System.out.println(cabecalho);
                System.out.println(corpo1);

                if(user.isAdmin()){
                    System.out.println(MENUPRINCIPALADMIN);
                }
                else {
                    System.out.println(MENUPRINCIPALUSUARIO);
                }
                selectedOption = sc.nextInt();
                if (!user.isAdmin() && selectedOption != 0) {
                    selectedOption = selectedOption + 1;
                }
                switch(selectedOption) {
                    case 1:
                        cadastrarUsuario(user);
                        break;
                    case 2:
                        alterarSenhaPessoal();
                        break;
                    case 3:
                        consultarPastaDeArquivosSecretos(user);
                        break;
                    case 4:
                        user = null;
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                }
            }

        }
    }
}



            // /////////////////////// ETAPA DA SENHA
            // // if(user.isBlocked()==true){
            // //     System.out.println("Bloqueado antes da senha");
            // //     user=null;
            // // }
            // else{        
            //     while(tentativas<=3 &&validatedPassword==false && blocked==false){
            //         PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
            //         while(selectedOption != 7) {
            //             System.out.println(keyBoard.getAsSingleString() + "7- END");
            //             selectedOption = sc.nextInt();
            //             if (selectedOption > 0 && selectedOption < 7) {
            //                 keyBoard.pressGroup(selectedOption);
            //                 keyBoard.randomizeKeys();
            //             }
            //         }
            //         selectedOption = 0;
            //         ArrayList<ArrayList<String>> password = keyBoard.getSelectedPassword();
            //         System.out.println(" SENHA SELECIONADA"+password);
            //         boolean isPasswordValid = user.getIsPasswordValid(password);
            //         if (!isPasswordValid){
            //             //RegistrosLogger.log(3004, true);
            //             tentativas+=1;
            //             if(tentativas==3){
            //                 System.out.println("BLOQUED MENU");
            //                 user.blockUser();
            //                 user=null;
            //                 blocked=true;