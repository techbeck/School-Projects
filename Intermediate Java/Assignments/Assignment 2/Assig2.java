/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080
*/

import java.util.Scanner;
import java.io.*;

public class Assig2 {
	public static void main(String[] args) throws IOException {
		Scanner keyboard = new Scanner(System.in);
		String firstName;
		Player player = new Player();

		System.out.print("Please enter your first name: ");
		firstName = keyboard.nextLine();
		File myFile = new File(firstName + ".txt");
		if (!myFile.exists()) {
			player.setFirstName(firstName);
			System.out.print("Please enter your last name: ");
			player.setLastName(keyboard.nextLine());
			System.out.print("Please enter how much money you have: ");
			player.setMoney(keyboard.nextDouble());
			System.out.print("Please enter how many rounds you've played: ");
			player.setTotalRounds(keyboard.nextInt());
			System.out.print("Please enter how many rounds you've won: ");
			player.setTotalRoundsWon(keyboard.nextInt());
			keyboard.nextLine();
		} else {
			player.setLastName(fileReader.nextLine());
			player.setFirstName(fileReader.nextLine());
			player.setMoney(fileReader.nextDouble());
			player.setTotalRounds(fileReader.nextInt());
			player.setTotalRoundsWon(fileReader.nextInt());
			fileReader.close();
		}

		System.out.printf("Welcome Back %s\n", player.getFirstName());
		System.out.println("Welcome to Over and Under!");
		System.out.println("Here is your current information:");
		System.out.println(player);

		System.out.print("Would you like to play a round? (y/n) ");
		String anotherRound = keyboard.nextLine();
		while (!anotherRound.equals("n") && player.getRemainingMoney() >0.00) {
			Double betAmount;
			do {
				System.out.printf("How much would you like to bet? (<= $%,.2f) ", player.getRemainingMoney());
				betAmount = keyboard.nextDouble();
				keyboard.nextLine();
			} while (betAmount > player.getRemainingMoney());
			Die die1 = new Die();
			Die die2 = new Die();
			int totalValue = die1.getValue() + die2.getValue();
			System.out.print("Select your choice: o(ver), u(nder), s(even) ");
			String playerGuess = keyboard.nextLine();
			System.out.println("The dice have been rolled...and the result is...");
			System.out.printf("Die1 = %d Die2 = %d => Total = %d\n", die1.getValue(), die2.getValue(), totalValue);
			if (totalValue != 7) {
				String msg = (totalValue>7) ? "OVER!" : "UNDER!";
				System.out.printf(".......%s.......\n", msg);
				if ((msg.equals("OVER!") && playerGuess.equals("o")) || (msg.equals("UNDER!") && playerGuess.equals("u"))) {
					playerWins(player, betAmount);
				} else {
					playerLoses(player, betAmount);
				}
			} else {
				System.out.println(".......SEVEN!.......");
				if (playerGuess.equals("s")) {
					playerWins(player, (4*betAmount));
				} else {
					playerLoses(player, betAmount);
				}
			}
			System.out.printf("Your updated money value is: $%,.2f\n", player.getRemainingMoney());
			System.out.print("Would you like to play a round (y/n)? ");
			anotherRound = keyboard.nextLine();
		}
		
		System.out.println("Thank you for playing Over and Under!");
		System.out.println("Here is your updated information:");
		System.out.println(player);
		System.out.printf("During this game you earned $%,.2f\n", player.getMoneyGained());
		System.out.printf("During this game you won %d rounds out of %d total\n", player.getRoundsWon(), player.getRoundsPlayed());

		PrintWriter outputWriter = new PrintWriter(firstName + ".txt");
		outputWriter.println(player.getLastName());
		outputWriter.println(player.getFirstName());
		outputWriter.println(player.getRemainingMoney());
		outputWriter.println(player.getTotalRounds());
		outputWriter.println(player.getTotalRoundsWon());
		outputWriter.close();
	}

	public static void playerWins(Player player, double money) {
		System.out.println("You have WON this round!");
		System.out.printf("You have won $%,.2f\n", money);
		player.addMoney(money);
		player.wonAGame();
	}

	public static void playerLoses(Player player, double money) {
		System.out.println("You have LOST this round!");
		System.out.printf("You have lost $%,.2f\n", money);
		player.subtractMoney(money);
		player.lostAGame();
	}
}