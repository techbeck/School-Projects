
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
		Node previousNode = null;
		for (int i = 0; i < digits.length; i++)
		{
			int digit = Integer.parseInt(digits[i]);
			previousNode = addToEnd(previousNode, digit);
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
		else
		{
			numberOfDigits = 0;
			if (anInteger < 0)
			{
				isNegative = true;
				anInteger = anInteger + (-2*anInteger);
			}
			Node nextNode = null;
			while (anInteger > 0)
			{
				int digit = anInteger % 10;
				anInteger = anInteger / 10;
				nextNode = addToStart(nextNode, digit);
			}
		}
	}

	private Node addToStart(Node nextNode, int digit)
	{
		Node newNode = new Node(null, digit, nextNode);
		if (nextNode == null)
		{
			lastNode = newNode;
		}
		else
		{
			nextNode.previous = newNode;
		}
		firstNode = newNode;
		numberOfDigits++;
		return newNode;
	}

	private Node addToEnd(Node previousNode, int digit)
	{
		Node newNode = new Node(previousNode, digit, null);
		if (previousNode == null)
		{
			firstNode = newNode;
		}
		else
		{
			previousNode.next = newNode;
		}
		lastNode = newNode;
		numberOfDigits++;
		return newNode;
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
				return new LInfiniteInteger(result.toString());
			}
			else
			{
				//-small + big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				return new LInfiniteInteger(result.toString());
			}
		}
		else
		{
			if (thisAbs.compareTo(otherAbs) == 1)
			{
				//big + -small
				result = thisAbs.subtractAbsoluteValues(otherAbs);
				return new LInfiniteInteger(result.toString());
			}
			else
			{
				//small + -big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				result.isNegative = true;
				return new LInfiniteInteger(result.toString());
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
		if (this.compareTo(otherInteger) == 0)
		{
			return new LInfiniteInteger(0);
		}
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
				return new LInfiniteInteger(result.toString());
			}
			else
			{
				//-small - -big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				return new LInfiniteInteger(result.toString());
			}
		}
		else
		{
			if (thisAbs.compareTo(otherAbs) == 1)
			{
				//big - small
				result = thisAbs.subtractAbsoluteValues(otherAbs);
				return new LInfiniteInteger(result.toString());
			}
			else
			{
				//small - big
				result = otherAbs.subtractAbsoluteValues(thisAbs);
				result.isNegative = true;
				return new LInfiniteInteger(result.toString());
			}
		}
	}

	private LInfiniteInteger getAbsoluteValue()
	{
		LInfiniteInteger abs = new LInfiniteInteger(this.toString());
		abs.isNegative = false;
		return abs;
	}

	private LInfiniteInteger addAbsoluteValues(LInfiniteInteger otherInteger)
	{
		LInfiniteInteger sum = new LInfiniteInteger(0);
		int carryOut = 0;
		int partialSum = 0;
		Node thisCurrentNode = this.lastNode;
		Node otherCurrentNode = otherInteger.lastNode;
		Node sumNextNode = null;
		while (thisCurrentNode != null && otherCurrentNode != null)
		{
			partialSum = thisCurrentNode.data + otherCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumNextNode = sum.addToStart(sumNextNode, partialSum);
			thisCurrentNode = thisCurrentNode.previous;
			otherCurrentNode = otherCurrentNode.previous;
		}
		while (thisCurrentNode != null)
		{
			partialSum = thisCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumNextNode = sum.addToStart(sumNextNode, partialSum);
			thisCurrentNode = thisCurrentNode.previous;
		}
		while (otherCurrentNode != null)
		{
			partialSum = otherCurrentNode.data + carryOut;
			carryOut = partialSum / 10;
			partialSum = partialSum % 10;
			sumNextNode = sum.addToStart(sumNextNode, partialSum);
			otherCurrentNode = otherCurrentNode.previous;
		}
		if (carryOut > 0)
		{
			sum.addToStart(sumNextNode, carryOut);
		}
		sum.numberOfDigits--;
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
			if (currentInt < 0)
			{
				carryIn = 1;
				currentInt = currentInt + 10;
			}
			else
			{
				carryIn = 0;
			}
			diffCurrentNode = new Node(null, currentInt, diffNextNode);
			diffNextNode.previous = diffCurrentNode;
			diff.firstNode = diffCurrentNode;
			diffNextNode = diffCurrentNode;
			diff.numberOfDigits = diff.numberOfDigits + 1;
			thisCurrentNode = thisCurrentNode.previous;
		}
		return diff;
	}
	
	/**
	 * Calculates the result of this infinite integer multiplied by anInfiniteInteger
	 * @param anInfiniteInteger the multiplier.
	 * @return a NEW infinite integer representing the result of this
	 * infinite integer multiplied by anInfiniteInteger.
	 */
	public InfiniteIntegerInterface multiply(final InfiniteIntegerInterface anInfiniteInteger)
	{
		boolean makeFalse = false;
		LInfiniteInteger otherInteger = (LInfiniteInteger) anInfiniteInteger;
		if (this.isNegative() && otherInteger.isNegative() == false)
		{
			makeFalse = true;
		}
		else if (this.isNegative() == false && otherInteger.isNegative())
		{
			makeFalse = true;
		}
		LInfiniteInteger multiplier;
		LInfiniteInteger multiplicand;
		if (this.compareTo(anInfiniteInteger) == 1)
		{
			multiplicand = otherInteger.getAbsoluteValue();
			multiplier = this.getAbsoluteValue();
		}
		else
		{
			multiplicand = this.getAbsoluteValue();
			multiplier = otherInteger.getAbsoluteValue();
		}
		LInfiniteInteger[] multiplicandMultiples = new LInfiniteInteger[10];
		multiplicandMultiples[0] = new LInfiniteInteger(0);
		multiplicandMultiples[1] = new LInfiniteInteger(multiplicand.toString());
		multiplicandMultiples[2] = (LInfiniteInteger) multiplicandMultiples[1].plus(multiplicandMultiples[1]);
		multiplicandMultiples[3] = (LInfiniteInteger) multiplicandMultiples[2].plus(multiplicandMultiples[1]);
		multiplicandMultiples[4] = (LInfiniteInteger) multiplicandMultiples[3].plus(multiplicandMultiples[1]);
		multiplicandMultiples[5] = (LInfiniteInteger) multiplicandMultiples[4].plus(multiplicandMultiples[1]);
		multiplicandMultiples[6] = (LInfiniteInteger) multiplicandMultiples[5].plus(multiplicandMultiples[1]);
		multiplicandMultiples[7] = (LInfiniteInteger) multiplicandMultiples[6].plus(multiplicandMultiples[1]);
		multiplicandMultiples[8] = (LInfiniteInteger) multiplicandMultiples[7].plus(multiplicandMultiples[1]);
		multiplicandMultiples[9] = (LInfiniteInteger) multiplicandMultiples[8].plus(multiplicandMultiples[1]);
		LInfiniteInteger[] partialProducts = new LInfiniteInteger[multiplier.numberOfDigits];
		Node currentNode = multiplier.lastNode;
		for (int i = 0; i < partialProducts.length; i++)
		{
			partialProducts[i] = multiplicandMultiples[currentNode.data];
			for (int j = 0; j < i; j++)
			{
				partialProducts[i] = new LInfiniteInteger(partialProducts[i] + "0");
			}
			currentNode = currentNode.previous;
		}
		for (int i = 1; i < partialProducts.length; i++)
		{
			partialProducts[0] = (LInfiniteInteger) partialProducts[0].plus(partialProducts[i]);
		}
		LInfiniteInteger product = partialProducts[0];
		if (product.compareTo(multiplicandMultiples[0]) == 0)
		{
			return product.getAbsoluteValue();
		}
		if (makeFalse)
		{
			product.isNegative = true;
		}
		return product;
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
