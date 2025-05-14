//$Id$
package Places;

public enum PlaceType {
	CHARGE("CHARGE"),
	PLACE("PLACE"), 
	PROPERTY("PROPERTY");
	
	private String belongsToTable;
	
	PlaceType(String belongsToTable) {
		this.belongsToTable = belongsToTable;
	}
	
	public String getTypeName() {
		return belongsToTable;
	}
}
