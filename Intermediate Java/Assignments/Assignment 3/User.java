import java.io.*;
import java.util.*;

public class User {
	private String firstName;
	private int numRight;
	private int numWrong;

	public User(String n, int r, int w) {
		firstName = n;
		numRight = r;
		numWrong = w;
	}

	public void answeredWrong() {
		numWrong++;
	}

	public void answeredRight() {
		numRight++;
	}

	public double getAvgScore() {
		if (numWrong == 0) {
			return 100.00;
		}
		return 100*(numRight/(double)(numRight+numWrong));
	}

	public String toString() {
		return firstName + "\n" + numRight + "\n" + numWrong;
	}
}