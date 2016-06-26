/*
Rebecca Addison
CS1501
java LZWmod - < orig.txt > cmprs.txt
java LZWmod + < cmprs.txt > expnd.txt
*/

public class LZWmod {
    private static final int R = 256;       // number of input chars
    private static int L = 512;       // initial number of codewords = 2^W
    private static int W = 9;               // initial codeword width

    public static void compress() { 
        TrieSTmod<Integer> st = new TrieSTmod<Integer>();
        for (int i = 0; i < R; i++) {
            st.put((char) i, i);
        }
        int code = R+1;  // R is codeword for EOF

        char c = BinaryStdIn.readChar();
        while (!BinaryStdIn.isEmpty()) {
            StringBuilder match = new StringBuilder();
            match.append(c);
            while (match.length() == st.longestPrefixOf(match).length() && !BinaryStdIn.isEmpty()) {
                c = BinaryStdIn.readChar();
                match.append(c);
            }
            match.deleteCharAt(match.length()-1);
            BinaryStdOut.write(st.get(match), W);      // Print s's encoding.
            System.err.println("match : " + match);
            if (code < L) {    // Add s to symbol table.
                st.put(match.append(c), code++);
            }
            if (code == L && W <= 16) {
                System.err.println("increment: " + code);
                System.err.println("W : " + W);
                W++;
                L *= 2;     // L = 2^(W+1) = 2(2^W)
            }
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 

    public static void expand() {
        String[] st = new String[65536];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];

        while (true) {
            BinaryStdOut.write(val);
            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            System.err.println("s : " + s);
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
            if (i == L && W <= 16) {
                System.err.println("increment : " + i);
                System.err.println("W : " + W);
                W++;
                L *= 2;     // L = 2^(W+1) = 2(2^W)
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}