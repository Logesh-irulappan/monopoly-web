package jdbc_webProject;
public class Dice {

  private int diceMaxAbility;

  public Dice(int diceMaxAbility) {
    this.diceMaxAbility = diceMaxAbility;
  }

	public int roll() {
		return (int) (Math.random() * diceMaxAbility) + 1;
	}
}
