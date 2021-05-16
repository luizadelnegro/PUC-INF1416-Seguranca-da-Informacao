package controllers;


import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import controllers.MySqlController;

public final class RegistrosLogger {

    private Integer idMensagem;
    private String login_name;
    private String arq_name;

    private static final Logger LOGGER = Logger.getLogger(RegistrosLogger.class.getName());

    private void sendToDb() throws SQLException {
        String sqlStatement = "INSERT INTO Registros(mensagem_id, login_name, arq_name) VALUES (" + Integer.toString(idMensagem) +  ", '";
        if (login_name != null) {
            sqlStatement = sqlStatement + login_name;
        }
        sqlStatement = sqlStatement + "', '";
        if (arq_name != null) {
            sqlStatement = sqlStatement + arq_name;
        }
        sqlStatement = sqlStatement + "');";
        Integer rows = MySqlController.getInstance().run_insert_statement(sqlStatement);
    }

    private RegistrosLogger(Integer idMensagem, String loginName, String arqName){
        this.idMensagem = idMensagem;
        this.login_name = loginName;
        this.arq_name = arqName;
    }

    public static void log(Integer idMensagem, String loginName, String arq_name, boolean PrintToUser) {
        String mensagem = "";

        RegistrosLogger rl = new RegistrosLogger(idMensagem, loginName, arq_name);
        try {
            rl.sendToDb();
            ResultSet messageRS = MySqlController.getInstance().run_select_statement("SELECT mensagem FROM Mensagens WHERE id = " + Integer.toString(idMensagem) + ";");
            messageRS.next();
            mensagem = messageRS.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }


        if (PrintToUser) {
            LOGGER.log(Level.INFO, mensagem.replace("<login_name>", loginName).replace("<arq_name>", arq_name));
        }
    }

    public static void log(Integer idMensagem, boolean printToUser) {
        log(idMensagem, "", "", printToUser);
    }

    public static void log(Integer idMensagem, String loginName, boolean printToUser) {
        log(idMensagem, loginName, "", printToUser);
    }

    
}
