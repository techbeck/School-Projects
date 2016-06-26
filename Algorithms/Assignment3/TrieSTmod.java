/*************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *
 *  A string symbol table for ASCII strings, implemented using a 256-way trie.
 *
 *************************************************************************/

public class TrieSTmod<Value> {
    private static final int R = 256;        // extended ASCII

    private Node root;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(StringBuilder key) {
        return get(key) != null;
    }

    public Value get(StringBuilder key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return (Value) x.val;
    }

    private Node get(Node x, StringBuilder key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

   /****************************************************
    * Insert key-value pair into the symbol table.
    ****************************************************/
    public void put(StringBuilder key, Value val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, StringBuilder key, Value val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    public void put(char c, Value val) {
        StringBuilder temp = new StringBuilder();
        temp.append(c);
        put(temp, val);
    }

    // find the key that is the longest prefix of s
    public StringBuilder longestPrefixOf(StringBuilder query) {
        int length = longestPrefixOf(root, query, 0, 0);
        StringBuilder temp = new StringBuilder(query.substring(0, length));
        return temp;
    }

    // find the key in the subtrie rooted at x that is the longest
    // prefix of the query string, starting at the dth character
    private int longestPrefixOf(Node x, StringBuilder query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }


    public Iterable<StringBuilder> keys() {
        return keysWithPrefix(new StringBuilder());
    }

    public Iterable<StringBuilder> keysWithPrefix(StringBuilder prefix) {
        Queue<StringBuilder> queue = new Queue<StringBuilder>();
        Node x = get(root, prefix, 0);
        collect(x, prefix, queue);
        return queue;
    }

    private void collect(Node x, StringBuilder key, Queue<StringBuilder> queue) {
        if (x == null) return;
        if (x.val != null) queue.enqueue(key);
        for (int c = 0; c < R; c++) {
            key.append((char) c);
            collect(x.next[c], key, queue);
        }
    }


    public Iterable<StringBuilder> keysThatMatch(StringBuilder pat) {
        Queue<StringBuilder> q = new Queue<StringBuilder>();
        collect(root, new StringBuilder(), pat, q);
        return q;
    }
 
    public void collect(Node x, StringBuilder prefix, StringBuilder pat, Queue<StringBuilder> q) {
        if (x == null) return;
        if (prefix.length() == pat.length() && x.val != null) q.enqueue(prefix);
        if (prefix.length() == pat.length()) return;
        char next = pat.charAt(prefix.length());
        for (int c = 0; c < R; c++)
            if (next == '.' || next == c) {
                prefix.append((char) c);
                collect(x.next[c], prefix, pat, q);
            }
    }

    public void delete(StringBuilder key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, StringBuilder key, int d) {
        if (x == null) return null;
        if (d == key.length()) x.val = null;
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1);
        }
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }


    /* test client
    public static void main(StringBuilder[] args) {

        // build symbol table from standard input
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; !StdIn.isEmpty(); i++) {
            StringBuilder key = StdIn.readString();
            st.put(key, i);
        }

        // print results
        for (StringBuilder key : st.keys()) {
            StdOut.println(key + " " + st.get(key));
        }
    }*/
}