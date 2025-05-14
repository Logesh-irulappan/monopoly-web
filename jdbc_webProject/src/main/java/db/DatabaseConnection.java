//$Id$
package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

	public static Connection getConnection() {
		String connectionUrl = "jdbc:mysql://localhost:3306/mydb";
		String username = "root";
		String password = "";
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			try {
				Connection dbConnection = DriverManager.getConnection(connectionUrl, username, password);
//				System.out.println("web project database connection is connected successfully....");
				return dbConnection;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
