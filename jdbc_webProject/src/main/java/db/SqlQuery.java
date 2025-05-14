package db;
public abstract class SqlQuery {
	private String tableName;
	
	public SqlQuery(String tableName) {
		this.tableName = tableName;
	}
	
	public String getTableName() {
		return tableName;
	}
	
	public abstract Object execute();
}