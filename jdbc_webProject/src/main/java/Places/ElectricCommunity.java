//$Id$
package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Banker;
import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class ElectricCommunity extends Charge { 
	
	public ElectricCommunity(int placeId, String placeName, double value) {
		super(placeId, placeName, value);
	}
	
	public List<String> getChoices(Player player) {
		String[] commands = {"PAY_BILL"};
		return new ArrayList<String>(Arrays.asList(commands));
	}
	
	public String details() {
		return "ELECTRIC COMMUNITY CHARGE $" + getChargeAmount();
	}
	
	public String action(Player player) throws RequestNotCompletedException {
		double billingAmount = getChargeAmount();
		
		if(player.getBalance() >= billingAmount) {
			player.deductBalance(billingAmount);
			Banker.credit(0, billingAmount);
			
			return "ELECTRIC COMMUNITY CHARGE $" + billingAmount +" IS PAID. ";
		}
		else {
			throw new RequestNotCompletedException("Insufficient Balance in your Account. "+ 
					" Borrow from Bank and pay the Electric community billing amount of $" + billingAmount + ". ");
		}
	}
}
