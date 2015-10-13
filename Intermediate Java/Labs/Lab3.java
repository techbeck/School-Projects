import java.util.Scanner;

public class Lab3 {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);
		int base;
		do {
			base = 0;
			int x = 0;
			int y = 0;
			int x2;
			System.out.println("Please enter an integer. " +
							"(A number less than 2 will quit the program.)");
			base = keyboard.nextInt();
			if (base < 2) {
				System.exit(0);
			}
			do {
				System.out.println("Please enter a positive integer");
				x = keyboard.nextInt();
			} while (x <= 0);
			x2 = x;
			do {
				x2 = x2/base;
				y++;
			} while (x2 >= base);
			System.out.printf("The floor of the log base %d of %d is %d\n", base, x, y);
		} while (base > 1);
	}
}