import java.util.*;
import java.io.*;

public class Helper2
{
	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner(new File("times.txt"));
		int count = 0;
		int total = 0;
		while (scan.hasNext())
		{
			total += scan.nextInt();
			count++;
		}
		double avg = total/(double)count;
		System.out.println("count: " + count);
		System.out.println("avg: " + avg);
	}
}