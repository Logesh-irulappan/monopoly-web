package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.MonopolyQueries;
import jdbc_webProject.Player;
import jdbc_webProject.Exceptions.RequestNotCompletedException;

public class Place {
    private String name;
    private int id;

    public Place(int id, String name) {
        this.name = name;
        this.id = id;
    }
    
    public List<String> getChoices(Player player) {
    	String[] commands = {"NO CHOICES, Enter ", "OK"};
		return new ArrayList<String>(Arrays.asList(commands));
    }
    	
    // this action is overrides by the child classes
    public String action(Player player) throws RequestNotCompletedException {
    	return "";
    }
    
    public String details() {
    	return "";
    }
    
    public String buyHouse(Player player) throws RequestNotCompletedException {
    	return null;
    }
    public String buyProperty(Player player) throws RequestNotCompletedException {
    	return null;
    }
    public String payPropertyRent(Player player) throws RequestNotCompletedException {
    	return null;
    }
    
    public int getPlaceId() {
        return id;
    }

    public String getPlaceName() {
        return name;
    }
    
    public int getPlaceOwnerId(int placeId) {
    	return MonopolyQueries.getPlaceOwnerId(placeId);
    }
}


