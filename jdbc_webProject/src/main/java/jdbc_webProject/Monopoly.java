//$Id$
package jdbc_webProject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Places.Place;
import jdbc_webProject.Exceptions.AlreadyExistsException;
import jdbc_webProject.Exceptions.RequestNotCompletedException;
 
public class Monopoly {
	private List<Player> playerList;
	private List<String> playerNames;
    private List<Place> board;
    
    private Banker banker;
    private MonopolyQueries queries;
    private Dice dice; 
    
    private boolean reqStatus; // for servlet request status 
    
    private int playerCount;
    private int currentPlayerIndex;
    
    public static Monopoly instance;
    
    public static Monopoly getInstance() {
    	if (instance == null) {
    		instance = new Monopoly();
    	}
    	return instance;
    }

    private Monopoly() {
        playerList = new ArrayList<>();
        playerNames = new ArrayList<>();
        board = new ArrayList<>();
        banker = Banker.getInstance();
        queries = new MonopolyQueries();
        
        playerCount = 0;
        currentPlayerIndex = 0;
        reqStatus = true;
        
        dice = new Dice(6);
        banker.setInitialBankBalance(2000);
        
        makeDynamicBoard();
    }
    
    public boolean checkForPlayer(String playerName) {
    	for (String name : playerNames) {
    		if(playerName.equals(name))
    			return true;
    	}
    	return false;
    }
    
    public String createPlayer(String playerName) throws AlreadyExistsException {
		if (checkForPlayer(playerName.toLowerCase())) {
			throw new AlreadyExistsException("Player Already Exists in the Lobby");
		}
		playerCount++;
		playerNames.add(playerName.toLowerCase());
		if (queries.isPlayerExists(playerName)) {
			Player player = queries.getPlayerByName(playerName);
			queries.createBankAccount(playerName, player.getId(), 200);
			playerList.add(player);	
			return "Player Already exists, added to the game.";	
		}
		else {
		    queries.addPlayer(playerName, 200);		    
    		Player player = queries.getPlayerByName(playerName);
    		playerList.add(player);
    		return "New Player Created, added to the game.";
	    }
    }
    
    private void clearGameTables() {
    	String[] tables = {"BANKER", "PLACE", "PROPERTY", "CHARGES", "OWNEDPLACES"}; // single game usage tables..
    	queries.truncateTables(tables);
    }
    
    public int rollTheDice() {
    	return dice.roll();
    }
    
    public void movePlayer() {
    	currentPlayerIndex++;
		if (currentPlayerIndex == playerList.size()) 
			currentPlayerIndex = 0;
    }
    
    // used by chance logic...
    public void moveBack() {
    	currentPlayerIndex--;
    }
    
    public String makeAction(String command) throws RequestNotCompletedException {
    	Player player = playerList.get(currentPlayerIndex);
    	StringBuilder strResult = new StringBuilder();

		switch(command) {
			case "PAY_RENT": {
				try {
					strResult.append(board.get(player.getPosition()).action(player));
				} catch (RequestNotCompletedException e) {
					throw new RequestNotCompletedException("", e.getDetails());
				}
				break;
			}
			case "PAY_TAX": {
				try {
					strResult.append(board.get(player.getPosition()).action(player));
				} catch (RequestNotCompletedException e) {
					throw new RequestNotCompletedException("", e.getDetails());
				}
				break;
			}
			case "PAY_PROPERTY_RENT": {
				try {
					strResult.append(board.get(player.getPosition()).payPropertyRent(player));
				} catch(RequestNotCompletedException e) {
					throw new RequestNotCompletedException("", e.getDetails());
				}
				break;
			}
			case "BUY_PROPERTY": {
				try {
					strResult.append(board.get(player.getPosition()).buyProperty(player));
				} catch(RequestNotCompletedException e) {
					throw new RequestNotCompletedException("", e.getDetails());
				}
				break;
			}
			case "BUY_HOUSES": {
				try {
					strResult.append(board.get(player.getPosition()).buyHouse(player));
				} catch(RequestNotCompletedException e) {
					throw new RequestNotCompletedException("", e.getDetails());
				}
				break;
			}
			case "OK": {
				strResult.append(board.get(player.getPosition()).action(player));
				break;
			}
			case "SKIP": {
				strResult.append(" This turn skipped... ");
				break;
			}
			default: {
				strResult.append(" The given option is Invalid ");
				break;
			}
		}
    	
    	return strResult.toString();
    }
    
    public List<String> getPlayerData() {
    	List<String> playerData = new ArrayList<>();
    	
    	Player player = playerList.get(currentPlayerIndex);
    	
    	playerData.add(player.getName()); // [0] name;
    	playerData.add(String.valueOf(player.getBalance())); // [1] balance;
    	playerData.add(String.valueOf(Banker.getBorrowedAmount(player.getId()))); // [2] borrowedAmount

    	return playerData;
    }
    
    public List<String> getCommands() {
    	Player curPlayer = playerList.get(currentPlayerIndex);
    	return board.get(curPlayer.getPosition()).getChoices(curPlayer);
    }
    
   
    public String gameLogic() {
    	StringBuilder strResponse = new StringBuilder();
    	
    	Player currentPlayer = playerList.get(currentPlayerIndex);
		
		int stepsToMove = rollTheDice();
		
		strResponse.append(" DICE "+ stepsToMove);

		int newPosition = currentPlayer.getPosition() + stepsToMove;
		if (newPosition >= board.size()) {
			newPosition %= board.size();
		}
		currentPlayer.setPosition(newPosition);
		
		strResponse.append(" NEW POSITION: "+ newPosition +", ");
		strResponse.append(" PLACE: " + board.get(newPosition).getPlaceName() + ", " );
		strResponse.append(board.get(newPosition).details());
		
		return strResponse.toString();
    }
    
    private void makeDynamicBoard() {
      try {
        String filePath = "/home/logesh-tt0832/jdbc_webProject/src/main/webapp/WEB-INF/data/places.txt";
        File file = new File(filePath);
		Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
          String placeData = scanner.nextLine();
          if (placeData.isEmpty()) 
            break;
          String[] splittedData = placeData.split(",");
          if (splittedData.length > 0) {
            Place place = null;
            switch(splittedData[0]) {
              case "PLACE": { 
                place = queries.createPlace(splittedData);
                break;
              }
              case "PROPERTY": { 
                place = queries.createProperty(splittedData);
                break;
              }
              case "CHARGE": { 
                place = queries.createChargePlace(splittedData); 
                break;
              }
              default: {
                System.out.println("A unknown Place value has been Entered");
                break;
              }
            } 
            board.add(place);
          }
        }
      }
      catch (FileNotFoundException e) {
        System.out.println("An error occurred, while reading the file.");
        e.printStackTrace();
      }
    }
    
    public String getNextPlayerName() {
    	if (currentPlayerIndex + 1 == playerNames.size()) {
    		return playerNames.get(playerNames.size()-1);
    	}
    	return playerNames.get(currentPlayerIndex);
    } 
    
    public List<String> getPlayerNames() {
    	return playerNames;
    }
    
    public void setReqStatus(boolean status) {
    	reqStatus = status;
    }
    
    public boolean getReqStatus() {
    	return reqStatus;
    }
    
    public int getPlayerCount() {
    	return playerCount;
    }
    
    public Player getCurrentPlayer() {
    	return playerList.get(currentPlayerIndex);
    }
    
    
}






































