import java.io.*;
import java.util.*;

public class User {
	private String username;
	private int numQRight;
	private int numQWrong;
	private int numQuestions;
	private Question[] questions;

	public User(String name, int num) {
		username = name;
		numQuestions = num;
		questions = new Question[numQuestions];
	}

	public void setNumQRight(int r) {
		numQRight = r;
	}

	public void setNumQWrong(int w) {
		numQWrong = w;
	}

	public void addQuestion(Question q, int j) {
		questions[j] = q;
	}

	public void answeredRight(int i) {
		numQRight++;
		questions[i].answeredCorrectly();
	}

	public void answeredWrong(int i) {
		numQWrong++;
		questions[i].answeredIncorrectly();
	}

	public double getAvgScore() {
		if (numQWrong == 0) {
			return 100.00;
		}
		return 100*(numQRight/(double)(numQRight+numQWrong));
	}

	public String toString() {
		StringBuilder s = new StringBuilder(username);
		s.append("\n");
		s.append(numQRight);
		s.append("\n");
		s.append(numQWrong);
		for (int i = 0; i < numQuestions; i++) {
			s.append("\n");
			s.append(questions[i].getQuestion());
			s.append("\n");
			s.append(questions[i].getUserTotal());
			s.append("\n");
			s.append(questions[i].getUserCorrect());
		}
		return s.toString();
	}
}