//$Id$
package Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jdbc_webProject.Monopoly;
import jdbc_webProject.Player;

public class Chance extends Place {
	
	public Chance(int id, String placeName) {
		super(id, placeName);
	}
	
	 public List<String> getChoices(Player player) {
			String[] commands = {"NO CHOICES, Enter ", "OK"};
			return new ArrayList<String>(Arrays.asList(commands));
		}
	  
	  public String details() {
		 return  "Arrived at Chance :)";
	  }

	  @Override
	  public String action(Player player) {
		  Monopoly monopoly = Monopoly.getInstance();
		  monopoly.moveBack();
	      return player.getName() + " , You got another chance \n";
	  }
}
