package ui;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import controllers.MySqlController;
import java.util.logging.Level;
import java.util.logging.Logger;




public class ViewLogs {


    public static void main(String args[]) {

        MySqlController mysqlsobj = MySqlController.getInstance();
        try {
            ResultSet results = mysqlsobj.run_select_statement("SELECT r.dttime, m.mensagem, r.login_name, r.arq_name FROM Registros r Join Mensagens m on r.mensagem_id = m.id");
 
            while(results.next()){
                if(results.getString(3)!=""){//se tiver nome
                    System.out.println("Data e Hora: "+ results.getString(1)+ " - Mensagem: "+ results.getString(2)+ " - Login Name: "+ results.getString(3)+ " - Nome do arquivo: "+ results.getString(4));
                }else{// se nao tiver loginname
                    System.out.println("Data e Hora: "+ results.getString(1)+ " - Mensagem: "+ results.getString(2)+" - Nome do arquivo: "+ results.getString(4));
                }
                

            }
           
            
        } catch (SQLException e) {
            System.out.println("Erro");
            
        }
    }
}