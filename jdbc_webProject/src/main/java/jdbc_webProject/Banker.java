package jdbc_webProject;

public class Banker {
	
	public static Banker instance;
	public static MonopolyQueries queries = new MonopolyQueries();
	
	public static Banker getInstance() {
	  if (instance == null)
	    instance = new Banker();
	  return instance;
	}
	
	public void setInitialBankBalance(double initialBalance) {
		queries.setBankerBalance(initialBalance); 
	}
	
	public static String getMoneyFromBank(Player player, double amount) {
		if (amount <= 0) {
			return "Invalid negative amount..";
		}
		
		StringBuilder strResponse = new StringBuilder("");
		
	  if (isBankHaveAmount(amount)) {
	    player.addBalance(amount);
	    strResponse.append(player.getName() + ", Your Current Bank Balance: " + player.getBalance() + " \n");
	    
	    addBorrower(player, amount); 
	    deductAmountFromBank(amount);
	    
	    strResponse.append("Borrowed Balance successfully added to your wallet!");
	  }
	  else {
		  strResponse.append("This much amount is not Available at Bank right now. \n");
	  }
	  return strResponse.toString();
	}
	
	public String payBank(double amount) {
	    addAmountToBank(amount);
	    return "$" + amount + " is deposited to Banker ";
	}
	
	public String payBorrowedMoney(double amount, Player player) {
		if (amount <= 0) {
			return "Invalid negative amount..";
		}
		
		if(player.getBalance() >= amount) {
			double currentBorrowedAmount = queries.getBorrowedAmount(player.getId());
			
			if (currentBorrowedAmount >= amount) {
				currentBorrowedAmount -= amount;
				
				player.deductBalance(amount);
				queries.setBorrowedAmount(currentBorrowedAmount, player.getId());
				queries.depositToPlayer(0, amount); // banker id is 0, so paying banker from here...
				
				String res = "$" + amount + " is deposited to Banker. ";
				return res + "Player: " + player.getName() + " Borrowed Amount is reduced to $" + currentBorrowedAmount;
			}
			else {
				amount -= currentBorrowedAmount;
				
				player.deductBalance(currentBorrowedAmount);
				queries.setBorrowedAmount(0, player.getId());
				queries.depositToPlayer(0, currentBorrowedAmount);
				
				return "Given amount is more than Borrowed Amount $" + currentBorrowedAmount +
					   " , So Bank only reduced the borrowedAmount from your account.";
			}
			
		}
		else {
			return "Entered amount is not available in your bank account!.. Payment cancelled. ";
		}
		 
	}
	
	private static boolean isBankHaveAmount(double amount) {
	  try {
			return queries.getBalance(0) >= amount;
	  } catch (Exception e) {
			e.printStackTrace();
	  }
	  return false;
	}
	
	public static double getBorrowedAmount(int playerId) {
		return queries.getBorrowedAmount(playerId);
	}
	
	public static double getBalance(int playerId) {
		return queries.getBalance(playerId);
	}
	
	public static boolean withdraw(int playerId, double amount) {
		return queries.withdrawPlayerAmount(playerId, amount);
	}
	
	public static boolean credit(int playerId, double amount) {
		return queries.depositToPlayer(playerId, amount);
	}
	
	private static void addBorrower(Player player, double amount) {
		double borrowed = queries.getBorrowedAmount(0) + amount;
		queries.addBorrowedAmount(player.getId(), borrowed);
	}
	
	private static String addAmountToBank(double amount) {
	  double bankBalance = queries.getBalance(0) + amount;
	  queries.depositToPlayer(0, bankBalance);
	  return "Bank Balance is increased by $"+ amount +" \n current Bank Balance: " + bankBalance;
	}
	
	private static String deductAmountFromBank(double amount) {
	    queries.withdrawPlayerAmount(0, amount);
	    return "Bank Balance is deducted by $"+ queries.getBalance(0);
	}
	
}
