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
	static int wordCount;
	static DictInterface dict;
	static ArrayList<SortedSet<String>> anagrams;

	public static void main(String[] args)
	{
		// Generate dictionary and get input strings
		Scanner dictScan = null;
		Scanner inScan = null;
		PrintWriter outWriter = null;
		PrintWriter timeWriter = null;
		String dictType = null;
		try
		{
			dictType = args[2];
			dictScan = new Scanner(new File("dictionary.txt"));
			inScan = new Scanner(new File(args[0]));
			outWriter = new PrintWriter(new File(args[1]));
			timeWriter = new PrintWriter(new File("times.txt"));
		}
		catch (IOException io)
		{
			System.out.println(io);
			System.exit(0);
		}
		catch (Exception e)
		{
			System.out.println("Missing command line argument(s)");
			System.out.println(e);
			System.exit(0);
		}
		if (dictType.equals("orig"))
		{
			dict = new MyDictionary();
		}
		else if (dictType.equals("dlb"))
		{
			dict = new DLB();
		}
		else
		{
			System.out.println("Incorrect dictionary type.");
			System.exit(0);
		}
		while (dictScan.hasNext())
		{
			dict.add(dictScan.nextLine());
		}
		ArrayList<String> input = new ArrayList<String>();
		while (inScan.hasNext())
		{
			input.add(inScan.nextLine().replaceAll("\\s",""));
		}
		
		// Generate anagrams output file
		for (String str : input)
		{
			System.out.printf("Milliseconds elapsed for word %s: ", str);
			Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			long pastTime = cal1.getTimeInMillis();
			wordCount = 0;
			// create new object for each anagram set
			anagrams = new ArrayList<SortedSet<String>>();
			anagrams.add(new TreeSet<String>());
			outWriter.printf("Here are the results for %s:\n", str);
			StringBuilder anagram = new StringBuilder("");
			char[] cArray = str.toCharArray();
			ArrayList<Character> chars = new ArrayList<Character>();
			for (int i = 0; i < cArray.length; i++)
			{
				chars.add(cArray[i]);
			}
			getAnagrams(anagram, chars);
			for (int i = 0; i < anagrams.size(); i++)
			{
				SortedSet<String> set = anagrams.get(i);
				if (!set.isEmpty())
				{
					outWriter.printf("%d word solutions:\n", i+1);
					for (String anAnagram : set)
					{
						outWriter.println(anAnagram);
					}
				}
			}
			outWriter.println();
			Calendar cal2 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
			long currentTime = cal2.getTimeInMillis();
			long timeElapsed = currentTime - pastTime;
			System.out.println(timeElapsed);
			timeWriter.println(timeElapsed);
		}

		dictScan.close();
		inScan.close();
		outWriter.close();
		timeWriter.close();
	}

	private static void getAnagrams(StringBuilder anagram, ArrayList<Character> chars)
	{
		wordCount = 1;
		//ArrayList<Character> used = new ArrayList<Character>();
		for (int i = 0; i < chars.size(); i++)
		{
			// New anagram stringbuilder for each starting character
			anagram = new StringBuilder();
			char character = chars.get(0);
			/*if (used.contains(character))
			{
				continue;
			}*/
			anagram.append(character);
			chars.remove(0);
			//used.add(character);
			recAnagrams(anagram, chars);
			chars.add(character);
		}
	}

	private static void recAnagrams(StringBuilder anagram, ArrayList<Character> chars)
	{
		int start = anagram.lastIndexOf(" ")+1;
		int end = anagram.length()-1;
		int charsSize = chars.size();
		int status = dict.searchPrefix(anagram, start, end);
		if (charsSize == 0)
		{
			// If anagram is word and all characters are used up, add to anagrams set
			if (status == 2 || status == 3)
			{
				anagrams.get(wordCount-1).add(anagram.toString());
			}
			return;
		}
		switch (status) {
			case 0:
				// Anagram is neither prefix nor word
				return;
			case 1:
				// Anagram is prefix not word, and chars not used up
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
			case 2:
				// Anagram is word, not prefix, and chars not used up
				// Check for multi word anagrams
				anagram.append(" ");
				wordCount++;
				if (anagrams.size() <= wordCount)
				{
					anagrams.add(new TreeSet<String>());
				}
				for (int i = 0; i < charsSize; i++)
				{
					char character = chars.get(0);
					anagram.append(character);
					chars.remove(0);
					recAnagrams(anagram, chars);
					chars.add(character);
					anagram.deleteCharAt(anagram.length()-1);
				}
				anagram.deleteCharAt(anagram.length()-1);
				wordCount--;
				return;
			case 3:
				// Anagram is both word and prefix, and chars not used up
				// Check for multi word anagrams and continue checking for singles
				anagram.append(" ");
				wordCount++;
				if (anagrams.size() <= wordCount)
				{
					anagrams.add(new TreeSet<String>());
				}
				for (int i = 0; i < charsSize; i++)
				{
					char character = chars.get(0);
					anagram.append(character);
					chars.remove(0);
					recAnagrams(anagram, chars);
					chars.add(character);
					anagram.deleteCharAt(anagram.length()-1);
				}
				anagram.deleteCharAt(anagram.length()-1);
				wordCount--;
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
		}
	}
}