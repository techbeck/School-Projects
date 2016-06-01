public class DLB implements DictInterface
{
	private Node root;

	public DLB()
	{
		root = null;
	}

	public boolean add(String s)
	{
		if (s == null || s.length() == 0)
		{
			return false;
		}
		if (root == null)
		{
			root = new Node(s.charAt(0));
		}
		Node currentNode = root;
		int index;
		// Find common prefix
		for (index = 0; currentNode.child != null; index++)
		{
			boolean found = false;
			if (currentNode.character == s.charAt(index))
			{
				found = true;
			}
			while (currentNode.sibling != null && !found)
			{
				currentNode = currentNode.sibling;
				if (currentNode.character == s.charAt(index))
				{
					found = true;
				}
			}
			if (!found)
			{
				currentNode.sibling = new Node(s.charAt(index));
				currentNode = currentNode.sibling;
			}
			if (currentNode.child != null)
			{
				currentNode = currentNode.child;
			}
		}
		// index == index after common prefix ends
		for ( ; (index < s.length()) && (s.length() != 1); index++)
		{
			currentNode.child = new Node(s.charAt(index));
			currentNode = currentNode.child;
		}
		currentNode.child = new Node('$');
		return true;
	}

	/*
		return 0 for neither
		return 1 for prefix
		return 2 for word
		return 3 for both
	*/
	public int searchPrefix(StringBuilder s)
	{
		// No string to check or no words in dictionary
		if (s == null || s.length() == 0 || root == null)
		{
			return 0;
		}
		return recSearchPrefix(root, s, 0);
	}

	public int searchPrefix(StringBuilder s, int start, int end)
	{
		// No string to check or no words in dictionary
		if (s == null || s.length() == 0 || root == null)
		{
			return 0;
		}
		// Invalid bounds
		if (start < 0 || end > s.length())
		{
			return 0;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(s.substring(start, end+1));
		return recSearchPrefix(root, sb, 0);
	}

	private int recSearchPrefix(Node currentNode, StringBuilder sb, int index)
	{
		boolean prefix = false;
		boolean word = false;
		if (currentNode == null)
		{
			return 0;
		}
		if (index == sb.length())
		{
			if (currentNode.child != null)
			{
				prefix = true;
			}
			if (currentNode.character == '$')
			{
				if (currentNode.sibling != null)
				{
					prefix = true;
					word = true;
				}
				else
				{
					word = true;
				}
			}
		}
		if (prefix && word)
		{
			return 3;
		}
		else if (prefix)
		{
			return 1;
		}
		else if (word)
		{
			return 2;
		}
		
		boolean found = false;
		if (currentNode.character == sb.charAt(index))
		{
			found = true;
		}
		while (currentNode.sibling != null && !found)
		{
			currentNode = currentNode.sibling;
			if (currentNode.character == sb.charAt(index))
			{
				found = true;
			}
		}
		if (!found)
		{
			return 0;
		}
		return recSearchPrefix(currentNode.child, sb, index+1);
	}

	private class Node
	{
		private char character;
		private Node sibling;
		private Node child;

		private Node(char _character)
		{
			character = _character;
			sibling = null;
			child = null;
		}
	}
}