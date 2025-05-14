//$Id$
package Places;

import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class Charge extends Place {
	
	private double value;

	public Charge(int placeId, String placeName, double value) {
		super(placeId, placeName);
		this.value = value;
	}
	
	@Override // this action will be overriden by child classes which are extending the charge class
    public String action(Player player) throws RequestNotCompletedException {
		return "";
	}
	
	public double getChargeAmount() {
		return value;
	}
	
}
