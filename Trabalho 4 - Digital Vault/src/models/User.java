package models;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.*;
import java.security.*;
import java.math.BigInteger;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import controllers.MySqlController;

import models.PhoneticKeyBoard;

public class User {
    
    private String userEmail;
    
    private boolean validUser = false;
    private Integer groupId = null;
    private String groupName = null;
    private String name = null;
    private Integer totalAcessos = null;
    private Integer totalConsultas = null;

    private boolean isBlocked;

    public  ArrayList<ArrayList<String>> passwordPossibilities = new ArrayList(); //TODO 


    private static final Logger LOGGER = Logger.getLogger(User.class.getName());

    public User(String userEmail){
        this.userEmail = userEmail;
        this.isBlocked = false; //TODO REMOVER
        isEmailValid();
    }

    private void isEmailValid() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT * FROM Usuarios WHERE login_name = '" + userEmail + "';");
            this.validUser = results.next();
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            this.validUser = false;
        }
    }

    public boolean isBlocked(){
        return this.isBlocked;
    }

    public String getEmail(){
        return this.userEmail;
    }

    public boolean isValid() {
        return this.validUser;
    }

    private void updateGroupValues() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            String q = String.format("SELECT g.nome, g.gid FROM Usuarios u JOIN Grupos g ON g.gid = u.grupo WHERE login_name = '%s';", userEmail);
            ResultSet results = mysqlsobj.run_select_statement(q);
            results.next();
            groupName = results.getString(1);
            groupId = results.getInt(2);
        } catch (SQLException e) {
            e.printStackTrace();
            groupId = 2;
        }
    }

    public String getGroupName() {
        if(groupName == null) {
            updateGroupValues();
        }
        return groupName;
    }

    public boolean isAdmin() {
        if(groupId == null) {
            updateGroupValues();
        }
        return groupId == 1;
    }

    public String getName() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(name == null) {
            try {
                String q = String.format("SELECT unome FROM Usuarios WHERE login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                name = results.getString(1);
            } catch (SQLException e) {
                e.printStackTrace();
                name = "";
            }
        }
        return name;
    }

    public String getLoginName() {
        return userEmail;
    }

    public Integer getTotalDeAcessos() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(totalAcessos == null) {
            try {
                String q = String.format("SELECT count(*) FROM Registros WHERE mensagem_id = 4003 AND login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                totalAcessos = results.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                totalAcessos = 0;
            }
        }
        return totalAcessos;
    }

    public Integer getTotalUsuarios() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if (!isAdmin()) return 0;
        try {
            String q = String.format("SELECT count(*) FROM Usarios;");
            ResultSet results = mysqlsobj.run_select_statement(q);
            results.next();
            return results.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    } 

    public Integer getTotalConsultasDeAcessos() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        if(totalConsultas == null) {
            try {
                String q = String.format("SELECT count(*) FROM Registros WHERE mensagem_id = 5004 AND login_name = '%s';", userEmail);
                ResultSet results = mysqlsobj.run_select_statement(q);
                results.next();
                totalConsultas = results.getInt(1);
            } catch (SQLException e) {
                e.printStackTrace();
                totalConsultas = 0;
            }
        }
        return totalConsultas;
    }

    private  ArrayList<String> getPossibilities ( ArrayList<ArrayList<String>> selectedPassword){
        ArrayList<ArrayList<String>> passwordPossibilities = new ArrayList();
        int j;
        for( j = 0;j<(selectedPassword.size());j++){//cada linha da selected password
            ArrayList<String> currentRowOfSelectedPassword = new ArrayList<String>(selectedPassword.get(j));

            //System.out.println(" currentRowOfSelectedPassword: "+currentRowOfSelectedPassword);//OK
            //inicializa
            if(j==0){
                ArrayList<String> firstRow = selectedPassword.get(0);
                    for(int i=0;i<3;i++){
                        ArrayList<String> newRow = new ArrayList<String>();
                        String element = firstRow.get(i);
                        newRow.add(element);
                        passwordPossibilities.add(newRow);
                    }
                    //System.out.println(" PP Primeira linha: " + passwordPossibilities);//OK
            }//se ja inicializou
            else{
                ArrayList<ArrayList<String>> temp = new ArrayList();
                for (int l=0;l<3;l++){//  cada elemento da linha de selected password
                //para cada elemento da minha lista de escolhidos eu vou combinar com meu elemento corrente e criar uma nova lista de possibilidades
                    //pega o elemento da linha
                    String elementOfRowOfSelectedPasswod=currentRowOfSelectedPassword.get(l);
                    //System.out.println(" elementOfRowOfSelectedPasswod El que queremos fazer a combinacao: " + elementOfRowOfSelectedPasswod);//OK
                    //faz a combinacao com a possibilities passowrd, cada elemento d alista de possiblidades
                    ArrayList<ArrayList<String>> auxPasswordPossibilities = new ArrayList();
                    for(int k=0;k<passwordPossibilities.size();k++){
                        ArrayList<String> newCombinationPossibilities = new ArrayList(passwordPossibilities.get(k));
                        newCombinationPossibilities.add(elementOfRowOfSelectedPasswod);
                        //System.out.println("Combinacao que tem q ser add"+newCombinationPossibilities);
                        auxPasswordPossibilities.add(newCombinationPossibilities);//acho q tem q ser do lado de fora!!!!
                        //System.out.println("Auxiliar com as possibilidades"+auxPasswordPossibilities);
                    }       
                    ArrayList<ArrayList<String>> aux = new ArrayList(auxPasswordPossibilities);
                    for(int a=0;a<aux.size();a++){
                        temp.add(aux.get(a));
                    }                   
                }
                passwordPossibilities=temp;
            }
        }
        
        System.out.println("size: "+passwordPossibilities.size());

//transforma lista de arrays em lista de lista
        ArrayList<String> resultPossibilities = new ArrayList();
        for(int i=0;i<passwordPossibilities.size();i++){
            ArrayList<String> array=passwordPossibilities.get(i);
            String finalString= String.join("",array);
            resultPossibilities.add(finalString);
        }

        return resultPossibilities;
    }



    private String getPassword() {
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT hash FROM Usuarios WHERE login_name = '" + userEmail + "'");
            this.validUser = results.next();
            return results.getString(1);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            this.validUser = false;
            return "errou";
        }
    }


    public boolean isPasswordValid(  ArrayList<ArrayList<String>> password) throws NoSuchAlgorithmException, UnsupportedEncodingException{//TODO mudar para private
        //senha menor que 4 elementos e maior que 6 é sempre inválida
        int length=password.size();
        if (length<4 || length>6){
            return false;
        }
        //Create Array of Possibilities
        ArrayList<String> pp= new  ArrayList<String>(getPossibilities(password));
        // check array of possibilities
        //get user password
        String pass=getPassword();//senha do usuario
        //PARA TESTE ATE JUNTAR hasheia essa senha
        String salt=getSalt();
       // String hashCorrectPass=generateHashedPassword(pass,salt);//PAREI AQUI


        //pega o salt
        //adiciona o salt a cada elemento
        //hasheia e ve se eh igual
        if(pp.contains(pass)){
            System.out.println("YASS");
            return true;
        }else{
            System.out.println("NOOOS");
            return false;
        }
    }


    public boolean getIsPasswordValid( ArrayList<ArrayList<String>> password) throws NoSuchAlgorithmException, UnsupportedEncodingException{
        return this.isPasswordValid(password);
    }

    public String getSalt(){
        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT salt FROM Usuarios WHERE login_name = '" + userEmail + "'");
            this.validUser = results.next();
            return results.getString(1);
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, e.getStackTrace().toString() + " " + e.getSQLState() + " " + e.getMessage());
            this.validUser = false;
            return "errou";
        }
    }

    public String generateSalt()throws NoSuchAlgorithmException, UnsupportedEncodingException{
        String charOptions = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < 10) { 
            int index = (int) (random.nextFloat() * charOptions.length());
            salt.append(charOptions.charAt(index));
        }
        String saltString = salt.toString();
        return saltString;
    }

    public String generateHashedPassword(String  password, String salt)throws NoSuchAlgorithmException, UnsupportedEncodingException {
        String newPassword=password+salt;
        try {
            MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            String digest = toHex(sha1.digest(newPassword.getBytes("UTF-8")));
            return digest;
            
        } catch (NoSuchAlgorithmException exception) {
            throw exception;
        } catch (UnsupportedEncodingException exception) {
            throw exception;
        }

    }
    public static String toHex(byte[] bytes){
        return String.format("%032x", new BigInteger(1, bytes));
    }


}
