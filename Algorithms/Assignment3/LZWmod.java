/*
Rebecca Addison
CS1501
Altered from Sedgewick text code
java LZWmod - < orig.txt > cmprs.txt
java LZWmod + < cmprs.txt > expnd.txt
*/

public class LZWmod {
    private static final int R = 256;       // number of input chars
    private static int L = 512;             // initial number of codewords = 2^W
    private static int W = 9;               // initial codeword width
    private static int bitsRead = 0;
    private static int bitsWritten = 0;     // bitsWritten/bitsRead = comp ratio
    private static boolean delete = false;
    private static boolean delAtThres = false;
    private static double delThres = 0.4;

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
                bitsRead += 8;
                match.append(c);
            }
            match.deleteCharAt(match.length()-1);
            BinaryStdOut.write(st.get(match), W);      // Print s's encoding.
            bitsWritten += W;
            if (code < L) {    // Add s to symbol table.
                st.put(match.append(c), code++);
            }
            if (code == L && W < 16) {
                W++;
                L *= 2;     // L = 2^(W+1) = 2(2^W)
            }
            if (code == 65536) {     // out of codewords
                if (delete) {
                    st = new TrieSTmod<Integer>();
                    W = 9;
                    L = 512;
                    for (int i = 0; i < R; i++) {
                        st.put((char) i, i);
                    }
                    code = R+1;
                }
                if (delAtThres && (bitsWritten/(float)bitsRead > delThres)) {
                    System.err.println("deleting dictionary");
                    st = new TrieSTmod<Integer>();
                    W = 9;
                    L = 512;
                    for (int i = 0; i < R; i++) {
                        st.put((char) i, i);
                    }
                    code = R+1;
                }
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
            bitsWritten += val.length() * 8;
            codeword = BinaryStdIn.readInt(W);
            bitsRead += W;
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            System.err.println("i " + i);
            System.err.println("L " + L);
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
            if (i == L-1 && W < 16) {
                System.err.println("increment");
                W++;
                L *= 2;     // L = 2^(W+1) = 2(2^W)
            }
            if (i == 65535) {     // out of codewords
                if (delete) {
                    st = new String[65536];
                    W = 9;
                    L = 512;
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";
                }
                if (delAtThres && (bitsWritten/(float)bitsRead > delThres)) {
                    System.err.println("deleting dictionary");
                    st = new String[65536];
                    W = 9;
                    L = 512;
                    for (i = 0; i < R; i++)
                        st[i] = "" + (char) i;
                    st[i++] = "";
                }
            }
        }
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if (args.length == 2) {
            if      (args[1].equals("d")) delete = true;
            else if (args[1].equals("dt")) delAtThres = true;
        }
        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new RuntimeException("Illegal command line argument");
    }
}