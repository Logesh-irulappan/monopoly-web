package Places;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import jdbc_webProject.Banker;
import jdbc_webProject.MonopolyQueries;
import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class Property extends Place {

//    private double sellValue;
//    private double rentValue;
//    private int totalProperties; 
	
	// Only have ID remove all other list and variables...
	
	Player placeOwner;

    public Property(int id, String name, double rentValue, double sellValue, int totalProperties) {
        super(id, name);
//        this.sellValue = sellValue;
//        this.rentValue = rentValue;
//        this.totalProperties = totalProperties;
    }
    
    public List<String> getChoices(Player player) {
    	List<String> commands = new ArrayList<>();
    	
    	if (isOwned() && player.getId() == placeOwner.getId()) {
    		commands.add("BUY_HOUSES");
    		commands.add("SKIP");
    	}
    	else if(isOwned()) {
    		commands.add("PAY_PROPERTY_RENT");
    	}
    	else {
    		commands.add("BUY_PROPERTY");
    		commands.add("SKIP");
    	}
    	return commands;
    }

    public String details() {
      String info = " SELLING VALUE: " + getSellValue() + " \n RENT VALUE: "
    		  		+ getRentValue() + " \n TOTAL HOUSES: " + getTotalHouses();
      return info;
    }
    
    // totally three actions are here...
    
    public String payPropertyRent(Player player) throws RequestNotCompletedException {
    	double rentValue = getRentValue();
    	if (player.getBalance() >= rentValue) {
    		player.deductBalance(rentValue);
    		placeOwner.addBalance(rentValue);
    		return player.getName() + ", rent value $" + rentValue + " deducted. ";
    	}
    	else {
    		throw new RequestNotCompletedException("", "Insufficient Account Balance to Pay rent $" + rentValue + " ");
    	}
    }
    
    public String buyHouse(Player player) throws RequestNotCompletedException {
    	int preAvailedHouses = MonopolyQueries.getAvailedHouses(getPlaceId());
    	if(getTotalHouses() <= preAvailedHouses) {
    		return " Owner, you already bought All the houses in this place. Rent is Max Now. ";
    	}
    	
    	double houseValue = getSellValue() / getTotalHouses();
    		
    	if (player.getBalance() >= houseValue) {
    		player.deductBalance(houseValue);
    		Banker.credit(0, houseValue); // for banker
    		
    		MonopolyQueries.setAvailedHouses(getPlaceId(), preAvailedHouses+1);
    		MonopolyQueries.increaseHouseRent(getPlaceId());
    		return " House purchase successful, Property rent value is doubled. ";
    	}
    	else {
    		throw new RequestNotCompletedException("", " Registration cancelled, due to insufficient balance ");
    	}
    }
    
    public String buyProperty(Player player) throws RequestNotCompletedException {
    	double sellValue = getSellValue();
    	if (player.getBalance() >= sellValue) {
    		player.deductBalance(sellValue);
    		Banker.credit(0, sellValue); // for banker
    		setPlaceOwner(player);
    		return player.getName() + ", registration successful" + ". Property value $" + sellValue + " deducted. ";
    	}
    	else {
    		throw new RequestNotCompletedException("", " Registration cancelled, due to insufficient balance ");
    	}
    }

    public boolean isOwned() {
        return placeOwner != null;
    }

    public void setPlaceOwner(Player player) {
        this.placeOwner = player; 
        MonopolyQueries.setPlaceOwner(player.getId(), getPlaceId());
    }

    public Player getPlaceOwner() {
        return placeOwner;
    }

    public double getRentValue() {
        return MonopolyQueries.getRentValue(getPlaceId());
    }

    public double getSellValue() {
        return MonopolyQueries.getSellValue(getPlaceId());
    }
    
    public int getTotalHouses() {
    	return MonopolyQueries.getTotalHouses(getPlaceId());
    }
}

