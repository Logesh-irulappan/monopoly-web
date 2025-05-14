package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Banker;
import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class Tax extends Place {

    private double taxValue;

    public Tax(int id, String name, double taxValue) {
        super(id, name);
        this.taxValue = taxValue;
    }
    
    public List<String> getChoices(Player player) {
    	String[] commands = {"PAY_TAX"};
		return new ArrayList<String>(Arrays.asList(commands));
	}
    
    public String details() {
    	return " TAX VALUE $" + taxValue;
    }

    @Override
    public String action(Player player) throws RequestNotCompletedException {
    	StringBuilder strResponse = new StringBuilder("");
    	
        if (player.getBalance() > taxValue) {
        	player.deductBalance(taxValue);
            Banker.credit(0, taxValue);
            strResponse.append(player.getName() + "\t Tax of $"+ taxValue + " is Deducted from your Account Balance! \n");
        }
        else {
        	throw new RequestNotCompletedException("", player.getName() + ", Insufficient Account balance to Pay Tax $"+ taxValue + " borrow from Bank. ");
        }
        return strResponse.toString();
    }

    public double getTaxValue() {
        return taxValue;
    }
}

