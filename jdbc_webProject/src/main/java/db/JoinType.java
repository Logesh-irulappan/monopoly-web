//$Id$
package db;

public enum JoinType {
	INNER_JOIN("INNER JOIN"),
	LEFT_JOIN("LEFT JOIN"),
	RIGHT_JOIN("RIGHT JOIN"),
	FULL_JOIN("FULL JOIN"),
	JOIN("JOIN");
	
	private String typeName;
	
	JoinType(String typeName) {
		this.typeName = typeName;
	}
	
	public String getType() {
		return typeName;
	}
}
