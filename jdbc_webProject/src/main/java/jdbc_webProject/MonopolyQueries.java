//$Id$
package jdbc_webProject;

import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;

import Places.Charge;
import Places.Go;
import Places.Jail;
import Places.Place;
import Places.PlaceType;
import Places.Property;
import Places.Railroad;
import Places.Tax;
import db.DeleteQuery;
import db.InsertQuery;
import db.JoinQuery;
import db.JoinType;
import db.SelectQuery;
import db.TruncateQuery;
import db.UpdateQuery;

import java.util.ArrayList;

public class MonopolyQueries {	
	
	public void truncateTables(String[] tableNames) {
		for (int i = 0; i < tableNames.length; i++) {
			String tableName = tableNames[i];
			TruncateQuery truncateQuery = new TruncateQuery(tableName);
			truncateQuery.execute();
		}
	}
	
	public void deleteGameEndData(String[] tableNames) {
		for (int i = 0; i < tableNames.length; i++) {
			String tableName = tableNames[i];
			DeleteQuery deleteQuery = new DeleteQuery(tableName);
			deleteQuery.execute();
		}
	}
	
	public List<String> getPlaceNamesByPlaceIds(List<Integer> placeIds) {
		List<String> placeNames = new ArrayList<>();
		for (int placeId : placeIds) {
			SelectQuery selectQuery = new SelectQuery("PLACE");
			selectQuery.addColumn("PlaceName");
			selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
			try {
				ResultSet res = selectQuery.execute();
				
				while (res.next()) 
					placeNames.add(res.getString("PlaceName"));
			}
			catch (SQLException e) {
				System.out.println(e.toString());
			}
		}
		return placeNames;
	}
	
	public static String getPlaceNameById(int id) {
		SelectQuery selectQuery = new SelectQuery("PLACE");
		selectQuery.addColumn("PlaceName");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(id));
		
		try {
			ResultSet result = selectQuery.execute();
			
			if (result.next())
				return result.getString("PlaceName");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Place createProperty(String[] data) {	
		createPlace(data[1], PlaceType.PROPERTY);
		
		InsertQuery insertQuery = new InsertQuery("PROPERTY");
		int placeId = getPlaceIdByName(data[1]);
		
		insertQuery.setInt(1, String.valueOf(placeId)); 
		insertQuery.setDouble(2, data[2]);
		insertQuery.setDouble(3, data[3]);
		insertQuery.setInt(4, data[4]);
		insertQuery.setInt(5, "0");
		insertQuery.execute();
		
		Place place = new Property(placeId, data[1], Double.valueOf(data[2]), Double.valueOf(data[3]), Integer.valueOf(data[4].trim()));
		return place;
	}
	
	public Place createChargePlace(String[] data) {
		createPlace(data[1], PlaceType.CHARGE);
		
		InsertQuery insertQuery = new InsertQuery("CHARGES");
		int placeId = getPlaceIdByName(data[1]);
	
		insertQuery.setInt(1, String.valueOf(placeId));
		insertQuery.setDouble(2, data[2]);		
		insertQuery.execute();
		
		Place place;
		
		if(data[1].contains("RAILROAD")) {
			place = new Railroad(placeId, data[1], Double.valueOf(data[2]));
		}
		else if(data[1].contains("TAX")) {
			place = new Tax(placeId, data[1], Double.valueOf(data[2]));
		}
		else {
			place = new Charge(placeId, data[1], Double.valueOf(data[2]));
		}
		
		return place;
	}
	
	private void createPlace(String placeName, PlaceType placeType) {
		InsertQuery insertQuery = new InsertQuery("PLACE");
		insertQuery.addColumn("PlaceName");
		insertQuery.addColumn("PlaceType");
		insertQuery.setString(1, placeName);
		insertQuery.setString(2, placeType.getTypeName());
		insertQuery.execute();
	}
	
	public Place createPlace(String[] data) { 
		InsertQuery insertQuery = new InsertQuery("PLACE");
		insertQuery.addColumn("PlaceName");
		insertQuery.addColumn("PlaceType");
		insertQuery.setString(1, data[1]);
		insertQuery.setString(2, PlaceType.PLACE.getTypeName());
		insertQuery.execute();
		
		int placeId = getPlaceIdByName(data[1]);
		
		Place place;
		if(data[1].contains("JAIL")) {
			place = new Jail(placeId, data[1]);
		}
		else if(data[1].contains("GO")) {
			place = new Go(placeId, data[1]);
		}
		else if(data[1].contains("CHEST")){
			place = new Place(placeId, data[1]);
		}
		else if(data[1].contains("ELECTRIC")) {
			place = new Place(placeId, data[1]);
		}
		else {
			place = new Place(placeId, data[1]);
		}
		 
		return place;
	}
	
	public boolean depositToPlayer(int playerId, double amount) {
	
		double updatedBalance = getBalance(playerId) + amount;
		
		UpdateQuery update = new UpdateQuery("BANKER");
		update.addUpdates("balance", String.valueOf(updatedBalance));
		update.addConditions("UserID", String.valueOf(playerId));
		
		update.execute();
		return true;
	}
	
	public boolean withdrawPlayerAmount(int playerId, double amount) {
		double currentBalance = getBalance(playerId);
		
		if (amount > currentBalance) {
			System.out.println("INSUFFICIENT BANK BALANCE TO WITHDRAW THE MENTIONED AMOUNT  \n");
			return false;
		}
		currentBalance -= amount;
		
		UpdateQuery update = new UpdateQuery("BANKER");
		update.addUpdates("balance", String.valueOf(currentBalance));
		update.addConditions("UserID", String.valueOf(playerId));
		
		update.execute();
		return true;
	}
	
	
	public static Integer getPlaceOwnerId(int placeId) {
		SelectQuery selectQuery = new SelectQuery("OWNEDPLACES");
		selectQuery.addColumn("OwnerId");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
		
		ResultSet result = selectQuery.execute();
		
		try {
			if (result.next())
				return result.getInt("OwnerId");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private int getPlaceIdByName(String placeName) { 
		SelectQuery selectQuery = new SelectQuery("PLACE");
		selectQuery.addColumn("PlaceId");
		selectQuery.addWhereArguments("PlaceName", placeName);
		
		ResultSet queryResult = selectQuery.execute();
		
		int placeId = 0;
		try {
			if (queryResult.next()) 
				placeId = queryResult.getInt("PlaceId");
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		return placeId;
	}
	
	public static double getRentValue(int placeId) {
		SelectQuery selectQuery = new SelectQuery("PROPERTY");
		selectQuery.addColumn("RentValue");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
		try {
			ResultSet result = selectQuery.execute();
			
			if (result.next())
				return result.getDouble("RentValue");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static double getSellValue(int placeId) {
		SelectQuery selectQuery = new SelectQuery("PROPERTY");
		selectQuery.addColumn("SellValue");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
		try {
			ResultSet result = selectQuery.execute();
			
			if (result.next())
				return result.getDouble("SellValue");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static boolean increaseHouseRent(int placeId) {
		double rentValue = getRentValue(placeId) * 2;
		
		UpdateQuery updateQuery = new UpdateQuery("PROPERTY");
		updateQuery.addUpdates("RentValue", String.valueOf(rentValue));
		updateQuery.addConditions("PlaceId", String.valueOf(placeId));
		return updateQuery.execute();
	}
	
	public static boolean setAvailedHouses(int placeId, int availedHouses) {
		UpdateQuery updateQuery = new UpdateQuery("PROPERTY");
		updateQuery.addUpdates("AvailedHouses", String.valueOf(availedHouses));
		updateQuery.addConditions("PlaceId", String.valueOf(placeId));
		return updateQuery.execute();
	}
	
	public static int getAvailedHouses(int placeId) {
		SelectQuery selectQuery = new SelectQuery("PROPERTY");
		selectQuery.addColumn("AvailedHouses");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
		try {
			ResultSet result = selectQuery.execute();
			if(result.next())
				return result.getInt("AvailedHouses");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getTotalHouses(int placeId) {
		SelectQuery selectQuery = new SelectQuery("PROPERTY");
		selectQuery.addColumn("TotalHouses");
		selectQuery.addWhereArguments("PlaceId", String.valueOf(placeId));
		try {
			ResultSet result = selectQuery.execute();
			if(result.next())
				return result.getInt("TotalHouses");
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public static void setPlaceOwner(int playerId, int placeId) {
		InsertQuery insertQuery = new InsertQuery("OWNEDPLACES");
		insertQuery.addColumn("PlaceId");
		insertQuery.addColumn("OwnerId");
		insertQuery.setString(1, String.valueOf(placeId));
		insertQuery.setString(2, String.valueOf(playerId));
		insertQuery.execute();
	}
	
	public static List<String> getOwnedPlaces(int playerId) {
		List<String> placeList = new ArrayList<>();
		
		JoinQuery joinQuery = new JoinQuery();
		joinQuery.setLeftTableName("PLACE");
		joinQuery.setRightTableName("OWNEDPLACES");
		
		joinQuery.addLeftColumns("PlaceName");
		joinQuery.setJoinType(JoinType.JOIN);
		
		joinQuery.setLeftMappingColumn("PlaceId");
		joinQuery.setRightMappingColumn("PlaceId");
		
		joinQuery.addRightWhereArguments("OwnerId", String.valueOf(playerId));
		
		try {
			ResultSet result = joinQuery.execute();
			while (result.next()) {
				placeList.add(result.getString("PlaceName"));
			}
		}
		catch (Exception e) {
			System.out.println(e.toString());
		}
		
		return new ArrayList<>();
	}
	
	public void setBankerBalance(double amount) {
		InsertQuery insertQuery = new InsertQuery("BANKER");
		
		insertQuery.setInt(1, "0");
		insertQuery.setDouble(2, String.valueOf(amount));
		insertQuery.setDouble(3, "0");
		
		insertQuery.execute();
	}
	
	public double getBalance(int playerId) {
		SelectQuery selectQuery = new SelectQuery("BANKER");
		selectQuery.addColumn("Balance");
		selectQuery.addWhereArguments("UserID", String.valueOf(playerId));
		
		double balance = 0;
		try {
			ResultSet resultSet = selectQuery.execute();
			if (resultSet.next()) {
				balance = resultSet.getDouble("Balance");
				return balance;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return balance;
	}
	
	public void addBorrowedAmount(int playerId, double amount) {
		double borrowedAmount = getBorrowedAmount(playerId);
		amount += borrowedAmount;
		
		UpdateQuery updateQuery = new UpdateQuery("BANKER");
		updateQuery.addUpdates("BorrowedAmount", String.valueOf(amount));
		updateQuery.addConditions("UserID", String.valueOf(playerId));
		updateQuery.execute();
	}
	
	public double getBorrowedAmount(int playerId) {
		SelectQuery selectQuery = new SelectQuery("BANKER");
		selectQuery.addColumn("BorrowedAmount");
		selectQuery.addWhereArguments("UserID", String.valueOf(playerId));
		
		double borrowedAmount = 0;
		try {
			ResultSet resultSet = selectQuery.execute();
			if (resultSet.next()) {
				borrowedAmount = resultSet.getDouble("BorrowedAmount");
				return borrowedAmount;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return borrowedAmount;
	}
	
	public void setBorrowedAmount(double amount, int playerId) {
		UpdateQuery updateQuery = new UpdateQuery("BANKER");
		updateQuery.addUpdates("BorrowedAmount", String.valueOf(amount));
		updateQuery.addConditions("UserID", String.valueOf(playerId));
		
		updateQuery.execute();
	}
	
	public static String getPlayerName(int playerId) {
		try {
    		SelectQuery selectQuery = new SelectQuery("PLAYERS");
    		selectQuery.addColumn("PlayerName");
    		selectQuery.addWhereArguments("PlayerId", String.valueOf(playerId));
    		
    		ResultSet queryResult = selectQuery.execute();
    		
    		if (queryResult.next()) {
    			return queryResult.getString("PlayerName");
    		}
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	return "";
	}
	
	public Player getPlayerByName(String playerName) {
		try {
    		SelectQuery selectQuery = new SelectQuery("PLAYERS");
    		selectQuery.addWhereArguments("PlayerName", playerName);
    		
    		ResultSet queryResult = selectQuery.execute();
    		
    		Player player = null;
    		if (queryResult.next()) {
    			player = new Player(queryResult.getString("PlayerName"), queryResult.getInt("PlayerId"));
    		}
    		
    		return player;
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	return null;
	}
	
	public Player getPlayerById(int playerId) {
    	try {
    		SelectQuery selectQuery = new SelectQuery("PLAYERS");
    		selectQuery.addWhereArguments("PlayerId", String.valueOf(playerId));
    		
    		ResultSet queryResult = selectQuery.execute();
    		
    		Player player = null;
    		if (queryResult.next()) {
    			player = new Player(queryResult.getString("PlayerName"), queryResult.getInt("PlayerId"));
    		}
    		
    		return player;
    	}
    	catch (Exception e) {
    		System.out.println(e.toString());
    	}
    	return null;
    }
	
	public void createBankAccount(String playerName, int playerId, double bankBalance) {
		InsertQuery bankInQuery = new InsertQuery("BANKER");
		bankInQuery.setInt(1, String.valueOf(playerId));
		bankInQuery.setDouble(2, String.valueOf(bankBalance));
		bankInQuery.setDouble(3, "0");
		
		bankInQuery.execute();
	}
	
	public void addPlayer(String playerName, int bankBalance) {
    	try {
    		InsertQuery playerInQuery = new InsertQuery("PLAYERS");
    		playerInQuery.addColumn("PlayerName");
    		playerInQuery.setString(1, playerName);
    		playerInQuery.execute();
    		
    		SelectQuery selectQuery = new SelectQuery("PLAYERS");
    		selectQuery.addWhereArguments("PlayerName", playerName);
    		
    		ResultSet resultSet = selectQuery.execute();
    		
    		int playerId = 0;
    		if (resultSet.next()) {
    			playerId = resultSet.getInt("PlayerId");
    		}
    		
    		createBankAccount(playerName, playerId, bankBalance);
    	}
    	catch (Exception e) {
    		e.printStackTrace();
    	}
    }
	
	public boolean isPlayerExists(String playerName) {
    	try {
    		SelectQuery selectQuery = new SelectQuery("PLAYERS");
    		
    		selectQuery.addColumn("playerName");
    		selectQuery.addWhereArguments("playerName", playerName);
    		
    		ResultSet queryResult = selectQuery.execute();
    		
    		if (queryResult.next()) 
    			return true;
    	}
    	catch (Exception e) {
    		e.toString();
    	}
    	
    	return false;
    }
	
}
