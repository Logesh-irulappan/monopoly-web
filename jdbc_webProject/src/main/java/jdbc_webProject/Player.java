package jdbc_webProject;

import java.util.List;

public class Player {
//    private String name;
    private int id;
    private String name;
    
    private int currentPosition;
    private boolean jailStatus;
    
    public Player(String name, int id) {
        this.id = id;
        this.name = name;
        currentPosition = 0;
        jailStatus = false;
    }
    
    @Override
    public String toString() {
    	return "PlayerName: " + name + "\t PlayerId: " + id;
    }
    
    public int getPosition() {
    	return currentPosition;
    }
    
    public void setPosition(int index) {
    	currentPosition = index;
    }

    public double getBalance() {
        return Banker.getBalance(id);
    }

    public void deductBalance(double amount) {
    	Banker.withdraw(id, amount);
    }

    public void addBalance(double amount) {
        Banker.credit(id, amount);
    }
    
    public void setJailStatus(boolean status) {
    	jailStatus = status;
    }
    
    public boolean getJailStatus() {
    	return jailStatus;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

	public List<String> getOwnedPlaces() {
		return MonopolyQueries.getOwnedPlaces(id);
	}
}


