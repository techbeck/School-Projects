public class Question {
	private String question;
	private String[] answers;
	private int numAnswers;
	private int correctAnswer;
	private int numTotalTries;
	private int numCorrectTries;
	private String guess;
	private boolean correct;
	private int userTotal;
	private int userCorrect

	public Question(String q, int n) {
		question = q;
		numAnswers = n;
		answers = new String[numAnswers];
	}

	public Question(String q, int t, int c) {
		question = q;
		userTotal = t;
		userCorrect = c;
	}

	public String getQuestion() {
		return question;
	}

	public int getNumAnswers() {
		return numAnswers;
	}

	public String[] getAnswersArray() {
		return answers;
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
		userTotal++;
		guess = answers[a];
		correct = true;
	}

	public void answeredIncorrectly(int a) {
		numTotalTries++;
		userCorrect++;
		guess = answers[a];
		correct = false;
	}

	public String getGuess() {
		return guess;
	}

	public boolean correct() {
		return correct;
	}

	public double cumulativePercent() {
		return 100*(numCorrectTries/(double) numTotalTries);
	}

	public int getUserTotal() {
		return userTotal;
	}

	public int getUserCorrect() {
		return userCorrect;
	}

	public String toString() {
		StringBuilder s = new StringBuilder(question + "\n");
		for (int i = 0; i < numAnswers; i++) {
			s.append(i + ": " + answers[i] + "\n");
		}
		return s.toString();
	}

	public int compareTo(Question q2) {
		double diff = (this.cumulativePercent() - q2.cumulativePercent());
		if (Math.abs(diff) < 0.001) {
			return 0;
		}
		if (this.cumulativePercent() < q2.cumulativePercent()) {
			return -1;
		} else {
			return 1;
		}
	}
}