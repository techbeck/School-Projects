public class Player {
	private String lastName;
	private String firstName;
	private double playerMoney;
	private double moneyGained;
	private int totalRoundsPlayed;
	private int totalRoundsWon;
	private int numRoundsPlayed;
	private int numRoundsWon;

	public Player() {
		moneyGained = 0.00;
	}

	public void setLastName(String last) {
		lastName = last;
	}

	public String getLastName() {
		return lastName;
	}

	public void setFirstName(String first) {
		firstName = first;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setMoney(double money) {
		playerMoney = money;
	}

	public double getRemainingMoney() {
		return playerMoney;
	}

	public void setTotalRounds(int totalGames) {
		totalRoundsPlayed = totalGames;
	}

	public int getTotalRounds() {
		return totalRoundsPlayed;
	}

	public void setTotalRoundsWon(int wonGames) {
		totalRoundsWon = wonGames;
	}

	public int getTotalRoundsWon() {
		return totalRoundsWon;
	}

	public double getMoneyGained() {
		return moneyGained;
	}

	public int getRoundsPlayed() {
		return numRoundsPlayed;
	}

	public int getRoundsWon() {
		return numRoundsWon;
	}

	public void addMoney(double money) {
		playerMoney += money;
		moneyGained += money;
	}

	public void subtractMoney(double money) {
		playerMoney -= money;
		moneyGained -= money;
	}

	public void wonAGame() {
		totalRoundsWon++;
		totalRoundsPlayed++;
		numRoundsWon++;
		numRoundsPlayed++;
	}

	public void lostAGame() {
		totalRoundsPlayed++;
		numRoundsPlayed++;
	}

	public String toString() {
		String text = String.format("\tName: %s %s\n\tMoney left: $%,.2f\n\tTotal rounds played: %d\n\tTotal rounds won: %d", firstName, lastName, playerMoney, totalRoundsPlayed, totalRoundsWon);
		return text;
	}
}