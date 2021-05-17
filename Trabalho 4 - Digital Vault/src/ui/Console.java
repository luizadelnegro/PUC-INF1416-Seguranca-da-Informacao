package ui;

import java.nio.file.NoSuchFileException;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import controllers.RegistrosLogger;
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

        System.out.println("Senha: ");
        System.out.println("Confirmacao senha: ");
        if(!nu.saveToDb()) {
            System.out.println("Error!");
        }

    }

    public static void alterarSenhaPessoal() {
        String cabecalho = String.format(Console.CABECALHO, user.getLoginName(), user.getGroupName(), user.getName());
        String corpo1 = String.format(Console.CORPO1, user.getTotalDeAcessos());
    }

    public static void consultarPastaDeArquivosSecretos() {

    }

    public static void main(String args[]) {
        while(true) {
            //RegistrosLogger.log(1001, true); // Iniciado
            PrivateKeyHandler pkh = null;


            int selectedOption = 0;
            
            System.out.println(HEADER);

            PrivateKey privateK;

            //RegistrosLogger.log(2001, true); // Inicio aut 1
            while (user == null) {
                System.out.println("Insira seu email:");
                String userEmail = sc.nextLine();
                user = new User(userEmail);
                if (! user.isValid()) {
                    LOGGER.log(Level.SEVERE, "Wrong email! " + userEmail);
                    user = null;
                }
            }
            //RegistrosLogger.log(2002, true); // Fim aut 2

            //RegistrosLogger.log(3001, true); // Inicio aut 2
            // PhoneticKeyBoard keyBoard = new PhoneticKeyBoard();
            // while(selectedOption != 7) {
            //     System.out.println(keyBoard.getAsSingleString() + "7- END");
            //     selectedOption = sc.nextInt();
            //     if (selectedOption > 0 && selectedOption < 7) {
            //         keyBoard.pressGroup(selectedOption);
            //         keyBoard.randomizeKeys();
            //     }

            // }
            //RegistrosLogger.log(3002, true); // Fim aut 2

            //RegistrosLogger.log(4001, true); // Inicio aut 3
            // while (pkh == null){
            //     System.out.println("Insira o path para sua chave privada:");
            //     pkh = new PrivateKeyHandler(sc.nextLine());
            //     if(! pkh.isInitialized()) {
            //         pkh = null;
            //     }
            // }
            //RegistrosLogger.log(4002, true); // Fim aut 3

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
                        consultarPastaDeArquivosSecretos();
                        break;
                    case 4:
                        break;
                    default:
                        System.out.println("Opcao invalida!");
                }
            }

        }
    }
}
