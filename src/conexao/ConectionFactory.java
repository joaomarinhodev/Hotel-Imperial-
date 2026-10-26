/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author User
 */
public class ConectionFactory {
    public static Connection conector(){
        java.sql.Connection conexao = null;
        String driver = "org.postgresql.Driver"; 
        String url = "jdbc:postgresql://localhost:5432/hotelImperial";
        String user = "postgres";
        String senha = "postgres";
        
        try {
            Class.forName(driver);
            conexao = DriverManager.getConnection(url, user, senha);
            return conexao;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}
