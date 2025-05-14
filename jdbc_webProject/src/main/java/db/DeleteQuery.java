//$Id$
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeleteQuery extends SqlQuery {
	
	private StringBuilder deleteQuery;
	
	private Map<String, String> whereArgs;
	private List<String> conditionList;
	
	public DeleteQuery(String tableName) {
		super(tableName);
		deleteQuery = new  StringBuilder("DELETE ");

		whereArgs = new HashMap<>();
		conditionList = new ArrayList<>();
	}
	
	public void addWhereArguments(String key, String value) {
		whereArgs.put(key, value);
	}
	
	private void appendWhereArgs() {
		deleteQuery.append(" WHERE ");
		
		for (Map.Entry<String, String> entry : whereArgs.entrySet()) {
			
			String currentValue = entry.getValue();
			Object object = findDataType(currentValue);
			
			String value = object.getClass().getName().substring(10);
			if (value.equals("String")) 
				currentValue = "'" + currentValue + "'";
			
			deleteQuery.append(" "+ entry.getKey() + " = " +currentValue);
			
			if (conditionList.size() > 0) {
				deleteQuery.append(" " + conditionList.get(0));
				conditionList.remove(0);
			}
		}
	}
	
	public void addAnd(int index) {
		conditionList.add(index-1, "AND");
	}
	
	public void addOr(int index) {
		conditionList.add(index-1, "OR");
	}
	
	private Object findDataType(String content) {
		try {
			int val = Integer.valueOf(content);
			return val;
		}
		catch (Exception e) {
			return content;
		}
	}
	
	private void makeQuery() {
		deleteQuery.append(" FROM " + getTableName());
		
		if (whereArgs.size() > 0) 
			appendWhereArgs();
		
		deleteQuery.append(";");
	}
	
	public Integer execute() {
		makeQuery();
		System.out.println(deleteQuery.toString());
		
		try {
			Connection connection = DatabaseConnection.getConnection();
			PreparedStatement statement = connection.prepareStatement(deleteQuery.toString());
			
			int result = statement.executeUpdate();
			
			System.out.println(result + " rows affected. ");
			return Integer.valueOf(result);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Empty set \n");
		return 0;
	}
	
}



















