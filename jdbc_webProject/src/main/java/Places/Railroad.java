package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Banker;
import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class Railroad extends Place {

    private double rentValue;

    public Railroad(int name, String id, double rentValue) {
        super(name, id);
        this.rentValue = rentValue;
    }
    
    public List<String> getChoices(Player player) {
    	String[] commands = {"PAY_RENT"};
		return new ArrayList<String>(Arrays.asList(commands));
	}
    
    public String details() {
    	return " RAILROAD RENT $" + rentValue + " \n ";
    }

    @Override
    public String action(Player player) throws RequestNotCompletedException {
    	StringBuilder strResponse = new StringBuilder("");
        if (player.getBalance() > rentValue) {
          strResponse.append("  Railroad rent $" + rentValue + " is collected from you! " + " ");
          
          player.deductBalance(rentValue); // player is debitted with rent
          Banker.credit(0, rentValue); // banker is credited with rent
        }
        else {
          throw new RequestNotCompletedException(player.getName() + ", Insufficient Account balance to Pay RoadRail Rent $" + rentValue + " borrow from Bank. "); 
        }
        return strResponse.toString();
    }

    public void setRentValue(double rentValue) {
        this.rentValue = rentValue;
    }

    public double getRentValue() {
        return rentValue;
    }

}

