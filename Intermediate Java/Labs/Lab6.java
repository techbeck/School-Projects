import java.util.*;

public class Lab6 {
	public static void main(String[] args) {
		Scanner keyboard = new Scanner(System.in);

		System.out.print("How many values do you want to enter? ");
		int tryAgain = 1;
		int numValues = 0;
		do {
			try {
				numValues = keyboard.nextInt();
				tryAgain = 0;
			} catch (InputMismatchException ex) {
				System.out.println("Invalid input. Try again.");
				tryAgain = 1;
			}
		} while (tryAgain == 1);
		double[] data = new double[numValues];
		for (int index = 0; index < data.length; index++) {
			System.out.print("Enter value: ");
			do {
				try {
					data[index] = keyboard.nextDouble();
					tryAgain = 0;
				} catch (InputMismatchException ex) {
					System.out.println("Invalid input. Try again.");
					tryAgain = 1;
				}
			} while (tryAgain == 1);
		}
		System.out.printf("Maximum value: %.2f\n", max(data));
		System.out.printf("Minimum value: %.2f\n", min(data));
		System.out.printf("Sum: %.2f\n", sum(data));
		System.out.printf("Average: %.2f\n", ave(data));
	}

	public static double max(double[] data) {
		double max = data[0];
		for (int index = 0; index < data.length; index++) {
			if (data[index] > max) {
				max = data[index];
			}
		}
		return max;
	}

	public static double min(double[] data) {
		double min = data[0];
		for (int index = 0; index < data.length; index++) {
			if (data[index] < min) {
				min = data[index];
			}
		}
		return min;
	}

	public static double sum(double[] data) {
		double sum = 0.0;
		for (int index = 0; index < data.length; index++) {
			sum += data[index];
		}
		return sum;
	}

	public static double ave(double[] data) {
		double sum = sum(data);
		int numValues = data.length;
		return sum/numValues;
	}
}