/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080

Program implements multiple-choice quiz.
Keeps track of total number of tries, and correct answers.
Keeps track of users independently of quizzes.
*/

import java.util.*;
import java.io.*;

public class Assig3 {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Enter quiz file name when running program");
			System.exit(1);
		}
		String fileName = args[0];
		File quizFile = new File(fileName);
		if (!quizFile.exists()) {
			System.out.println("Quiz file does not exist");
			System.exit(1);
		}
		Scanner quizReader = new Scanner(quizFile);
		Scanner keyboard = new Scanner(System.in);

		System.out.print("What's your username? ");
		String name = keyboard.nextLine();
		User person = null;
		File userFile = new File("users.txt");
		if (!userFile.exists()) {
			System.out.println("User file does not exist");
			System.exit(1);
		}
		Scanner userReader = new Scanner(userFile);
		boolean userInFile = false;
		int numUsers = userReader.nextInt();
		int numQ = userReader.nextInt();
		String[] quest = new String[numQ];
		User[] users = new User[numUsers];
		for (int i = 0; i < numUsers; i++) {
			userReader.nextLine();
			String fileUsername = userReader.nextLine();
			users[i] = new User(fileUsername, numQ);
			int numRight = userReader.nextInt();
			int numWrong = userReader.nextInt();
			for (int j = 0; j < numQ; j++) {
				userReader.nextLine();
				String question = userReader.nextLine();
				quest[j] = question;
				double percent = userReader.nextDouble();
				Question q = new Question(question, percent);
				users[i].addQuestion(q, j);
			}
			if (name.equals(fileUsername)) {
				person = users[i];
				userInFile = true;
				break;
			}
		}
		if (!userInFile) {
			person = new User(name, numQ);
			person.setNumQRight(0);
			person.setNumQWrong(0);
			for (int j = 0; j < numQ; j++) {
				Question q = new Question(quest[j], 0.00);
				person.addQuestion(q,j);
			}
		}

		ArrayList<Question> questions = new ArrayList<Question>();
		int numQuestions = 0;
		// Read in questions from file
		while (quizReader.hasNext()) {
			String question = quizReader.nextLine();
			int numAnswers = quizReader.nextInt();
			quizReader.nextLine();
			Question q = new Question(question, numAnswers);
			for (int i = 0; i < numAnswers; i++) {
				q.addAnswer(quizReader.nextLine(), i);
			}
			q.setCorrectAnswer(quizReader.nextInt());
			q.setTotalTries(quizReader.nextInt());
			q.setCorrectTries(quizReader.nextInt());
			quizReader.nextLine();
			questions.add(q);
			numQuestions++;
		}
		quizReader.close();

		// Actual quiz portion
		for (int i = 0; i < numQuestions; i++) {
			Question q = questions.get(i);
			System.out.printf("Question %d:\n%s", i, q.toString());
			int answer;
			do {
				System.out.print("Your answer? (enter a number): ");
				answer = keyboard.nextInt();
			} while (answer > q.getNumAnswers());
			if (answer == q.getCorrectAnswer()) {
				q.answeredCorrectly(answer);
				person.answeredRight();
			} else {
				q.answeredIncorrectly(answer);
				person.answeredWrong();
			}
		}

		// Shows personal results
		double numCorrect = 0;
		double numTotal = 0;
		System.out.println("\nResults:");
		for (int i = 0; i < numQuestions; i++) {
			Question q = questions.get(i);
			System.out.print("Question: ");
			System.out.println(q.getQuestion());
			System.out.print("Correct answer: ");
			System.out.println(q.getCorrectAnswerString());
			System.out.print("Your answer: ");
			System.out.println(q.getGuess());
			if (q.correct()) {
				System.out.println("Result: CORRECT! Great Work!");
				numCorrect++;
				numTotal++;
			} else {
				System.out.println("Result: INCORRECT! Remember the answer for next time!");
				numTotal++;
			}
			System.out.println();
		}
		double percentCorrect = 100*(numCorrect/numTotal);
		System.out.printf("Your score is: %.2f%%\n", percentCorrect);
		
		// Show cumulative user stats
		System.out.printf("Over all attempts, your average score is: %.2f%%\n", person.getAvgScore());
		

		// Shows cumulative stats
		String easyQuestion = null;
		double easyPercent = 0;
		String hardQuestion = null;
		double hardPercent = 100;
		System.out.println("\nCumulative stats:");
		for (int i = 0; i < numQuestions; i++) {
			Question q = questions.get(i);
			System.out.println("Question: " + q.getQuestion());
			System.out.println("Number of times answered correctly: " + q.getCorrectTries());
			int incorrectTries = q.getTotalTries() - q.getCorrectTries();
			System.out.println("Number of times answered incorrectly: " + incorrectTries);
			double cumulativePercentCorrect = q.cumulativePercent();
			System.out.printf("Percent correct answers: %.2f%%\n", cumulativePercentCorrect);
			System.out.println();
			if (cumulativePercentCorrect >= easyPercent) {
				easyQuestion = q.getQuestion();
				easyPercent = cumulativePercentCorrect;
			}
			if (cumulativePercentCorrect <= hardPercent) {
				hardQuestion = q.getQuestion();
				hardPercent = cumulativePercentCorrect;
			}
		}
		System.out.println("Easiest question: " + easyQuestion);
		System.out.printf("Percent correct: %.2f%%\n", easyPercent);
		System.out.println("Hardest question: " + hardQuestion);
		System.out.printf("Percent correct: %.2f%%\n", hardPercent);

		// Outputs quiz data to file
		PrintWriter quizWriter = new PrintWriter(quizFile);
		for (int i = 0; i < numQuestions; i++) {
			Question q = questions.get(i);
			quizWriter.println(q.getQuestion());
			quizWriter.println(q.getNumAnswers());
			for (int j = 0; j < q.getNumAnswers(); j++) {
				quizWriter.println(q.getAnswer(j));
			}
			quizWriter.println(q.getCorrectAnswer());
			quizWriter.println(q.getTotalTries());
			quizWriter.println(q.getCorrectTries());
		}
		quizWriter.close();

		PrintWriter userWriter = new PrintWriter(userFile);
		userWriter.println(numUsers);
		for (int i = 0; i < numUsers; i++) {
			userWriter.println(users[i].toString());
		}
		if (!userInFile) {
			userWriter.println(person.toString());
		}
		userWriter.close();
	}
}
