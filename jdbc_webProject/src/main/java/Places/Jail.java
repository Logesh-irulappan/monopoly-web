package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Player;

public class Jail extends Place {
	
  public Jail(int id, String name) {
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
	  player.setJailStatus(true);
      return player.getName() + " IN Jail, No action \n";
  }

}
