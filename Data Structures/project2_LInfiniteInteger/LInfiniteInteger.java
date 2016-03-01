
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
		String[] digits = s.split("");
		Node currentNode;
		Node previousNode = null;
		for (int i = 0; i < digits.length; i++)
		{
			int currentDigit = Integer.parseInt(digits[i]);
			currentNode = new Node(previousNode, currentDigit, null);
			if (previousNode == null)
			{
				firstNode = currentNode;
			}
			else
			{
				previousNode.next = currentNode;
			}
			lastNode = currentNode;
			previousNode = currentNode;
			numberOfDigits++;
		}
	}

	/**
	 * Constructor: Constructs this infinite integer from an integer.
	 * @param anInteger  an integer
	 */
	public LInfiniteInteger(int anInteger)
	{
		if (anInteger == 0)
		{
			firstNode = new Node(0);
			lastNode = firstNode;
			numberOfDigits = 1;
		}
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
		return numberOfDigits;
	}

	/**
	 * Checks whether this infinite integer is a negative number.
	 * @return true if this infinite integer is a negative number.
	 * Otherwise, return false.
	 */
	public boolean isNegative()
	{
		return isNegative;
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
		LInfiniteInteger otherInteger = (LInfiniteInteger) anInfiniteInteger;
		LInfiniteInteger thisAbs = this.getAbsoluteValue();
		LInfiniteInteger otherAbs = otherInteger.getAbsoluteValue();
		LInfiniteInteger result = null;
		if (this.isNegative == false && otherInteger.isNegative == false)
		{
			//positive + positive
			result = thisAbs.addAbsoluteValues(otherAbs);
			return result;
		}
		if (this.isNegative && otherInteger.isNegative)
		{
			//negative + negative
			result = thisAbs.addAbsoluteValues(otherAbs);
			result.isNegative = true;
			return result;
		}
		if (thisAbs.compareTo(otherAbs) == 0)
		{
			return new LInfiniteInteger(0);
		}
		if (this.isNegative)
		{
			if (thisAbs.compareTo(otherAbs) == 1)
			{
				//-big + small
				result = thisAbs.subtractAbsoluteValues(otherAbs);
				result.isNegative = true;
				return result;
			}
			else
			{
				//-small + big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				return result;
			}
		}
		else
		{
			if (thisAbs.compareTo(otherAbs) == 1)
			{
				//big + -small
				result = thisAbs.subtractAbsoluteValues(otherAbs);
				return result;
			}
			else
			{
				//small + -big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				result.isNegative = true;
				return result;
			}	
		}
	}

	/**
	 * Calculates the result of this infinite integer subtracted by anInfiniteInteger
	 * @param anInfiniteInteger the infinite integer to subtract.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer subtracted by anInfiniteInteger
	 */
	public InfiniteIntegerInterface minus(final InfiniteIntegerInterface anInfiniteInteger)
	{
		LInfiniteInteger otherInteger = (LInfiniteInteger) anInfiniteInteger;
		LInfiniteInteger thisAbs = this.getAbsoluteValue();
		LInfiniteInteger otherAbs = otherInteger.getAbsoluteValue();
		LInfiniteInteger result = null;
		if (this.isNegative == false && otherInteger.isNegative)
		{
			//positive - negative
			return thisAbs.addAbsoluteValues(otherAbs);
		}
		if (this.isNegative && otherInteger.isNegative == false)
		{
			//negative - positive
			result = thisAbs.addAbsoluteValues(otherAbs);
			result.isNegative = true;
			return result;
		}
		if (this.isNegative)
		{
			if (thisAbs.compareTo(otherAbs) == 1)
			{
				//-big - -small
				result = thisAbs.subtractAbsoluteValues(otherAbs);
				result.isNegative = true;
				return result;
			}
			if (otherAbs.compareTo(thisAbs) == 1)
			{
				//-small - -big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				return result;
			}
		}
		return null;
	}

	private LInfiniteInteger addAbsoluteValues(LInfiniteInteger otherInteger)
	{
		LInfiniteInteger sum = new LInfiniteInteger(0);
		int carryOut = 0;
		int partialSum = 0;
		Node thisCurrentNode = this.lastNode;
		Node otherCurrentNode = otherInteger.lastNode;
		Node sumCurrentNode = sum.lastNode;
		Node sumNextNode = null;
		while (thisCurrentNode != null && otherCurrentNode != null)
		{
			partialSum = thisCurrentNode.data + otherCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumCurrentNode = new Node(null, partialSum, sumNextNode);
			if (sumNextNode == null)
			{
				sum.lastNode = sumCurrentNode;
			}
			else
			{
				sumNextNode.previous = sumCurrentNode;
			}
			sum.firstNode = sumCurrentNode;
			sumNextNode = sumCurrentNode;
			sum.numberOfDigits = sum.numberOfDigits + 1;
			thisCurrentNode = thisCurrentNode.previous;
			otherCurrentNode = otherCurrentNode.previous;
		}
		while (thisCurrentNode != null)
		{
			partialSum = thisCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumCurrentNode = new Node(null, partialSum, sumNextNode);
			if (sumNextNode == null)
			{
				sum.lastNode = sumCurrentNode;
			}
			else
			{
				sumNextNode.previous = sumCurrentNode;
			}
			sum.firstNode = sumCurrentNode;
			sumNextNode = sumCurrentNode;
			sum.numberOfDigits = sum.numberOfDigits + 1;
			thisCurrentNode = thisCurrentNode.previous;
		}
		while (otherCurrentNode != null)
		{
			partialSum = otherCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumCurrentNode = new Node(null, partialSum, sumNextNode);
			if (sumNextNode == null)
			{
				sum.lastNode = sumCurrentNode;
			}
			else
			{
				sumNextNode.previous = sumCurrentNode;
			}
			sum.firstNode = sumCurrentNode;
			sumNextNode = sumCurrentNode;
			sum.numberOfDigits = sum.numberOfDigits + 1;
			otherCurrentNode = otherCurrentNode.previous;
		}
		if (carryOut > 0)
		{
			sumCurrentNode = new Node(null, carryOut, sumNextNode);
			sumNextNode.previous = sumCurrentNode;
			sum.firstNode = sumCurrentNode;
			sum.numberOfDigits = sum.numberOfDigits + 1;
		}
		return sum;
	}

	private LInfiniteInteger subtractAbsoluteValues(LInfiniteInteger otherInteger)
	{
		LInfiniteInteger diff = new LInfiniteInteger(0);
		int carryIn = 0;
		int partialDiff = 0;
		int currentInt = 0;
		Node thisCurrentNode = this.lastNode;
		Node otherCurrentNode = otherInteger.lastNode;
		Node diffCurrentNode = diff.lastNode;
		Node diffNextNode = null;
		while (thisCurrentNode != null && otherCurrentNode != null)
		{
			currentInt = thisCurrentNode.data - carryIn;
			if ((currentInt - otherCurrentNode.data) < 0)
			{
				carryIn = 1;
				currentInt = currentInt + 10;
			}
			else
			{
				carryIn = 0;
			}
			partialDiff = currentInt - otherCurrentNode.data;
			diffCurrentNode = new Node(null, partialDiff, diffNextNode);
			if (diffNextNode == null)
			{
				diff.lastNode = diffCurrentNode;
			}
			else
			{
				diffNextNode.previous = diffCurrentNode;
			}
			diff.firstNode = diffCurrentNode;
			diffNextNode = diffCurrentNode;
			diff.numberOfDigits = diff.numberOfDigits + 1;
			thisCurrentNode = thisCurrentNode.previous;
			otherCurrentNode = otherCurrentNode.previous;
		}
		while (thisCurrentNode != null)
		{
			currentInt = thisCurrentNode.data - carryIn;
			carryIn = 0;
			diffCurrentNode = new Node(null, currentInt, diffNextNode);
			if (diffNextNode == null)
			{
				diff.lastNode = diffCurrentNode;
			}
			else
			{
				diffNextNode.previous = diffCurrentNode;
			}
			diff.firstNode = diffCurrentNode;
			diffNextNode = diffCurrentNode;
			diff.numberOfDigits = diff.numberOfDigits + 1;
			thisCurrentNode = thisCurrentNode.previous;
		}
		return diff;
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
		if (isNegative)
		{
			s = "-";
		}
		while (currentNode != null)
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
	public int compareTo(final InfiniteIntegerInterface anInfiniteInteger)
	{
		LInfiniteInteger otherInteger = (LInfiniteInteger) anInfiniteInteger;
		if (this.isNegative && otherInteger.isNegative == false)
		{
			return -1;
		}
		if (this.isNegative == false && otherInteger.isNegative)
		{
			return 1;
		}
		Node thisCurrentNode = this.firstNode;
		Node otherCurrentNode = otherInteger.firstNode;
		if (this.isNegative)
		{
			//further from 0 is smaller
			if (numberOfDigits < otherInteger.getNumberOfDigits())
			{
				return 1;
			}
			if (numberOfDigits > otherInteger.getNumberOfDigits())
			{
				return -1;
			}
			while (thisCurrentNode != null)
			{
				if (thisCurrentNode.data < otherCurrentNode.data)
				{
					return 1;
				}
				if (thisCurrentNode.data > otherCurrentNode.data)
				{
					return -1;
				}
				thisCurrentNode = thisCurrentNode.next;
				otherCurrentNode = otherCurrentNode.next;
			}
			return 0;
		}
		else
		{
			//further from 0 is larger
			if (numberOfDigits > otherInteger.getNumberOfDigits())
			{
				return 1;
			}
			if (numberOfDigits < otherInteger.getNumberOfDigits())
			{
				return -1;
			}
			while (thisCurrentNode != null)
			{
				if (thisCurrentNode.data > otherCurrentNode.data)
				{
					return 1;
				}
				if (thisCurrentNode.data < otherCurrentNode.data)
				{
					return -1;
				}
				thisCurrentNode = thisCurrentNode.next;
				otherCurrentNode = otherCurrentNode.next;
			}
			return 0;
		}
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
	*Added methods from here.
	*/

	private LInfiniteInteger getAbsoluteValue()
	{
		LInfiniteInteger abs = new LInfiniteInteger(this.toString());
		abs.isNegative = false;
		return abs;
	}

	/**
	*To here
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
