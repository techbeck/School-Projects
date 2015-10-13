import java.util.Random;
import java.util.Scanner;

public class Lab4 {
	public static void main(String[] args) {
		Random randomNum = new Random();
		Scanner keyboard = new Scanner(System.in);
		int numRolls, rollAgain;
		do {
			System.out.print("Enter number of rolls: ");
			numRolls = keyboard.nextInt();
			RollDice(numRolls, randomNum);
			System.out.print("Enter 1 to roll again or 2 to exit: ");
			rollAgain = keyboard.nextInt();
		} while (rollAgain != 2);
	}

	public static void RollDice(int numRolls, Random rollValues) {
		double num2 = 0;
		double num3 = 0;
		double num4 = 0;
		double num5 = 0;
		double num6 = 0;
		double num7 = 0;
		double num8 = 0;
		double num9 = 0;
		double num10 = 0;
		double num11 = 0;
		double num12 = 0;
		for (int i = 0; i < numRolls; i++) {
			int roll = (rollValues.nextInt(6) + 1) + (rollValues.nextInt(6) + 1);
			switch (roll) {
				case 2:
					num2++;
					break;
				case 3:
					num3++;
					break;
				case 4:
					num4++;
					break;
				case 5:
					num5++;
					break;
				case 6:
					num6++;
					break;
				case 7:
					num7++;
					break;
				case 8:
					num8++;
					break;
				case 9:
					num9++;
					break;
				case 10:
					num10++;
					break;
				case 11:
					num11++;
					break;
				case 12:
					num12++;
					break;
			}
		}
		System.out.println("Value : Number of times it appears : Fraction : Compare to expected fraction");
		System.out.printf("2 : %.0f : %.3f : %.3f\n", num2, (num2/numRolls), (1.0/36));
		System.out.printf("3 : %.0f : %.3f : %.3f\n", num3, (num3/numRolls), (2.0/36));
		System.out.printf("4 : %.0f : %.3f : %.3f\n", num4, (num4/numRolls), (3.0/36));
		System.out.printf("5 : %.0f : %.3f : %.3f\n", num5, (num5/numRolls), (4.0/36));
		System.out.printf("6 : %.0f : %.3f : %.3f\n", num6, (num6/numRolls), (5.0/36));
		System.out.printf("7 : %.0f : %.3f : %.3f\n", num7, (num7/numRolls), (6.0/36));
		System.out.printf("8 : %.0f : %.3f : %.3f\n", num8, (num8/numRolls), (5.0/36));
		System.out.printf("9 : %.0f : %.3f : %.3f\n", num9, (num9/numRolls), (4.0/36));
		System.out.printf("10 : %.0f : %.3f : %.3f\n", num10, (num10/numRolls), (3.0/36));
		System.out.printf("11 : %.0f : %.3f : %.3f\n", num11, (num11/numRolls), (2.0/36));
		System.out.printf("12 : %.0f : %.3f : %.3f\n", num12, (num12/numRolls), (1.0/36));
	}
}