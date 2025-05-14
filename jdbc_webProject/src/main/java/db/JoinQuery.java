//$Id$
package db;

import java.util.List;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;

public class JoinQuery {
	
	private StringBuilder joinQuery;
	
	private String leftTable;
	private String rightTable;
	
	private String leftMappingColumn;
	private String rightMappingColumn;
	
	private JoinType joinType;
	
	private List<String> leftTableColumns;
	private List<String> rightTableColumns;
	private List<String> whereArguments;
	
	public JoinQuery() {
		joinQuery = new StringBuilder("SELECT ");
		leftTableColumns = new ArrayList<>();
		rightTableColumns = new ArrayList<>();
		whereArguments = new ArrayList<>();
	}
	
	private void makeQuery() {
		appendColumns();
		joinQuery.append(" FROM " + leftTable + " ");
		appendJoinType();
		joinQuery.append(rightTable +" ON ");
		joinQuery.append(leftTable + "." + leftMappingColumn +"=" + rightTable + "." + rightMappingColumn);
		appendWhereArgs();
		joinQuery.append(";");
	}
	
	public ResultSet execute() {
		makeQuery();
		try {
			Connection connection = DatabaseConnection.getConnection();
			Statement statement = connection.createStatement();
			
			ResultSet result = statement.executeQuery(joinQuery.toString());
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private void appendJoinType() {
		joinQuery.append(joinType.getType() + " ");
	}
	
	private void appendColumns() {
		if (leftTableColumns.size() > 0) {
			for (int i = 0; i < leftTableColumns.size()-1; i++) {
				String column = leftTableColumns.get(i);
				joinQuery.append(leftTable + "." + column + ", ");
			}
			joinQuery.append(leftTable + "." + leftTableColumns.get(leftTableColumns.size()-1));
		}
		
		if (rightTableColumns.size() > 0) {
			if (leftTableColumns.size() > 0)
				joinQuery.append(", ");
			joinQuery.append(rightTable + "." + rightTableColumns.get(0));
			for (int i = 1; i < rightTableColumns.size(); i++) {
				String column = rightTableColumns.get(i);
				joinQuery.append(", " + rightTable + "." + column);
			}
		}
	}
	
	private void appendWhereArgs() {
		if (whereArguments.size() > 0) {
			joinQuery.append(" WHERE");
			for (String arg : whereArguments) 
				joinQuery.append(" " + arg);
		}
	}
	
	public void addLeftWhereArguments(String key, String value) {
		String arg = leftTable + "." + key + "=" + value;
		whereArguments.add(arg);
	}
	
	public void addRightWhereArguments(String key, String value) {
		String arg = rightTable + "." + key + "=" + value;
		whereArguments.add(arg);
	}
	
	public void setLeftMappingColumn(String columnName) {
		leftMappingColumn = columnName;
	}
	
	public void setRightMappingColumn(String columnName) {
		rightMappingColumn = columnName;
	}
	
	public void setLeftTableName(String tableName) {
		leftTable = tableName;
	}
	
	public void setRightTableName(String tableName) {
		rightTable = tableName;
	}
	
	public void setJoinType(JoinType joinType) {
		this.joinType = joinType;
	}
	
	public void addLeftColumns(String columnName) {
		leftTableColumns.add(columnName);
	}
	
	public void addRightColumns(String columnName) {
		rightTableColumns.add(columnName);
	}
}
