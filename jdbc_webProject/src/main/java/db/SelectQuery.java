//$Id$
package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.InputMismatchException;

public class SelectQuery extends SqlQuery {

	private StringBuilder selectQuery;
	
	private List<String> columns;
	private Map<String, String> whereArgs;
	
	private List<String> conditionList;
	
	private String orderByColumn = "";
	private String orderByType = "";
	
	public SelectQuery(String tableName) {
		super(tableName);
		selectQuery = new StringBuilder("SELECT ");
		
		columns = new ArrayList<>();
		whereArgs = new HashMap<>();
		conditionList = new ArrayList<>();
	}
	
	public void addColumn(String column) {
		columns.add(column);	
	}
	
	public void addWhereArguments(String key, String value) {
		whereArgs.put(key, value);
	}
	
	public void addAnd(int index) {
		conditionList.add(index-1, "AND");
	}
	
	public void addOr(int index) {
		conditionList.add(index-1, "OR");
	}
	
	public void setOrderBy(String columnName, String type) {
		if (!type.equals("DESC") && !type.equals("ASC"))
			throw new InputMismatchException("Given ORDER BY type is mismatching with ASC & DESC");
		orderByType = type;
		orderByColumn = columnName;
	} 
	
	private void appendColumns() {
		if (columns.size() == 0) 
			columns.add("*");
		
		int i = 0;
		for (; i < columns.size()-1; i++) 
			selectQuery.append(columns.get(i)+", ");
		
		selectQuery.append(columns.get(i)+" FROM "+ getTableName());
	}
	
	private void appendWhereArgs() {
		selectQuery.append(" WHERE ");
		for (Map.Entry<String, String> entry : whereArgs.entrySet()) {
			String currentValue = entry.getValue();
			
			Object object = findDataType(currentValue);
			String value = object.getClass().getName().substring(10);
			
			if (value.equals("String")) {
				currentValue = "'" + currentValue + "'";
			}
			selectQuery.append(" "+ entry.getKey() + " = " +currentValue);
			
			if (conditionList.size() > 0) {
				selectQuery.append(" " + conditionList.get(0));
				conditionList.remove(0);
			}
		}
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
	
	private String makeQuery() {
		appendColumns();
		if (whereArgs.size() > 0) 
			appendWhereArgs();
		
		if (orderByType.length() > 0) 
			selectQuery.append(" ORDER BY " + orderByColumn + " " + orderByType);
		
		selectQuery.append(";");
		
//		System.out.println(selectQuery.toString());
		return selectQuery.toString();
	}
	
	public ResultSet execute() {
		makeQuery();
		
		try {
			Connection connection = DatabaseConnection.getConnection();
			
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(selectQuery.toString());
			
			return resultSet;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Empty set \n");
		return null;
	}
	
}
