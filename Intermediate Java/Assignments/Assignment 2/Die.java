import java.util.Random;

public class Die {
	private int value;

	public Die() {
		Random randomNum = new Random();
		value = randomNum.nextInt(6) + 1;
	}

	public int getValue() {
		return value;
	}
}