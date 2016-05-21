/*
Rebecca Addison
CS 1501
*/

import java.util.*;
import java.io.*;

/**
Program prints anagrams found from strings read from a file to a separate file.
*/
public class Anagrams
{

	static SortedSet<String> anagrams;
	static DictInterface dict = new MyDictionary();

	public static void main(String[] args)
	{
		// Generate dictionary and get input strings
		File dictFile = new File("dictionary.txt");
		File inputFile = null;
		File outputFile = null;
		Scanner dictScan = null;
		Scanner inScan = null;
		PrintWriter outWriter = null;
		try
		{
			inputFile = new File(args[0]);
			outputFile = new File(args[1]);
			dictScan = new Scanner(dictFile);
			inScan = new Scanner(inputFile);
			outWriter = new PrintWriter(outputFile);
		}
		catch (IOException io)
		{
			System.out.println(io);
			System.exit(0);
		}
		catch (Exception e)
		{
			System.out.println("Need command line argument(s) for file name(s)");
			System.out.println(e);
			System.exit(0);
		}
		while (dictScan.hasNext())
		{
			dict.add(dictScan.nextLine());
		}
		ArrayList<String> input = new ArrayList<String>();
		while (inScan.hasNext())
		{
			input.add(inScan.nextLine());
		}
		
		// Generate anagrams output file
		for (String str : input)
		{
			// create new object for each anagram set
			anagrams = new TreeSet<String>();
			outWriter.printf("Here are the results for %s:\n", str);
			outWriter.println("1 word solutions:");
			getAnagrams(str);
			for (String anagram : anagrams)
			{
				outWriter.println(anagram);
			}
			outWriter.println();
		}

		dictScan.close();
		inScan.close();
		outWriter.close();
	}

	public static void getAnagrams(String s)
	{
		char[] carray = s.toCharArray();
		ArrayList<Character> chars = new ArrayList<Character>();
		for (int i = 0; i < carray.length; i++)
		{
			chars.add(carray[i]);
		}
		StringBuilder anagram;
		for (int i = 0; i < chars.size(); i++)
		{
			char character = chars.get(0);
			anagram = new StringBuilder();
			anagram.append(character);
			chars.remove(0);
			recAnagrams(anagram, chars);
			chars.add(character);
		}
	}

	public static void recAnagrams(StringBuilder anagram, ArrayList<Character> chars)
	{
		int status = dict.searchPrefix(anagram);
		if (chars.isEmpty())
		{
			if (status == 2 || status == 3)
			{
				anagrams.add(anagram.toString());
			}
			return;
		}
		switch (status) {
			case 0:		// anagram is neither 
				return;
			case 1:		// anagram is prefix not word, and chars not used up
				int charsSize = chars.size();
				for (int i = 0; i < charsSize; i++)
				{
					char character = chars.get(0);
					anagram.append(character);
					chars.remove(0);
					recAnagrams(anagram, chars);
					chars.add(character);
					anagram.deleteCharAt(anagram.length()-1);
				}
				return;
			case 2:		// anagram is word, not prefix, and chars not used up
				/**
					USE THIS FOR MULTI-WORD ANAGRAMS
					BE SURE THAT THE SEARCHPREFIX AT TOP ONLY HAS START OF NEW WORD,
							NOT THIS WORD TOO
				*/
				return;
			case 3:		// anagram is both word and prefix, and chars not used up
				int size = chars.size();
				for (int i = 0; i < size; i++)
				{
					char character = chars.get(0);
					anagram.append(character);
					chars.remove(0);
					recAnagrams(anagram, chars);
					chars.add(character);
					anagram.deleteCharAt(anagram.length()-1);
				}
				return;
		}
	}
}