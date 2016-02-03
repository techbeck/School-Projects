import java.util.*;
import java.io.*;

public class CharacterFrequency
{
	public static void main(String[] args)
	{
		File letter1 = new File("letter1.txt");
		Scanner letterReader = null;
		try
		{
			letterReader = new Scanner(letter1);
		}
		catch (IOException io)
		{
			System.out.println("Reading IO Exception");
			System.exit(0);
		}
		FrequencyBag<Character> charBag = new FrequencyBag<Character>();
		while (letterReader.hasNext())
		{
			String word = letterReader.next();
			word = word.toLowerCase();
			char[] characters = word.toCharArray();
			for (int i = 0; i < characters.length; i++)
			{
				charBag.add(characters[i]);
			}
		}
		System.out.println("Character: Frequency\n" + 
							"====================");
		System.out.println("a: " + charBag.getFrequencyOf('a'));
		System.out.println("b: " + charBag.getFrequencyOf('b'));
		System.out.println("c: " + charBag.getFrequencyOf('c'));
		System.out.println("d: " + charBag.getFrequencyOf('d'));
		System.out.println("e: " + charBag.getFrequencyOf('e'));
		System.out.println("f: " + charBag.getFrequencyOf('f'));
		System.out.println("g: " + charBag.getFrequencyOf('g'));
		System.out.println("h: " + charBag.getFrequencyOf('h'));
		System.out.println("i: " + charBag.getFrequencyOf('i'));
		System.out.println("j: " + charBag.getFrequencyOf('j'));
		System.out.println("k: " + charBag.getFrequencyOf('k'));
		System.out.println("l: " + charBag.getFrequencyOf('l'));
		System.out.println("m: " + charBag.getFrequencyOf('m'));
		System.out.println("n: " + charBag.getFrequencyOf('n'));
		System.out.println("o: " + charBag.getFrequencyOf('o'));
		System.out.println("p: " + charBag.getFrequencyOf('p'));
		System.out.println("q: " + charBag.getFrequencyOf('q'));
		System.out.println("r: " + charBag.getFrequencyOf('r'));
		System.out.println("s: " + charBag.getFrequencyOf('s'));
		System.out.println("t: " + charBag.getFrequencyOf('t'));
		System.out.println("u: " + charBag.getFrequencyOf('u'));
		System.out.println("v: " + charBag.getFrequencyOf('v'));
		System.out.println("w: " + charBag.getFrequencyOf('w'));
		System.out.println("x: " + charBag.getFrequencyOf('x'));
		System.out.println("y: " + charBag.getFrequencyOf('y'));
		System.out.println("z: " + charBag.getFrequencyOf('z'));
	}
}