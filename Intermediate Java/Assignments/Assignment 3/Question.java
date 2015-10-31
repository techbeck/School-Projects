public class Question implements Comparable<Question> {
	private String question;
	private String[] answers;
	private int correctAnswer;
	private String hint;
	private int numRight1;
	private int numWrong1;
	private int numRight2;
	private int numWrong2;
	private double percentRight1;
	private double percentRight2;
	private String guess1;
	private String guess2;
	private boolean correct;

	public Question(String q, int n) {
		question = q;
		answers = new String[n];
	}

	public void addAnswer(String g, int i) {
		answers[i] = g;
	}

	public void setCorrectAnswer(int c) {
		correctAnswer = c;
	}

	public void setHint(String h) {
		hint = h;
	}

	public void setNumRight1(int r1) {
		numRight1 = r1;
	}

	public void setNumWrong1(int w1) {
		numWrong1 = w1;
	}

	public void setNumRight2(int r2) {
		numRight2 = r2;
	}

	public void setNumWrong2(int w2) {
		numWrong2 = w2;
	}	

	public int getNumRight1() {
		return numRight1;
	}

	public int getNumRight2() {
		return numRight2;
	}

	public int getNumWrong1() {
		return numWrong1;
	}

	public int getNumWrong2() {
		return numWrong2;
	}

	public double getPercent1() {
		percentRight1 = 100*numRight1/(double)(numRight1+numWrong1);
		return percentRight1;
	}

	public double getPercent2() {
		if (numRight2+numWrong2 == 0) {
			return 0;
		}
		percentRight2 = 100*numRight2/(double)(numRight2+numWrong2);
		return percentRight2;
	}

	public String getQuestion() {
		return question;
	}

	public int getNumAnswers() {
		return answers.length;
	}

	public int getCAnswerNum() {
		return correctAnswer;
	}

	public String getCAnswerString(){
		return answers[correctAnswer];
	}

	public String[] getAnswersArray() {
		return answers;
	}

	public String getHint() {
		return hint;
	}

	public String getGuess1() {
		return guess1;
	}

	public String getGuess2() {
		return guess2;
	}

	public void answeredRight1(int g) {
		numRight1++;
		guess1 = answers[g];
		correct = true;
	}

	public void answeredWrong1(int g) {
		numWrong1++;
		guess1 = answers[g];
	}

	public void answeredRight2(int g) {
		numRight2++;
		guess2 = answers[g];
		correct = true;
	}

	public void answeredWrong2(int g) {
		numWrong2++;
		guess2 = answers[g];
		correct = false;
	}

	public String toString() {
		StringBuilder s = new StringBuilder(question + "\n");
		for (int i = 0; i < answers.length; i++) {
			s.append(i + ": " + answers[i] + "\n");
		}
		return s.toString();
	}

	public int compareTo(Question q2) {
		double diff = (this.getPercent1() - q2.getPercent1());
		if (Math.abs(diff) < 0.001) {
			diff = 0;
		}
		if (diff < 0) {
			return -1;
		}
		return 1;
	}
}