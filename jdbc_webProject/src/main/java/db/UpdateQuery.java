//$Id$
package db;

import java.util.List;
import java.util.ArrayList;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;

public class UpdateQuery extends SqlQuery {
	
	private StringBuilder updateQuery;	
	private List<String> updates;
	private List<String> conditions;
	private List<String> operators;
	
	public UpdateQuery(String tableName) {
		super(tableName);
		updates = new ArrayList<>();
		operators = new ArrayList<>();
		conditions = new ArrayList<>();
		updateQuery = new StringBuilder("UPDATE " + tableName +" ");
	}
	
	private void makeQuery() {
		updateQuery.append("SET ");
		appendUpdates();
		updateQuery.append("WHERE ");
		appendWhereArgs();
		updateQuery.append(";");
	}
	
	private void appendWhereArgs() {
		if (conditions.size() > 0) {
			updateQuery.append(conditions.get(0) + " ");
			for (int i = 1; i < conditions.size(); i++) {
				if (operators.size() > 0) {
					updateQuery.append(" "+ operators.get(0) +" "+ conditions.get(i));
					operators.remove(0);
				}
				else {
					updateQuery.append(" "+ conditions.get(i));
				}
			}
		}
	}
	
	private void appendUpdates() {
		if (updates.size() > 0) {
			updateQuery.append(updates.get(0) + " ");
			for (int i = 1; i < updates.size(); i++) 
				updateQuery.append(", " + updates.get(i) + " ");
		}
		else 
			throw new IllegalStateException("Error in given params");
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
	
	
	public Boolean execute() {
		makeQuery();
//		System.out.println(updateQuery.toString());
		try {
			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement();
			
			return statement.execute(updateQuery.toString());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void addConditionOperator(int index, String operator) {
		operators.add(index-1, operator);
	}
	
	public void addConditions(String key, String value) {
		Object object = findDataType(value);
		String type = object.getClass().getName().substring(10);
		
		if (type.equals("String")) {
			value = "'" + value + "'";
		}
		String condition = " " + key + " = " + value;
		conditions.add(condition);
	}
	
	public void addUpdates(String key, String value) {
		Object object = findDataType(value);
		String type = object.getClass().getName().substring(10);
		
		if (type.equals("String")) {
			value = "'" + value + "'";
		}
		String update = " " + key + " = " + value;
		updates.add(update);
	}
}
