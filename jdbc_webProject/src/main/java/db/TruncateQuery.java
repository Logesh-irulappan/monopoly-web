//$Id$
package db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TruncateQuery extends SqlQuery {
	private StringBuilder truncateQuery;
	
	public TruncateQuery(String tableName) {
		super(tableName);
		
		truncateQuery = new StringBuilder("TRUNCATE ");
	}
	
	private void makeQuery() {
		truncateQuery.append(getTableName() + ";");
//		System.out.println(truncateQuery.toString());
	}
	
	public Boolean execute() {
		makeQuery();
		try {
			Connection connection = DatabaseConnection.getConnection();
			
			Statement statement = connection.createStatement();
			return statement.execute(truncateQuery.toString());
		}
		catch (SQLException e) {
			System.out.println(e.toString());
		}
		return false;
	}
}
