public class Question {
	private String question;
	private String[] answers;
	private int numAnswers;
	private int correctAnswer;
	private int numTotalTries;
	private int numCorrectTries;
	private String guess;
	private boolean correct;

	public Question(String q, int n) {
		question = q;
		numAnswers = n;
		answers = new String[numAnswers];
	}

	public String getQuestion() {
		return question;
	}

	public int getNumAnswers() {
		return numAnswers;
	}

	public void addAnswer(String a, int i) {
		answers[i] = a;
	}

	public String getAnswer(int i) {
		return answers[i];
	}

	public void setCorrectAnswer(int a) {
		correctAnswer = a;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}

	public String getCorrectAnswerString() {
		return answers[correctAnswer];
	}

	public void setTotalTries(int t) {
		numTotalTries = t;
	}

	public int getTotalTries() {
		return numTotalTries;
	}

	public void setCorrectTries(int t) {
		numCorrectTries = t;
	}

	public int getCorrectTries() {
		return numCorrectTries;
	}

	public void answeredCorrectly(int a) {
		numTotalTries++;
		numCorrectTries++;
		guess = answers[a];
		correct = true;
	}

	public void answeredIncorrectly(int a) {
		numTotalTries++;
		guess = answers[a];
		correct = false;
	}

	public String getGuess() {
		return guess;
	}

	public boolean correct() {
		if (correct) {
			return true;
		}
		return false;
	}

	public double cumulativePercent() {
		return numCorrectTries/(double) numTotalTries;
	}

	public String toString() {
		StringBuilder s = new StringBuilder(question + "\n");
		for (int i = 0; i < numAnswers; i++) {
			s.append(i + ": " + answers[i] + "\n");
		}
		return s.toString();
	}

}