/*
Rebecca Addison
Assignment 4
CS1501
*/
import java.util.*;
public class Substitute implements SymCipher {
	private byte[] key;
	public Substitute() {
		key = new byte[256];
		List<Byte> k = new ArrayList<Byte>();
		for (int i = 0; i < 256; i++) {
			k.add((byte)(i - 128));
		}
		Collections.shuffle(k);
		Object[] objects = k.toArray();
		for (int i = 0; i < 256; i++) {
			key[i] = (byte) objects[i];
		}
	}
	public Substitute(byte[] k) {
		key = k;
	}
	public byte[] getKey() {
		return key;
	}
	public byte[] encode(String s) {
		byte[] bytes = s.getBytes();
		for (int i = 0; i < bytes.length; i++) {
			bytes[i] = (byte)(key[bytes[i]]);
		}
		return bytes;
	}
	public String decode(byte[] bytes) {
		byte[] iKey = inverseKey();
		for(int i = 0; i < bytes.length; i++) {
			int index = bytes[i] + 128;
        	bytes[i] = (byte) iKey[index];
	    }
		return new String(bytes);
	}
	private byte[] inverseKey() {
		byte[] inverse = new byte[key.length];
		for (int i = 0; i < key.length; i++) {
			inverse[key[i] + 128] = (byte) i;
		}
		return inverse;
	}
}