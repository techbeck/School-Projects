/*
Rebecca Addison
CS 1501
*/

import java.lang.Iterable;
import java.util.Iterator;

public class PHPArray<V> implements Iterable<V>
{
	private int count;
	private int capacity;
	private Node<V>[] hashTable;
	private Node<V> first;
	private Node<V> last;
	private Node<V> curr;

	public PHPArray(int initSize)
	{
		count = 0;
		capacity = initSize;
		@SuppressWarnings("unchecked")
    	Node<V>[] ht = (Node<V>[]) new Node<?>[capacity];
    	hashTable = ht;
		first = null;
		last = null;
	}

	// put new (key, value) pair into array
	public void put(String k, V v)
	{
		if (k == null) return;
		if (v == null) unset(k);
		if (count >= capacity/2) resize(2*capacity);
		count++;
		int i;
		for (i = hash(k); hashTable[i] != null; i++)
		{
			if (hashTable[i].data.key.equals(k))
			{
				hashTable[i].data.value = v;
				return;
			}
		}
		hashTable[i] = new Node<V>(new Pair<V>(k,v));
		if (first == null)
		{
			first = hashTable[i];
			curr = first;
		}
		if (last == null)
		{
			last = hashTable[i];
		}
		hashTable[i].previous = last;
		last.next = hashTable[i];
		last = hashTable[i];
	}

	// convert int to string, then call regular put()
	public void put(Integer i, V v)
	{
		String k = i.toString();
		if (i == null) return;
		if (v == null) unset(k);
		put(k,v);
	}

	// resize hashtable
	private void resize(int newCap)
	{
		System.out.print("Size: " + count);
		System.out.println(" -- resizing array from " + capacity + " to " + newCap);
		PHPArray<V> temp = new PHPArray<V>(newCap);
		for (int i = 0; i < capacity; i++)
		{
			if (hashTable[i] != null)
			{
				temp.put(hashTable[i].data.key, hashTable[i].data.value);
			}
		}
		this.hashTable = temp.hashTable;
		this.first = temp.first;
		this.last = temp.last;
		capacity = newCap;
	}

	// return value associated with given key. if not found, return null.
	public V get(String k)
	{
		for (int i = hash(k); hashTable[i] != null; i = (i + 1) % capacity) 
		{
			if (hashTable[i].data.key.equals(k))
			{
				return hashTable[i].data.value;
			}
		}
		return null;
	}

	public V get(Integer i)
	{
		String k = i.toString();
		return get(k);
	}

	// if array contains (key, value) pair associated with given key,
	// return true. else, false.
	public boolean contains(String k)
	{
		return get(k) != null;
	}

	// delete (key, value) pair associated with given key from array
	public void unset(String k)
	{
		if (!contains(k))
		{
			return;
		}

		// find position i of key
		int i = hash(k);
		while (!k.equals(hashTable[i].data.key))
		{
			i = (i + 1) % capacity;
		}

		// delete key and associated value
		if (last == hashTable[i])
		{
			last = hashTable[i].previous;
		}
		if (first == hashTable[i])
		{
			first = hashTable[i].next;
		}
		hashTable[i].previous.next = hashTable[i].next;
		hashTable[i].next.previous = hashTable[i].previous;
		hashTable[i] = null;

		// rehash all keys in same cluster
		i = (i + 1) % capacity;
		while (hashTable[i] != null) {
			String keyToRehash = hashTable[i].data.key;
			System.out.println("Key " + keyToRehash + " rehashed...\n");
            V valToRehash = hashTable[i].data.value;
			hashTable[i] = null;
			count--;  
			put(keyToRehash, valToRehash);
			i = (i + 1) % capacity;
		}

		count--;
	}

	// convert integer to string then call regular unset
	public void unset(Integer i)
	{
		String k = i.toString();
		unset(k);
	}

	public Pair<V> each()
	{
		if (curr == null)
		{
			return null;
		}
		Pair<V> currPair = curr.data;
		curr = curr.next;
		return currPair;
	}

	public void reset()
	{
		curr = first;
	}

	public int length()
	{
		return capacity;
	}

	public void sort()
	{

	}

	public void asort()
	{

	}

	// hash code of string, made positive, and modded to fit in table
	private int hash(String key)
	{
		return (key.hashCode() & 0x7fffffff) % capacity;
	}

	public Iterator<V> iterator()
	{
		return new ArrayIterator();
	}

	public void showTable()
	{
		System.out.println("Raw Hash Table Contents:");
		for (int i = 0; i < capacity; i++)
		{
			System.out.print(i + ": ");
			if (hashTable[i] == null)
			{
				System.out.println("null");
			}
			else
			{
				hashTable[i].printData();
			}
		}
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
	private static class Node<V>
	{
		private Pair<V> data;
		private Node<V> next;
		private Node<V> previous;

		private Node(Pair<V> d)
		{
			data = d;
		}

		// used for next() return
		public String toString()
		{
			return data.value.toString();
		}

		// used for showTable()
		private void printData()
		{
			System.out.println("Key: " + data.key + " Value: " + data.value);
		}
	}

	// iterates through linked list
	private class ArrayIterator implements Iterator<V>
	{
		private Node<V> current;

		public ArrayIterator()
		{
			current = first;
		}

		public boolean hasNext()
		{
			if (current.next != null)
			{
				return true;
			}
			return false;
		}

		public V next()
		{
			current = current.next;
			return current.data.value;
		}
	}
}