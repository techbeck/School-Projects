/*
Rebecca Addison
CS 1501
*/

public class PHPArray<V> implements Iterable<V>
{
	private int count;
	private int capacity;
	private Node[] hashTable;
	private Node first;
	private Node last;
	private Node current;

	public PHPArray<V>(int initSize)
	{
		count = 0;
		capacity = initSize;
		hashTable = new Node[capacity];
		first = null;
		last = null;
	}

	// put new (key, value) pair into array
	public void put(String k, V v)
	{
		if (k == null) return;
		if (v == null) delete(k);
		if (count >= capacity/2) resize(2*capacity);
		count++;
		for (int i = hash(k); hashTable[i] != null; i++)
		{
			if (hashTable[i].data.key.equals(k))
			{
				hashTable[i].data.value = v;
				return;
			}
		}
		hashTable[i] = new Node(new Pair<V>(k,v));
		hashTable[i].previous = last;
		last.next = hashTable[i];
		last = hashTable[i];
	}

	// convert int to string, then call regular put()
	public void put(Integer i, V v)
	{
		String k = i.toString();
		if (i == null) return;
		if (v == null) delete(k);
		put(k,v);
	}

	// resize hashtable
	private void resize(int newCap)
	{
		PHPArray<V> temp = new PHPArray<V>(newCap);
		for (int i = 0; i < capacity; i++)
		{
			temp.put(list[i]);
		}
		this.list = temp.list;
		this.first = temp.first;
		this.last = temp.last;
		capacity = newCap;
	}

	// return value associated with given key. if not found, return null.
	public V get(String k)
	{
		for (int i = hash(k); hashTable[i] != null; i = (i + 1) % M) 
		{
			if (hashTable[i].data.key.equals(k))
			{
				return hashTable[i].data.value;
			}
		}
		return null;
	}

	// if array contains (key, value) pair associated with given key,
	// return true. else, false.
	public boolean contains(String k)
	{
		return get(k) != null;
	}

	// delete (key, value) pair associated with given key from array
	public void delete(String k)
	{
		if (!contains(k))
		{
			return;
		}

		// find position i of key
		int i = hash(key);
		while (!key.equals(hashTable[i].data.key))
		{
			i = (i + 1) % M;
		}

		// delete key and associated value
		hashTable[i].data.key = null;
		hashTable[i].data.value = null;

		// rehash all keys in same cluster
		i = (i + 1) % M;
		while (hashTable[i] != null) {
			// delete keys[i] an vals[i] and reinsert
			String keyToRehash = hashTable[i].key;
            V valToRehash = hashTable[i].value;
			hashTable[i].key = null;
			hashTable[i].value = null;
			count--;  
			put(keyToRehash, valToRehash);
			i = (i + 1) % M;
		}

		count--;
	}

	// hash code of string, made positive, and modded to fit in table
	private int hash(String key)
	{
		return (key.hashCode() & 0x7fffffff) % capacity;
	}

	public Iterator<V> iterator()
	{
		return new ArrayIterator<V>(first);
	}

	public static class Pair<V>
	{
		public String key;
		public V value;
		
		private Pair(String k, V v)
		{
			key = k;
			value = v;
		}
	}

	// node holding pair info and linked list info
	private class Node
	{
		private Pair<V> data;
		private next;
		private previous;

		private Node(Pair<V> d)
		{
			data = d;
		}

		// used for next() return
		public String toString()
		{
			return data.value.toString();
		} 
	}

	// iterates through linked list
	private class ArrayIterator<V> implements Iterator<V>
	{
		private Node current;

		public ArrayIterator<V>(Node p)
		{
			current = p;
		}

		public boolean hasNext()
		{
			if (current.next != null)
			{
				return true;
			}
			return false;
		}

		public Node next()
		{
			current = current.next;
			return current;
		}
	}
}