public class User {
	private String userName;
	private int numQRight;
	private int numQWrong;
	private double percentQRight;

	public User(String n) {
		userName = n;
	}

	public String getName() {
		return userName;
	}

	public void setNumQRight(int r) {
		numQRight = r;
	}

	public int getNumQRight() {
		return numQRight;
	}

	public void setNumQWrong(int w) {
		numQWrong = w;
	}

	public int getNumQWrong() {
		return numQWrong;
	}

	public void correctAnswer() {
		numQRight++;
	}

	public void incorrectAnswer() {
		numQWrong++;
	}

	public double getPercentQRight() {
		percentQRight = 100*numQRight/(double)(numQRight+numQWrong);
		return percentQRight;
	}
}