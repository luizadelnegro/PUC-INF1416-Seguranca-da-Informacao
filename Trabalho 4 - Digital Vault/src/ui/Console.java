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
import javax.xml.datatype.XMLGregorianCalendar;

import controllers.RegistrosLogger;
import controllers.X509CertificateHandler;
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
        X509CertificateHandler xHandler = null;
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
            xHandler = nu.setCrtPath(crtPath);
            if (xHandler == null) {
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
            int previousPhonema = 0;
            samePass = true;
            System.out.println("Senha: ");
            selectedOption = 1;
            while(selectedOption != 0) {
                System.out.println(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                    if(previousPhonema == selectedOption) {
                        // Sequencia de fonema repetido!
                        RegistrosLogger.log(6003, user.getEmail(), true);
                        continue;
                    }
                    previousPhonema = selectedOption;
                }
            }
            selectedOption = 1;
            System.out.println("Confirmacao senha: ");
            String firstPass = PhoneticKeyBoard.getPassword();
            if(firstPass.length() < 8 || firstPass.length() > 12) {
                // Senha com menos de 4 fonemas ou mais de 12!
                RegistrosLogger.log(6003, user.getEmail(), true);
                continue;
            }
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
        System.out.println(xHandler.getConfirmationString());
        System.out.println("0- Cadastrar\t9- Voltar");
        selectedOption = sc.nextInt();
        if (selectedOption == 0) {
            if(!nu.saveToDb()) {
                System.out.println("Error!");
            }
        }

    }

    public static void alterarSenhaPessoal() {
        X509CertificateHandler xHandler = null;
        String firstPass = null;
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
            if(crtPath.equals("")) break;   // Nao vai mudar o certificado!
            if(crtPath.equals("EXIT")) return;
            xHandler = nu.setCrtPath(crtPath);
            if (xHandler == null) {
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
            if(selectedOption == 0) {
                // nao vai alterar a senha
                break;
            }
            selectedOption = 1;
            System.out.println("Confirmacao senha: ");
            firstPass = PhoneticKeyBoard.getPassword();
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
        System.out.println(xHandler.getConfirmationString());
        System.out.println("0- Cancelar\t9- Confirmar");
        selectedOption = sc.nextInt();
        if(selectedOption == 9) {
            user.updateUser(xHandler, firstPass);
        }

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

    public static User newUserFromEmail() {
        User u = null;
        while (u == null) {
            System.out.println("Insira seu email:");
            String userEmail = sc.nextLine();
            u = new User(userEmail);
            if (!u.isValid()){
                //usuario errado
                u = null;
            } else if(u.isBlocked()){
                System.out.println("USU BLOQ");
                u = null;
            }
        }
        return u;
    }

    public static boolean userPassWord() {
        boolean isPasswordValid = false;
        int tentativas = 0;
        int selectedOption = 0;
        while(tentativas < 3 && ! isPasswordValid){
            PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
            while(selectedOption != 7) {
                System.out.println(keyBoard.getAsSingleString() + "7- END" + "\t0- EXIT");
                selectedOption = sc.nextInt();
                if (selectedOption == 0) return false;
                if (selectedOption > 0 && selectedOption < 7) {
                    keyBoard.pressGroup(selectedOption);
                    keyBoard.randomizeKeys();
                }
            }
            selectedOption = 8;
            ArrayList<ArrayList<String>> password = keyBoard.getSelectedPassword();

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
                if(tentativas >= 3){
                    user.blockUser();
                    return false;
                }
                System.out.println("TENTATIVA "+tentativas); //mensagem das tentativas  
            }
        }
        return true;
    }

    public static boolean getPrivateKey() {
        PrivateKeyHandler pkh = null;
        boolean isValid = false;

        while (pkh == null){
            System.out.println("Insira o path para sua chave privada: ");
            String pathKey = sc.nextLine();
            if(pathKey == "EXIT") return false;
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
                        isValid = PrivateKeyHandler.isPrivateKeyValid(privateKey, user.getPublicKey());
                        user.setPrivateKey(privateKey);
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                        e.printStackTrace();
                        pkh = null;
                    }
                }
            }
        }
        return isValid;
    }

    public static void main(String args[]) {
        while(true) {
            //RegistrosLogger.log(1001, true); // Iniciado
            int selectedOption = 0;
            
            System.out.println(HEADER);

            if(user==null) user = newUserFromEmail();
            if(!userPassWord()) {
                user = null;
                continue;
            }
            if(!getPrivateKey()) {
                user = null;
                continue;
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