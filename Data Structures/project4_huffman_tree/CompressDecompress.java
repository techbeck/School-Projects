/**
 * It is okay to use ArrayList class but you are not allowed to use any other
 * predefined class supplied by Java.
 */
import java.util.ArrayList;

public class CompressDecompress
{
	/**
	 * Get a string representing a Huffman tree where its root node is root
	 * @param root the root node of a Huffman tree
	 * @return a string representing a Huffman tree
	 */
	public static String getTreeString(final BinaryNodeInterface<Character> root)
	{
		String treeString = "";
		treeString = recTreeString(root, treeString);
		return treeString;
	}

	private static String recTreeString(final BinaryNodeInterface<Character> root, String treeString)
	{
		if (root == null)
		{
			return null;
		}
		if (root.isLeaf())
		{
			treeString = treeString + "L" + root.getData();
			return treeString;
		}
		treeString = treeString + "I";
		treeString = recTreeString(root.getLeftChild(), treeString);
		treeString = recTreeString(root.getRightChild(), treeString);
		return treeString;
	}

	/**
	 * Compress the message using Huffman tree represented by treeString
	 * @param root the root node of a Huffman tree
	 * @param message the message to be compressed
	 * @return a string representing compressed message.
	 */
	public static String compress(final BinaryNodeInterface<Character> root, final String message)
	{
		String compressedMessage = "";
		char[] characters = message.toCharArray();
		for(int i = 0; i < characters.length; i++)
		{
			compressedMessage = compressedMessage + getPathTo(root, characters[i]);
		}

		return compressedMessage;
	}

	private static String getPathTo(final BinaryNodeInterface<Character> root, char c)
	{
		BinaryNodeInterface<Character> leftChild = root.getLeftChild();
		BinaryNodeInterface<Character> rightChild = root.getRightChild();;
		if (leftChild != null && leftChild.getData() != null && leftChild.getData().equals(c))
		{
			return "0";
		}
		if (rightChild != null && rightChild.getData() != null && rightChild.getData().equals(c))
		{
			return "1";
		}
		if (leftChild != null)
		{
			String leftPath = getPathTo(leftChild,c);
			if (leftPath != null)
			{
				return "0" + leftPath;
			}
		}
		if (rightChild != null)
		{
			String rightPath = getPathTo(rightChild,c);
			if (rightPath != null)
			{
				return "1" + rightPath;
			}
		}
		return null;
	}
	
	/**
	 * Decompress the message using Huffman tree represented by treeString
	 * @param treeString the string represents the Huffman tree of the
	 * compressed message
	 * @param message the compressed message to be decompressed
	 * @return a string representing decompressed message
	 */
	public static String decompress(final String treeString, final String message)
	{
		BinaryNodeInterface<Character> root = new BinaryNode<Character>();
		ArrayList<String> treeList = new ArrayList<String>();
		treeList.add(treeString);
		root = getTree(root,treeList);

		String compMessage = message;
		String decompMessage = "";
		ArrayList<String> messList = new ArrayList<String>();
		while (compMessage.length() > 0)
		{
			messList.add(compMessage);
			decompMessage = decompMessage + followPath(root, messList);
			compMessage = messList.remove(0);
		}

		return decompMessage;
	}

	private static char followPath(BinaryNodeInterface<Character> root, ArrayList<String> messList)
	{
		if (root.isLeaf())
		{
			return root.getData();
		}
		String compMessage = messList.remove(0);
		if (compMessage.charAt(0) == '0')
		{
			compMessage = compMessage.substring(1);
			messList.add(compMessage);
			return followPath(root.getLeftChild(), messList);
		}
		compMessage = compMessage.substring(1);
		messList.add(compMessage);
		return followPath(root.getRightChild(), messList);
	}

	private static BinaryNodeInterface<Character> getTree(BinaryNodeInterface<Character> root,
														ArrayList<String> treeList)
	{
		String treeString = treeList.remove(0);
		if (treeString.charAt(0) == 'L')
		{
			root.setData(treeString.charAt(1));
			treeString = treeString.substring(2);
			treeList.add(treeString);
			return root;
		}
		treeString = treeString.substring(1);
		treeList.add(treeString);
		root.setLeftChild(getTree(new BinaryNode<Character>(), treeList));
		root.setRightChild(getTree(new BinaryNode<Character>(), treeList));
		return root;
	}

}
