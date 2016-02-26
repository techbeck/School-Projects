
public class LInfiniteInteger implements InfiniteIntegerInterface
{
	private Node firstNode;
	private Node lastNode;
	private int numberOfDigits;
	private boolean isNegative;
	
	/**
	 * Constructor: Constructs this infinite integer from a string
	 * representing an integer.
	 * @param s  a string represents an integer
	 */
	public LInfiniteInteger(String s)
	{
		numberOfDigits = 0;
		if (s.charAt(0) == '-')
		{
			isNegative = true;
			s = s.substring(1);
		}
		s = s.replaceFirst("^0+(?!$)", "");
		String[] chars = s.split("");
		numberOfDigits = chars.length;
		for (int i = 0; i < numberOfDigits; i++)
		{
			//TO DO
		}
	}

	/**
	 * Constructor: Constructs this infinite integer from an integer.
	 * @param anInteger  an integer
	 */
	public LInfiniteInteger(int anInteger)
	{
		numberOfDigits = 0;
		if (anInteger < 0)
		{
			isNegative = true;
			anInteger = anInteger + (-2*anInteger);
		}
		Node currentNode;
		Node nextNode = null;
		while (anInteger > 0)
		{
			currentNode = new Node(null, anInteger % 10, nextNode);
			if (nextNode == null)
			{
				lastNode = currentNode;
			}
			else
			{
				nextNode.previous = currentNode;
			}
			firstNode = currentNode;
			nextNode = currentNode;
			anInteger = anInteger / 10;
			numberOfDigits++;
		}
	}

	/**
	 * Gets the number of digits of this infinite integer.
	 * @return an integer representing the number of digits
	 * of this infinite integer.
	 */
	public int getNumberOfDigits()
	{
		// TO DO
		return 0;
	}

	/**
	 * Checks whether this infinite integer is a negative number.
	 * @return true if this infinite integer is a negative number.
	 * Otherwise, return false.
	 */
	public boolean isNegative()
	{
		// TO DO
		return false;
	}

	/**
	 * Calculates the result of this infinite integer plus anInfiniteInteger
	 * @param anInfiniteInteger the infinite integer to be added to this
	 * infinite integer.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer plus anInfiniteInteger
	 */
	public InfiniteIntegerInterface plus(final InfiniteIntegerInterface anInfiniteInteger)
	{		
		// TO DO
		return null;
	}

	/**
	 * Calculates the result of this infinite integer subtracted by anInfiniteInteger
	 * @param anInfiniteInteger the infinite integer to subtract.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer subtracted by anInfiniteInteger
	 */
	public InfiniteIntegerInterface minus(final InfiniteIntegerInterface anInfiniteInteger)
	{
		// TO DO
		return null;
	}
	
	/**
	 * Generates a string representing this infinite integer. If this infinite integer
	 * is a negative number a minus symbol should be in the front of numbers. For example,
	 * "-12345678901234567890". But if the infinite integer is a positive number, no symbol
	 * should be in the front of the numbers (e.g., "12345678901234567890").
	 * @return a string representing this infinite integer number.
	 */
	public String toString()
	{
		String s = new String();
		Node currentNode = firstNode;
		if(isNegative)
		{
			s = "-";
		}
		while(currentNode != null)
		{
			s = s + currentNode.data;
			currentNode = currentNode.next;
		}
		return s;
	}
	
	/**
	 * Compares this infinite integer with anInfiniteInteger
	 * @return either -1, 0, or 1 as follows:
	 * If this infinite integer is less than anInfiniteInteger, return -1.
	 * If this infinite integer is equal to anInfiniteInteger, return 0.
	 * If this infinite integer is greater than anInfiniteInteger, return 1.
	 */
	public int compareTo(InfiniteIntegerInterface anInfiniteInteger)
	{
		// TO DO
		return 0;
	}

	/**
	 * Calculates the result of this infinite integer multiplied by anInfiniteInteger
	 * @param anInfiniteInteger the multiplier.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer multiplied by anInfiniteInteger.
	 */
	public InfiniteIntegerInterface multiply(final InfiniteIntegerInterface anInfiniteInteger)
	{
		// TO DO
		return null;
	}

	/**
	Remove from here. For testing only.
	*/
	public int getFirstData()
	{
		if (firstNode == null)
			return 10;
		return firstNode.data;
	}

	public int getLastData()
	{
		if (lastNode == null)
			return 10;
		return lastNode.data;
	}


	/**
	To here
	*/
	
	private class Node
	{
		private int data;
		private Node next;
		private Node previous;
		
		private Node(Node previousNode, int aData, Node nextNode)
		{
			previous = previousNode;
			data = aData;
			next = nextNode;
		}
		
		private Node(int aData)
		{
			this(null, aData, null);
		}
	}
}
