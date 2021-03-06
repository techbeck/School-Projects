
public class FrequencyBag<T>
{
	// Instance Variables
	private Node firstNode;
	private int numberOfEntries;
	
	/**
	 * Constructor
	 * Constructs an empty frequency bag.
	 */
	public FrequencyBag()
	{
		firstNode = null;
		numberOfEntries = 0;
	}
	
	/**
	 * Adds new entry into this frequency bag.
	 * @param aData the data to be added into this frequency bag.
	 */
	public void add(T aData)
	{
		if (firstNode == null)
		{
			Node newNode = new Node(aData);
			firstNode = newNode;
			numberOfEntries++;
			return;
		}
		Node currentNode = firstNode;
		boolean found = false;
		while (currentNode != null)
		{
			if (currentNode.data.equals(aData))
			{
				found = true;
				currentNode.incrementFrequency();
				break;
			}
			currentNode = currentNode.next;
		}
		if (!found)
		{
			Node newNode = new Node(aData);
			newNode.next = firstNode;
			firstNode = newNode;
		}
		numberOfEntries++;
	}
	
	/**
	 * Gets the number of occurrences of aData in this frequency bag.
	 * @param aData the data to be checked for its number of occurrences.
	 * @return the number of occurrences of aData in this frequency bag.
	 */
	public int getFrequencyOf(T aData)
	{
		Node currentNode = firstNode;
		while (currentNode != null)
		{
			if (currentNode.data.equals(aData))
			{
				return currentNode.frequency;
			}
			currentNode = currentNode.next;
		}
		return 0;
	}

	/**
	 * Gets the maximum number of occurrences in this frequency bag.
	 * @return the maximum number of occurrences of an entry in this
	 * frequency bag.
	 */
	public int getMaxFrequency()
	{
		int maxFrequency = 0;
		Node currentNode = firstNode;
		while (currentNode != null)
		{
			if (currentNode.frequency > maxFrequency)
			{
				maxFrequency = currentNode.frequency;
			}
			currentNode = currentNode.next;
		}
		return maxFrequency;
	}
	
	/**
	 * Gets the probability of aData
	 * @param aData the specific data to get its probability.
	 * @return the probability of aData
	 */
	public double getProbabilityOf(T aData)
	{
		Node currentNode = firstNode;
		double probability = getFrequencyOf(aData)/ (double)numberOfEntries;
		return probability;
	}

	/**
	 * Empty this bag.
	 */
	public void clear()
	{
		firstNode = null;
		numberOfEntries = 0;
	}
	
	/**
	 * Gets the number of entries in this bag.
	 * @return the number of entries in this bag.
	 */
	public int size()
	{
		return numberOfEntries;
	}

	private class Node
	{
		private T data;
		private int frequency;
		private Node next;

		private Node(T entry)
		{
			data = entry;
			frequency = 1;
		}

		private void incrementFrequency()
		{
			frequency++;
		}
	}
}
