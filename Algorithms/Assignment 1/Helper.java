import java.util.*;
import java.io.*;

public class Helper
{
	public static void main(String[] args) throws IOException
	{
		Scanner scan = new Scanner(new File("dictionary.txt"));
		ArrayList<PrintWriter> printers = new ArrayList<PrintWriter>();
		ArrayList<ArrayList<String>> lists = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < 22; i++)
		{
			lists.add(new ArrayList<String>());
			printers.add(new PrintWriter(new File(i + "letters.txt")));
		}
		while (scan.hasNext())
		{
			String s = scan.nextLine();
			ArrayList<String> list = lists.get(s.length());
			list.add(s);
		}
		for (int i = 0; i < lists.size(); i++)
		{
			ArrayList<String> list = lists.get(i);
			for (int j = 0; j < list.size(); j++)
			{
				printers.get(i).println(list.get(j));
			}
		}
		for (int i = 0; i < 22; i++)
		{
			printers.get(i).close();
		}
	}
}