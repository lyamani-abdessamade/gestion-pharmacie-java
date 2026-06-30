package dao;

import java.sql.Connection;
import java.sql.DriverManager;



public class Utilitaire {
	private static Connection conn;

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            String url = "jdbc:mysql://localhost:3306/gestion_pharmacie";
            conn = DriverManager.getConnection(url, "root", "h1a2j3a4r5");

        } catch (Exception e) {
            System.out.println("Erreur connexion : " + e);
        }
    }
      
    public static Connection getConnection() {
        return conn;
    }
    
   
}
