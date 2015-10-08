/*
Rebecca Addison
CS 0401
Lecture Section 1060
Lab Section 1080

Program simulates shop where pizza, cherry pie, and gold pi charms can be purchased.
Members receive discounts and payment can be with cash or credit.
*/
import java.util.*;
import java.io.*;

public class PieShop {
	static Scanner keyboard = new Scanner(System.in);

	public static void main(String[] args) throws IOException {
		HashSet<String> memberNames = new HashSet<String>();
		HashSet<String> invalidNumbers = new HashSet<String>();
		int customer, pieCard, customerChoice, paymentChoice;
		String cardNumber;
		double plainPizzaCost = 10.00;
		double pepperoniPizzaCost = 12.00;
		double slicePieCost = 2.00;
		double wholePieCost = 10.00;
		double charmCost = 50.00;
		double subtotal, tax, total, cashReceived, change;
		double plainPizzaSubtotal = 0.00;
		double pepperoniPizzaSubtotal = 0.00;
		double slicePieSubtotal = 0.00;
		double wholePieSubtotal = 0.00;
		double charmSubtotal = 0.00;
		
		File namesFile = new File("piecardmembers.txt");
		if (!namesFile.exists())
		{
			System.out.println("Missing data file.");
			System.exit(0);
		}
		Scanner namesReader = new Scanner(namesFile);
		while (namesReader.hasNext()) {
			memberNames.add(namesReader.nextLine());
		}
		namesReader.close();
		File numbersFile = new File("invalidnumbers.txt");
		if (!numbersFile.exists())
		{
			System.out.println("Missing data file.");
			System.exit(0);
		}
		Scanner numbersReader = new Scanner(numbersFile);
		while (numbersReader.hasNext()) {
			invalidNumbers.add(numbersReader.nextLine());
		}

		customer = checkForCustomer();
		while (customer != 2) { // runs as long as answer is not no
			int qtyPlainPizza = 0;
			int qtyPepperoniPizza = 0;
			int totalSlicesPie = 0;
			int qtySlicesPie = 0;
			int qtyWholePie = 0;
			int qtyCharms = 0;

			System.out.print("What's your name? ");
			String customerName = keyboard.nextLine();
			if (memberNames.contains(customerName)) {
				pieCard = 1;
			} else {
				System.out.println("Your name is not on the list of Pie Card Members.");
				pieCard = 2;
			}

			if (pieCard == 1) {
				pepperoniPizzaCost = plainPizzaCost;
				slicePieCost -= 0.25;
				wholePieCost -= 2.00;
				charmCost *= 0.9;
				System.out.println("Welcome Back, Pie Card holder!\n" +
									"You will receive:\n" + 
										"\tPepperoni pizza for the price of plain\n" +
										"\t$0.25 off a slice of cherry pie\n" +
										"\t$2.00 off a whole cherry pie\n" +
										"\t$10.00% off each Pi charm");
			} // updates prices for pie card members

			do {
				System.out.println("Please choose an option:\n" +
									"\t1) Update Pizza Order\n" +
									"\t2) Update Cherry Pie Order\n" +
									"\t3) Update Charm Order\n" +
									"\t4) Check Out");
				customerChoice = keyboard.nextInt();

				switch (customerChoice) {
				case 1: // Pizza
					System.out.println("Here is your current order:");
					if (qtyPlainPizza == 0 && qtyPepperoniPizza == 0) {
						System.out.println("\tNo pizzas ordered");
					} else {
						System.out.printf("\t%d plain pizzas\n", qtyPlainPizza);
						System.out.printf("\t%d pepperoni pizzas\n", qtyPepperoniPizza);
					}
					System.out.printf("How many plain pizzas would you like for $%.2f each?\n", plainPizzaCost);
					System.out.println("(Positive input for adding pizzas or negative input for removing pizzas.)");
					int plainPizzaIn = keyboard.nextInt();
					if (plainPizzaIn < 0) {
						plainPizzaIn = Math.abs(plainPizzaIn);
						if (qtyPlainPizza >= plainPizzaIn) {
							qtyPlainPizza -= plainPizzaIn;
							System.out.printf("\t%d plain pizzas taken off order\n", plainPizzaIn);
						} else {
							System.out.println("Can't remove more pizzas than ordered");
						}
					} else {
						qtyPlainPizza += plainPizzaIn;
						System.out.printf("\t%d plain pizzas added to order\n", plainPizzaIn);
					}
					System.out.printf("How many pepperoni pizzas would you like for $%.2f each?\n", pepperoniPizzaCost);
					System.out.println("(Positive input for adding pizzas or negative input for removing pizzas.)");
					int pepperoniPizzaIn = keyboard.nextInt();
					if (pepperoniPizzaIn < 0) {
						pepperoniPizzaIn = Math.abs(plainPizzaIn);
						if (qtyPepperoniPizza >= pepperoniPizzaIn) {
							qtyPepperoniPizza -= pepperoniPizzaIn;
							System.out.printf("\t%d pepperoni pizzas taken off order\n", plainPizzaIn);
						} else {
							System.out.println("Can't remove more pizzas than ordered");
						}
					} else {
						qtyPepperoniPizza += pepperoniPizzaIn;
						System.out.printf("\t%d pepperoni pizzas added to order\n", pepperoniPizzaIn);
					}
					break;
				case 2: // Pie
					System.out.println("Here is your current order:");
					if (qtySlicesPie == 0 && qtyWholePie == 0) {
						System.out.println("\tNo cherry pie slices ordered");
					} else {
						System.out.printf("\t%d whole cherry pies\n", qtyWholePie);
						System.out.printf("\t%d cherry pie slices\n", qtySlicesPie);
					}
					System.out.println("How many cherry pie slices would you like for:");
					System.out.printf("\t$%.2f per slice\n", slicePieCost);
					System.out.printf("\t$%.2f per pie (6 slices)\n", wholePieCost);
					int pieIn = keyboard.nextInt();
					if (pieIn < 0) {
						pieIn = Math.abs(pieIn);
						if (totalSlicesPie >= pieIn) {
							totalSlicesPie -= pieIn;
							System.out.printf("\t%d slices of pie taken off order\n", pieIn);
						} else {
							System.out.println("Can't remove more slices than ordered");
						}
					} else {
						totalSlicesPie += pieIn;
						System.out.printf("\t%d slices of pie added to order\n", pieIn);
					}
					qtySlicesPie = totalSlicesPie % 6;
					qtyWholePie = totalSlicesPie / 6;
					break;
				case 3: // Charm
					System.out.println("Here is your current order:");
					if (qtyCharms == 0) {
						System.out.println("\tNo gold Pi charms ordered");
					} else {
						System.out.printf("\t%d gold Pi charms\n", qtyCharms);
					}
					System.out.printf("How many Pi charms would you like for $%.2f each?\n", charmCost);
					System.out.println("(Positive input for adding charms or negative input for removing charms.)");
					int charmIn = keyboard.nextInt();
					if (charmIn < 0) {
						charmIn = Math.abs(charmIn);
						if (qtyCharms >= charmIn) {
							qtyCharms -= charmIn;
							System.out.printf("\t%d charms taken off order\n", charmIn);
						} else {
							System.out.println("Can't remove more charms than ordered");
						}
					} else {
						qtyCharms += charmIn;
						System.out.printf("\t%d charms added to order\n", charmIn);
					}
					break;
				case 4: // Check Out
					// exits loop to continue simulation
					break;
				default:
					// invalid answer: causes loop to repeat
				}
			} while (customerChoice != 4); // keeps asking until Check Out is chosen

			if ((qtyPlainPizza+qtyPepperoniPizza+qtySlicesPie+qtyWholePie+qtyCharms) > 0) {
				System.out.println("Here is your subtotal:");
				
				plainPizzaSubtotal = plainPizzaCost * qtyPlainPizza;
				pepperoniPizzaSubtotal = pepperoniPizzaCost * qtyPepperoniPizza;
				wholePieSubtotal = wholePieCost * qtyWholePie;
				slicePieSubtotal = slicePieCost * qtySlicesPie;
				charmSubtotal = charmCost * qtyCharms;
				subtotal = plainPizzaSubtotal + pepperoniPizzaSubtotal + slicePieSubtotal + wholePieSubtotal + charmSubtotal;

				String text;
				if (plainPizzaSubtotal != 0.00) {
					text = String.format("%d %s at $%.2fea.: ", qtyPlainPizza, "plain pizzas", plainPizzaCost);
					System.out.printf("\t%-45s $%-10.2f\n", text, plainPizzaSubtotal);
				}
				if (pepperoniPizzaSubtotal != 0.00) {
					text = String.format("%d %s at $%.2fea.: ", qtyPepperoniPizza, "pepperoni pizzas", pepperoniPizzaCost);
					System.out.printf("\t%-45s $%-10.2f\n", text, pepperoniPizzaSubtotal);
				}
				if (wholePieSubtotal != 0.00) {
					text = String.format("%d %s at $%.2fea.: ", qtyWholePie, "whole cherry pies", wholePieCost);
					System.out.printf("\t%-45s $%-10.2f\n", text, wholePieSubtotal);
				}
				if (slicePieSubtotal != 0.00) {
					text = String.format("%d %s at $%.2fea.: ", qtySlicesPie, "cherry pie slices", slicePieCost);
					System.out.printf("\t%-45s $%-10.2f\n", text, slicePieSubtotal);
				}
				if (charmSubtotal != 0.00) {
					text = String.format("%d %s at $%.2fea.: ", qtyCharms, "14K gold Pi charms", charmCost);
					System.out.printf("\t%-45s $%-10.2f\n", text, charmSubtotal);
				}

				System.out.printf("\t%-45s %-10s \n", "", "-------");
				System.out.printf("\t%-45s $%-10.2f\n", "Subtotal:", subtotal);

				if (pieCard == 1) {
					double discount = subtotal * 0.1;
					subtotal -= discount;
					System.out.printf("\t%-45s ($%-5.2f)\n", "Bonus discount of 10%:", discount);
					System.out.printf("\t%-45s %-10s \n", "", "-------");
					System.out.printf("\t%-45s $%-10.2f\n", "New subtotal:", subtotal);
				}

				tax = subtotal * 0.07;
				total = subtotal + tax;
				System.out.printf("\t%-45s $%-10.2f\n", "Tax:", tax);
				System.out.printf("\t%-45s %-10s \n", "", "-------");
				System.out.printf("\t%-45s $%-10.2f\n\n", "Total:", total);

				int repeat = -1;
				do {
					System.out.println("Would you like to pay with cash or a credit card?");
					System.out.println("(1 = cash, 2 = credit) ");
					paymentChoice = keyboard.nextInt();
					keyboard.nextLine();
					if (paymentChoice == 1 || paymentChoice == 2) {
						repeat = 1;
					}
				} while (repeat == -1);
				
				switch(paymentChoice) {
					case 1: // cash
						do {
							System.out.printf("\t%-45s $", "Please enter your payment amount:");
							cashReceived = keyboard.nextDouble();
						} while (cashReceived < total);
						change = cashReceived - total;
						System.out.printf("\t%-45s $%-10.2f\n", "Your change:", change);
						System.out.println("Thank you for shopping at P,P,&P!\n");
						break;
					case 2: // credit
						do {
							System.out.println("Enter your credit card number");
							cardNumber = keyboard.nextLine();
							cardNumber = cardNumber.replace(" ", "");
							int length = cardNumber.length();
							if (length != 16 && length != 15) {
								repeat = -1;
							} else if (invalidNumbers.contains(cardNumber)) {
								repeat = -1;
							} else {
								repeat = 1;
								System.out.println("Payment Accepted");
								System.out.println("Thank you for shopping at P,P,&P!\n");
							}
							numbersReader.close();
						} while (repeat == -1);
						break;
				}
			} else {
				System.out.println("Nothing in your order. Thank you for stopping by.\n");
			}

			customer = checkForCustomer();
		} // end of customer while
	}

	public static int checkForCustomer() {
		System.out.println("Welcome to Pies, Pies, and Pis");
		System.out.print("Is there a customer in line? (1 = yes, 2 = no) ");
		int customer = keyboard.nextInt();
		keyboard.nextLine();
		return customer;
	}
}