/*
Rebecca Addison
CS 1501
*/

import java.lang.Iterable;
import java.util.Iterator;
import java.lang.Comparable;
import java.lang.IllegalArgumentException;

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
		curr = null;
	}

	// put new (key, value) pair into array
	public void put(String k, V v)
	{
		if (k == null) return;
		if (v == null) unset(k);
		if (count >= capacity/2) resize(2*capacity);
		count++;
		int i;
		for (i = hash(k, capacity); hashTable[i] != null; i++)
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
		System.out.print("\t\tSize: " + count);
		System.out.println(" -- resizing array from " + capacity + " to " + newCap);
		@SuppressWarnings("unchecked")
		Node<V>[] temp = (Node<V>[]) new Node<?>[newCap];
		for (int i = 0; i < capacity; i++)
		{
			if (hashTable[i] != null)
			{
				temp[hash(hashTable[i].data.key, newCap)] = hashTable[i];
			}
		}
		hashTable = temp;
		capacity = newCap;
	}

	// return value associated with given key. if not found, return null.
	public V get(String k)
	{
		for (int i = hash(k, capacity); hashTable[i] != null; i = (i + 1) % capacity) 
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
		int i = hash(k, capacity);
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
			System.out.println("\t\tKey " + keyToRehash + " rehashed...\n");
            V valToRehash = hashTable[i].data.value;
			hashTable[i] = null;
			int newHash = hash(keyToRehash, capacity);
			while (hashTable[newHash] != null)
			{
				newHash++;
			}
			hashTable[newHash] = new Node<V>(new Pair<V>(keyToRehash, valToRehash));
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
		return count;
	}

	public void sort()
	{
		if (!(first.data.value instanceof Comparable))
		{
			throw new IllegalArgumentException();
		}
		if (first == null || first.next == null)
		{
			return;
		}
		// need array without the null elements of hashTable
		Node<V>[] a = linkedListToArray();
		@SuppressWarnings("unchecked")
		Node<V>[] temp = (Node<V>[]) new Node<?>[count];
		mergeSort(a, temp, 0, count-1);
		// copy into hashTable, create new linked list, & alter keys
		first = a[0];
		hashTable[0] = a[0];
		hashTable[0].data.key = "0";
		int i;
		for (i = 1; i < a.length; i++)
		{
			String key = Integer.toString(i);
			hashTable[hash(key, capacity)] = a[i];
			hashTable[hash(key, capacity)].data.key = key;
			a[i-1].next = a[i];
			a[i].previous = a[i-1];
		}
		a[i-1].next = null;
	}

	public void asort()
	{
		if (!(first.data.value instanceof Comparable))
		{
			throw new IllegalArgumentException();
		}
		if (first == null || first.next == null)
		{
			return;
		}
		// need array without the null elements of hashTable
		Node<V>[] a = linkedListToArray();
		@SuppressWarnings("unchecked")
		Node<V>[] temp = (Node<V>[]) new Node<?>[count];
		mergeSort(a, temp, 0, count-1);
		// create new linked list
		first = a[0];
		int i;
		for (i = 1; i < count; i++)
		{
			a[i-1].next = a[i];
			a[i].previous = a[i-1];
		}
		a[i-1].next = null;
	}

	private void mergeSort(Node<V>[] a, Node<V>[] temp, int start, int end)
	{
		if( start < end )
		{
			int center = (start + end) / 2;
			mergeSort(a, temp, start, center);
			mergeSort(a, temp, center + 1, end);
			merge(a, temp, start, center + 1, end);
		}
	}

	private void merge(Node<V>[] a, Node<V>[] temp, int start, int middle, int end )
	{
		int startEnd = middle - 1;
		int k = start;
		int num = end - start + 1;

		while(start <= startEnd && middle <= end)
		{
			if(a[start].compareTo(a[middle]) <= 0)
			{
				temp[k++] = a[start++];
			}
			else
			{
				temp[k++] = a[middle++];
			}
		}
		while(start <= startEnd)    // Copy rest of first half
		{
			temp[k] = a[start];
			k++;
			start++;
		}
		while(middle <= end)  // Copy rest of second half
		{
			temp[k] = a[middle];
			k++;
			middle++;
		}
		// Copy temp back
		for(int i = 0; i < num; i++, end--)
		{
			a[end] = temp[end];
		}
	}

	public Node<V>[] linkedListToArray()
	{
		@SuppressWarnings("unchecked")
    	Node<V>[] temp = (Node<V>[]) new Node<?>[count];
    	Node<V> curr = first;
    	int index = 0;
		while(index < count)
		{
			temp[index] = curr;
			curr = curr.next;
			index++;
		}
		return temp;
	}

	// hash code of string, made positive, and modded to fit in table
	private int hash(String key, int cap)
	{
		return (key.hashCode() & 0x7fffffff) % cap;
	}

	public Iterator<V> iterator()
	{
		return new ArrayIterator();
	}

	public void showTable()
	{
		System.out.println("\tRaw Hash Table Contents:");
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
	private static class Node<V> implements Comparable<Node<V>>
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

		public int compareTo(Node<V> n)
		{
			V thisData = this.data.value;
			V nData = n.data.value;
			@SuppressWarnings("unchecked")
			int compare = ((Comparable<V>) thisData).compareTo(nData);
			return compare;
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
			if (current != null)
			{
				return true;
			}
			return false;
		}

		public V next()
		{
			V val = current.data.value;
			current = current.next;
			return val;
		}
	}
}