package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Banker;
import jdbc_webProject.Player;

public class Go extends Place {

    public Go(int id, String name) {
        super(id, name);
    }
    
    public List<String> getChoices(Player player) {
    	String[] commands = {"NO CHOICES, Enter ", "OK"};
		return new ArrayList<String>(Arrays.asList(commands));
	}
    
    public String details() {
    	return "";
    }

    @Override
    public String action(Player player) {
    	StringBuilder strResponse = new StringBuilder("");
    	
    	Banker.credit(player.getId(), 20); // player creditted with 200
    	Banker.withdraw(0, 20); // Banker debitted with 200
    	
        strResponse.append(" Go, your balance is increased by $20 with GO");
        
        return strResponse.toString();
    }
}

