package ui;

import java.util.ArrayList;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
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
import ui.MyUtil;


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

    public static String getNewPassword(int logFailId) {
        int selectedOption;
        boolean samePass = false;
        String firstPass = null;
        while(!samePass) {
            int previousPhonema = 0;
            samePass = true;
            System.out.println("Senha: ");
            selectedOption = 1;
            while(selectedOption != 0) {
                selectedOption = MyUtil.safeGetIntInput(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                if (previousPhonema == 0 && selectedOption == 0) {
                    // EXIT WITH BLANK PASSWORD
                    return "";
                }
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                    if(previousPhonema == selectedOption) {
                        // Sequencia de fonema repetido!
                        RegistrosLogger.log(logFailId, user.getEmail(), true);
                        previousPhonema = 0;
                        PhoneticKeyBoard.getPassword();
                        continue;
                    }
                    previousPhonema = selectedOption;
                }
            }
            selectedOption = 1;
            System.out.println("Confirmacao senha: ");
            firstPass = PhoneticKeyBoard.getPassword();
            if(firstPass.length() < 8 || firstPass.length() > 12) {
                // Senha com menos de 4 fonemas ou mais de 12!
                RegistrosLogger.log(6003, user.getEmail(), true);
                firstPass = null;
                continue;
            }
            while(selectedOption != 0) {
                selectedOption = MyUtil.safeGetIntInput(PhoneticKeyBoard.phonemesPasswordAll() + "\t0-EXIT");
                if (selectedOption > 0 && selectedOption <= 18) {
                    PhoneticKeyBoard.pressPhonema(selectedOption);
                }
            }
            String secondPass = PhoneticKeyBoard.getPassword();
            if(!firstPass.equals(secondPass)) {
                samePass = false;
                RegistrosLogger.log(logFailId, user.getEmail(), true);
                System.out.println("Senhas nao coincidem!");
            }
            
        }
        return firstPass;
    }


    public static void cadastrarUsuario(User admin) {
        X509CertificateHandler xHandler = null;
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format("Total de usuarios no sistema: %d", user.getTotalUsuarios());
        String crtPath = null;
        Integer grupo = null;
        Integer selectedOption = 1;
        String password = "";
        NewUser nu = new NewUser();
        System.out.println(cabecalho);
        System.out.println(corpo1);

        RegistrosLogger.log(6001, user.getEmail(), false);
        System.out.println("Formulario de cadastro: \n");
        while(true){
            int continuar = MyUtil.safeGetIntInput("Deseja cadastrar um novo usuario? Aperte qualquer tecla ou\t 9- Voltar ao menu principal.");
            if (continuar == 9){
                RegistrosLogger.log(6007, user.getEmail(), false);
                return;
            }
            RegistrosLogger.log(6002, user.getEmail(), false);
            while(crtPath == null) {
                crtPath = MyUtil.safeGetString("Caminho do arquivo do certificado digital (EXIT para cancelar): ");
                if(crtPath.equals("EXIT")) return;
                xHandler = nu.setCrtPath(crtPath);
                if (xHandler == null) {
                    RegistrosLogger.log(6004, user.getEmail(), false);
                    crtPath = null;
                }
            }
            password = getNewPassword(6003);
            if(password.equals("")) { 
                RegistrosLogger.log(6003, user.getEmail(), false);  
                return;
            }
            nu.setPassword(password);
            while(grupo == null) {
                HashMap<Integer, String> gruposPossiveis = NewUser.getGroupsOptions();
                grupo = MyUtil.safeGetIntInput("Grupo: " + gruposPossiveis.toString());
                if(gruposPossiveis.keySet().contains(grupo)) {
                    nu.setGrupo(grupo);
                }
                else {
                    grupo = null;
                }
            }
            
            System.out.println(xHandler.getConfirmationString());
            selectedOption = MyUtil.safeGetIntInput("0- Cancelar\t9- Cadastrar");
            if (selectedOption == 9) {
                RegistrosLogger.log(6005, user.getEmail(), false);
                if(!nu.saveToDb()) {
                    System.out.println("Error!");
                }
            }
            else {
                RegistrosLogger.log(6006, user.getEmail(), false);
            }
        }

    }

    public static boolean alterarSenhaPessoal() {
        X509CertificateHandler xHandler = null;
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format(Console.CORPO1, user.getTotalDeAcessos());
        String crtPath = null;
        Integer selectedOption;
        boolean changingPass = false;
        boolean changingCert = false;
        
        System.out.println(cabecalho);
        System.out.println(corpo1);
        RegistrosLogger.log(7001, user.getEmail(), false);
        String oldUserEmail = user.getEmail();
        while(true) {
            if(MyUtil.safeGetIntInput("Deseja alterar senha ou certificado? Aperte qualquer tecla ou\t9- Voltar ao menu") == 9) {
                RegistrosLogger.log(7006, oldUserEmail, false);
                return changingCert;
            } 
            while(crtPath == null) {
                crtPath = MyUtil.safeGetString("Caminho do arquivo do certificado digital: ");
                if(crtPath.equals("")) break;   // Nao vai mudar o certificado!
                try{
                    FileInputStream fis = new FileInputStream(crtPath);
                    xHandler = new X509CertificateHandler(fis);
                    fis.close();
                } catch (IOException | CertificateException e) {
                    RegistrosLogger.log(7003, user.getEmail(), false);
                }
                if (xHandler == null) {
                    crtPath = null;
                }
            }
            
            String password = getNewPassword(7002);
            if(password == null || password.equals("")) password = null;
            changingPass = password != null;
            if(xHandler != null) {
                 System.out.println(xHandler.getConfirmationString());
                 changingCert = true;
            }
            if(xHandler != null || changingPass) {
                System.out.println("Trocou a senha: " + String.valueOf(changingPass).replace("true", "sim").replace("false", "nao"));
                selectedOption = 1;
                while(selectedOption != 9 && selectedOption != 0){
                    selectedOption = MyUtil.safeGetIntInput("0- Voltar (Cancelar)\t9- Cadastrar");
                    if(selectedOption == 9) {
                        RegistrosLogger.log(7004, user.getEmail(), false);
                        user.updateUser(xHandler, password);
                    }
                    else if (selectedOption == 0){
                        RegistrosLogger.log(7005, user.getEmail(), false);
                    }
                }
                
            }
        }

    }

    public static void consultarPastaDeArquivosSecretos() {
        HashMap<Integer, String> indexFiles = new HashMap<Integer, String>();
        Path folderPath = null;
        byte[] indexDecrypted = null;
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format("Total de consultas do usuário: %d", user.getTotalConsultasDeAcessos());
        RegistrosLogger.log(8001, user.getEmail(), false);

        while (folderPath == null || !folderPath.toFile().exists()) {
            String inputPath = MyUtil.safeGetString("Caminho da pasta: \t'EXIT'- Para voltar ao menu principal");
            if(inputPath.equals("EXIT")) {
                RegistrosLogger.log(8002, user.getEmail(), false);
                return;
            }
            folderPath = Paths.get(inputPath);
            if(folderPath.toFile().exists()) {
                if (!folderPath.toFile().isDirectory()) RegistrosLogger.log(8004, user.getEmail(), false);
            }
            else RegistrosLogger.log(8004, user.getEmail(), false);
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
            indexDecrypted = dvf.decrypt(user.getPrivateKey());
        } catch(Exception e) {
            RegistrosLogger.log(8007, user.getEmail(), false);
            return;
        }
        RegistrosLogger.log(8005, user.getEmail(), false);
        try {
            dvf.isFileValid(indexDecrypted, user.getPublicKey());
            RegistrosLogger.log(8006, user.getEmail(), false);
        } catch(Exception e) {
            RegistrosLogger.log(8008, user.getEmail(), false);
        }
        int selectedOption = -1;
        while(selectedOption != 0) {
            String indexString = new String(indexDecrypted, StandardCharsets.UTF_8);
            String[] options = indexString.split("\n");
            for(int i=0; i<options.length; i++) {
                System.out.println(Integer.toString(i + 1) + "- " + options[i]);
            }
            RegistrosLogger.log(8009, user.getEmail(), false);
            selectedOption = MyUtil.safeGetIntInput("0 - Voltar ao menu principal");
            if (selectedOption > 0 && selectedOption <= options.length){
                Boolean canAccess = false;
                String[] row = options[selectedOption-1].split(" ");
                RegistrosLogger.log(8010, user.getEmail(), row[0], false);
                if(row[2].equals(user.getEmail()) || row[3].equals(user.getGroupName())) {
                    // Invalid permission
                    canAccess = true;
                }
                if(canAccess) {
                    RegistrosLogger.log(8011, user.getEmail(), row[1], false);
                    byte[] decFile = null;
                    Path fileEnv = Paths.get(folderPath.toString(), row[0] + ".env");
                    Path fileEnc = Paths.get(folderPath.toString(), row[0] + ".enc");
                    Path fileAsd = Paths.get(folderPath.toString(), row[0] + ".asd");
                    DigitalVaultFile sdf = new DigitalVaultFile(fileEnv.toString(), fileEnc.toString(), fileAsd.toString());
                    try {
                        decFile = sdf.decrypt(user.getPrivateKey());
                    } catch(Exception e) {
                        RegistrosLogger.log(8015, user.getEmail(), row[1], false);
                        continue;
                    }
                    RegistrosLogger.log(8013, user.getEmail(), row[1], false);
                    try {
                        sdf.isFileValid(decFile, user.getPublicKey());
                    } catch(Exception e) {
                        RegistrosLogger.log(8016, user.getEmail(), row[1], false);
                        continue;
                    }
                    RegistrosLogger.log(8014, user.getEmail(), row[1], false);
                    try {
                        FileOutputStream fos = new FileOutputStream(Paths.get(folderPath.toString(), row[1]).toString());
                        fos.write(decFile);
                        System.out.println("WROTE! path= " + Paths.get(folderPath.toString(), row[1]).toString());
                
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    RegistrosLogger.log(8012, user.getEmail(), row[1], false);
                }

            }
        }
        RegistrosLogger.log(8002, user.getEmail(), false);
    }

    public static User newUserFromEmail() {
        User u = null;
        while (u == null) {
            String userEmail = MyUtil.safeGetString("Insira seu email:");
            u = new User(userEmail);
            if (!u.isValid()){
                //usuario errado
                RegistrosLogger.log(2005, userEmail, false);
                u = null;
            } else if(u.isBlocked()){
                RegistrosLogger.log(2004, userEmail, true);
                u = null;
            }
        }
        RegistrosLogger.log(2003, u.getEmail(), false);
        return u;
    }

    public static boolean userPassWord() {
        boolean isPasswordValid = false;
        int tentativas = 0;
        int selectedOption = 0;
        while(tentativas < 3 && ! isPasswordValid){
            PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
            while(selectedOption != 7) {
                selectedOption = MyUtil.safeGetIntInput(keyBoard.getAsSingleString() + "7- END" + "\t0- EXIT");
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
                tentativas+=1;
                RegistrosLogger.log(3003 + tentativas, user.getEmail(), false);
                if(tentativas >= 3){
                    RegistrosLogger.log(3007, user.getEmail(), false);
                    user.blockUser();
                    return false;
                }
                System.out.println("TENTATIVA "+tentativas); //mensagem das tentativas  
            }
        }
        RegistrosLogger.log(3003, user.getEmail(), false);
        return true;
    }

    public static boolean getPrivateKey() {
        PrivateKeyHandler pkh = null;
        boolean isValid = false;

        while (pkh == null){
            while(pkh==null) {
                String pathKey = MyUtil.safeGetString("Insira o path para sua chave privada: ");
                try {
                    pkh = new PrivateKeyHandler(pathKey);
                } catch (IOException e) {
                    RegistrosLogger.log(4004, user.getEmail(), false);
                }
            }
            if(! pkh.isInitialized()) {
                pkh = null;
            } else {
                PrivateKey privateKey = pkh.getPrivateKey(MyUtil.safeGetPassword("Insira seu passphrase: "));
                if(privateKey == null) {
                    pkh = null;
                    RegistrosLogger.log(4005, user.getEmail(), true);

                }
                else {
                    user.setPrivateKey(privateKey);
                    try {
                        isValid = PrivateKeyHandler.isPrivateKeyValid(privateKey, user.getPublicKey());
                        if (!isValid) {
                            RegistrosLogger.log(4006, user.getEmail(), false);
                            pkh= null;
                        }
                        else user.setPrivateKey(privateKey);
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
                        e.printStackTrace();
                        pkh = null;
                    }
                }
            }
        }
        if(isValid) RegistrosLogger.log(4003, user.getEmail(), false);
        return isValid;
    }

    public static void main(String args[]) {
        boolean loop = true;
        while(loop) {
            RegistrosLogger.log(1001, false); // Iniciado
            int selectedOption = 0;
            
            System.out.println(HEADER);

            RegistrosLogger.log(2001, false);
            if(user==null) user = newUserFromEmail();
            RegistrosLogger.log(2002, false);
            RegistrosLogger.log(3001, user.getEmail(), false);
            if(!userPassWord()) {
                user = null;
                continue;
            }
            RegistrosLogger.log(3002, user.getEmail(), false);
            RegistrosLogger.log(4001, user.getEmail(), false);
            if(!getPrivateKey()) {
                user = null;
                continue;
            }
            RegistrosLogger.log(4002, user.getEmail(), false);

            RegistrosLogger.log(4003, user.getLoginName(), true);
            String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
            String corpo1 = String.format(Console.CORPO1, user.getTotalDeAcessos());

            //RegistrosLogger.log(1002, true); // Finalizado
            
            selectedOption = 0;
            while(selectedOption != 4 && user!=null) {
                System.out.println(cabecalho);
                System.out.println(corpo1);
                RegistrosLogger.log(5001, user.getEmail(), false);
                if(user.isAdmin()){
                    System.out.println(MENUPRINCIPALADMIN);
                }
                else {
                    System.out.println(MENUPRINCIPALUSUARIO);
                }
                selectedOption = MyUtil.safeGetIntInput("");
                if (!user.isAdmin() && selectedOption != 0) {
                    selectedOption = selectedOption + 1;
                }
                switch(selectedOption) {
                    case 1:
                        RegistrosLogger.log(5002, user.getEmail(), false);
                        cadastrarUsuario(user);
                        break;
                    case 2:
                        RegistrosLogger.log(5003, user.getEmail(), false);
                        if(alterarSenhaPessoal()) {
                            if(!getPrivateKey()) {
                                user = null;
                                continue;
                            }
                        };
                        break;
                    case 3:
                        RegistrosLogger.log(5004, user.getEmail(), false);
                        consultarPastaDeArquivosSecretos();
                        break;
                    case 4:
                        RegistrosLogger.log(5005, user.getEmail(), false);
                        user = null;
                        loop = false;
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                }
            }

        }
        RegistrosLogger.log(1002, true);
    }
}