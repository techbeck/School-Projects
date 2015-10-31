/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080

Program implements multiple-choice quiz.
Keeps track of total number of tries, and correct answers.
*/

/**
Questions:
- how to get users working right
- what percent should second try be if always got right on first try? 0%?
- if always got right on second try, what is score?
*/

import java.util.*;
import java.io.*;

public class Assignment3 {
	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.out.println("Enter quiz file name when running program");
			System.exit(1);
		}
		String file = args[0];
		File quizFile = new File(file);
		if (!quizFile.exists()) {
			System.out.println("Quiz file does not exist");
			System.exit(1);
		}
		Scanner quizReader = new Scanner(quizFile);
		Scanner keyboard = new Scanner(System.in);

		// User login
		System.out.print("What's your name? ");
		String name = keyboard.nextLine();
		File userFile = new File("users.txt");
		if (!userFile.exists()) {
			System.out.println("User file does not exist");
			System.exit(1);
		}
		Scanner userReader = new Scanner(userFile);
		ArrayList<User> users = new ArrayList<User>();
		User person = null;
		boolean userInFile = false;
		while (userReader.hasNext()) {
			String userName = userReader.nextLine();
			int numRight = userReader.nextInt();
			int numWrong = userReader.nextInt();
			userReader.nextLine();
			User fileUser = new User(userName);
			fileUser.setNumQRight(numRight);
			fileUser.setNumQWrong(numWrong);
			users.add(fileUser);
			if (name.equals(userName)) {
				person = fileUser;
				userInFile = true;
				break;
			}
		}
		if (!userInFile) {
			person = new User(name);
			person.setNumQRight(0);
			person.setNumQWrong(0);
		}

		// Read in questions from file
		ArrayList<Question> questions = new ArrayList<Question>();
		int numQuestions = 0;
		while (quizReader.hasNext()) {
			String question = quizReader.nextLine();
			int numAnswers = quizReader.nextInt();
			quizReader.nextLine();
			Question q = new Question(question, numAnswers);
			for (int i = 0; i < numAnswers; i++) {
				q.addAnswer(quizReader.nextLine(), i);
			}
			q.setCorrectAnswer(quizReader.nextInt());
			quizReader.nextLine();
			q.setHint(quizReader.nextLine());
			q.setNumRight1(quizReader.nextInt());
			q.setNumWrong1(quizReader.nextInt());
			q.setNumRight2(quizReader.nextInt());
			q.setNumWrong2(quizReader.nextInt());
			quizReader.nextLine();
			questions.add(q);
			numQuestions++;
		}
		quizReader.close();

		// Actual quiz portion
		for (int i = 0; i < numQuestions; i++) {
			Question q = questions.get(i);
			System.out.printf("Question %d:\n%s", i, q.toString());
			int guess;
			do {
				System.out.print("Your answer? (enter a number): ");
				guess = keyboard.nextInt();
			} while (guess > q.getNumAnswers());
			if (guess == q.getCAnswerNum()) {
				q.answeredRight1(guess);
				person.correctAnswer();
			} else {
				q.answeredWrong1(guess);
				System.out.println("Incorrect. Try again.");
				System.out.printf("Hint: %s\n", q.getHint());
				do {
					System.out.print("Your answer? (enter a number): ");
					guess = keyboard.nextInt();
				} while (guess > q.getNumAnswers());
				if (guess == q.getCAnswerNum()) {
					q.answeredRight2(guess);
					person.correctAnswer();
				} else {
					q.answeredWrong2(guess);
					person.incorrectAnswer();
				}
			}
		}

		// Shows personal results
		int numCorrect = 0;
		int numTotal = 0;
		System.out.println("\nResults:");
		for (int i = 0; i < numQuestions; i++) {
			numTotal++;
			Question q = questions.get(i);
			System.out.print("Question: ");
			System.out.println(q.getQuestion());
			System.out.print("Correct answer: ");
			System.out.println(q.getCAnswerString());
			System.out.print("Your first answer: ");
			System.out.println(q.getGuess1());
			if (q.getGuess1().equals(q.getCAnswerString())) {
				System.out.println("Result: CORRECT ON FIRST TRY! Great Work!");
				numCorrect++;
			} else {
				System.out.print("Your second answer: ");
				System.out.println(q.getGuess2());
				if (q.getGuess2().equals(q.getCAnswerString())) {
					System.out.println("Result: CORRECT ON SECOND TRY! Great Work!");
					numCorrect++;
				} else {
					System.out.println("Result: INCORRECT! Remember the answer for next time!");
				}
			}
			System.out.println();
		}
		double percentCorrect = 100*(numCorrect/(double)numTotal);
		System.out.printf("Your score is: %.2f%%\n", percentCorrect);
		System.out.printf("Your average score is: %.2f%%\n", person.getPercentQRight());

		ArrayList<Question> sortedList = new ArrayList<Question>();
		sortedList.addAll(questions);
		for (int start = 0; start < (sortedList.size() -1); start++) {
			int maxIndex = start;
			Question max = sortedList.get(start);
			for(int index = start +1; index < sortedList.size(); index++) {
				if (sortedList.get(index).compareTo(max) > 0) {
					max = sortedList.get(index);
					maxIndex = index;
				}
			}
			sortedList.set(maxIndex, sortedList.get(start));
			sortedList.set(start, max);
		}

		// Shows cumulative stats
		String easyQuestion = null;
		double easyPercent = 0;
		String hardQuestion = null;
		double hardPercent = 100;
		System.out.println("\nCumulative stats:");
		for (int i = 0; i < sortedList.size(); i++) {
			Question q = sortedList.get(i);
			System.out.println("Question: " + q.getQuestion());
			System.out.print("Number of times answered correctly on first try: "); 
			System.out.println(q.getNumRight1());
			System.out.print("Number of times answered correctly on second try: ");
			System.out.println(q.getNumRight2());
			int incorrectTries = q.getNumWrong1() + q.getNumWrong2();
			System.out.println("Number of times answered incorrectly: " + incorrectTries);
			double firstPercent = q.getPercent1();
			System.out.printf("Percent correct answers on first try: %.2f%%\n", firstPercent);
			System.out.printf("Percent correct answers on second try: %.2f%%\n", q.getPercent2());
			System.out.println();
			if (firstPercent >= easyPercent) {
				easyQuestion = q.getQuestion();
				easyPercent = firstPercent;
			}
			if (firstPercent <= hardPercent) {
				hardQuestion = q.getQuestion();
				hardPercent = firstPercent;
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
			String[] answers = q.getAnswersArray();
			for (int j = 0; j < answers.length; j++) {
				quizWriter.println(answers[j]);
			}
			quizWriter.println(q.getCAnswerNum());
			quizWriter.println(q.getHint());
			quizWriter.println(q.getNumRight1());
			quizWriter.println(q.getNumWrong1());
			quizWriter.println(q.getNumRight2());
			quizWriter.println(q.getNumWrong2());
		}
		quizWriter.close();

		PrintWriter userWriter = new PrintWriter(userFile);
		for (int i = 0; i < users.size(); i++) {
			User u = users.get(i);
			userWriter.println(u.getName());
			userWriter.println(u.getNumQRight());
			userWriter.println(u.getNumQWrong());
		}
		userWriter.close();



	}
}