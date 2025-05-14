//$Id$
package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;

public class InsertQuery extends SqlQuery {
	private StringBuilder insertQuery;
	
	private String tableName;
	
	private List<String> columns;
	private List<Values> values;
	
	public InsertQuery(String tableName) {
		super(tableName);	
		insertQuery = new StringBuilder("INSERT INTO ");
		this.tableName = tableName;
		columns = new ArrayList<>();
		values = new ArrayList<>();
	}
	
	public void addColumn(String column) {
		columns.add(column);
	}
	
	private void appendColumns() {
		if (columns.size() > 0) {
			insertQuery.append("(");
			int index = 0;
			for (index = 0; index < columns.size()-1; index++) {
				insertQuery.append(columns.get(index) + ", ");
			}
			insertQuery.append(columns.get(index)+") ");
		}
	}
	
	private String makeQuery() {
		insertQuery.append(tableName +" ");
		appendColumns();
		
		if (values.size() > 0) {
			insertQuery.append("VALUES(");
			for (int i = 0; i < values.size()-1; i++) {
				insertQuery.append("?, ");
			}
			insertQuery.append("?);");
		}
		else {
			throw new IllegalStateException("Values params are must to be given in insert query");
		}

		return insertQuery.toString();
	}
	
	public Boolean execute() {
		makeQuery();
		
		try {
			int listSize = values.get(0).getSize();
			int index = values.size();
			
			Connection connection = DatabaseConnection.getConnection();
			
//			System.out.println(insertQuery.toString());
			PreparedStatement statement = connection.prepareStatement(insertQuery.toString());
			
			for (int i = 0; i < listSize; i++) {
				for (int j = 1; j <= index; j++) {
					List<Object> objects = values.get(j-1).getObjectList();
					
					String str;
					int integerNumber;
					double doubleNumber;
					float floatNumber;
					long longNumber;
					
					String condition = objects.get(i).getClass().getName().substring(10);
					switch (condition) {
						case "String": {
							str = (String) objects.get(i);
							statement.setString(j, str);
							break;
						}
						case "Integer": {
							integerNumber = (int) objects.get(i);
							statement.setInt(j, integerNumber);
							break;
						}
						case "Double": {
							doubleNumber = (double) objects.get(i);
							statement.setDouble(j, doubleNumber);
							break;
						}
						case "Float": {
							floatNumber = (float) objects.get(i);
							statement.setFloat(j, floatNumber);
							break;
						}
						case "Long": {
							longNumber = (long) objects.get(i);
							statement.setLong(j, longNumber);
							break;
						}
						default : {
							throw new InputMismatchException("Given datatype is not supported");
						}
					}
				}
				statement.addBatch();
				statement.execute();
			}
//			System.out.println("Inserted (" + listSize + ") rows.");
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setDouble(int index, String data) {
		Double newData = Double.valueOf(data);
		if (values.size() >= index) {
			Values value = values.get(index-1);
			value.addObject(newData);
		}
		else {
			Values value = new Values();
			value.setIndex(index);
			value.addObject(newData);
			
			values.add(value);
		}
	}
	
	public void setFloat(int index, String data) {
		Float newData = Float.valueOf(data);
		if (values.size() >= index) {
			Values value = values.get(index-1);
			value.addObject(newData);
		}
		else {
			Values value = new Values();
			value.setIndex(index);
			value.addObject(newData);
			
			values.add(value);
		}
	}
	
	public void setInt(int index, String data) {
		Integer newData = Integer.valueOf(data);
		if (values.size() >= index) {
			Values value = values.get(index-1);
			value.addObject(newData);
		}
		else {
			Values value = new Values();
			value.setIndex(index);
			value.addObject(newData);
			
			values.add(value);
		}
	}
	
	public void setString(int index, String data) {
		if (values.size() >= index) {
			Values value = values.get(index-1);
			value.addObject(data);
		}
		else {
			Values value = new Values();
			value.setIndex(index);
			value.addObject(data);
			
			values.add(value);
		}
	}
	

}
