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
		StringBuilder sb = new StringBuilder();
		sb.append(s.getChar(0));
		int status = searchPrefix(sb);
		int startIndex;
		// Find common prefix
		for (startIndex = 1; startIndex < s.length() && status != 0; startIndex++)
		{
			sb.append(s.getChar(startIndex));
			status = searchPrefix(sb);
			currentNode = currentNode.child;
		}
		if (startIndex == s.length())
		{
			return true;
		}
		for (int i = startIndex; i < s.length(); i++)
		{
			currentNode.setChild(new Node(s.charAt(startIndex)));
			currentNode = currentNode.child;
		}
	}

	/*
		return 0 for neither
		return 1 for prefix
		return 2 for word
		return 3 for both
	*/
	public int searchPrefix(StringBuilder s)
	{
		if (s == null || s.length() == 0 || root == null)
		{
			return 0;
		}
		Node currentNode = root;
		String str = s.toString();
		return recSearchPrefix(currentNode, str);
	}

	public int searchPrefix(StringBuilder s, int start, int end)
	{
		if (root == null)
		{
			return 0;
		}
		Node currentNode = root;
		String str = s.substring(start, end+1);
		return recSearchPrefix(currentNode, str);
	}

	private recSearchPrefix(Node currentNode, String str)
	{
		if (currentNode.character == '$')
		{
			if (currentNode.sibling != null)
			{
				return 3;
			}
			else
			{
				return 2;
			}
		}
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

		private setSib(Node _sibling)
		{
			sibling = _sibling;
		}

		private setChild(Node _child)
		{
			child = _child;
		}
	}
}