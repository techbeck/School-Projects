/*
Rebecca Addison
Assignment 4
CS1501
*/

import java.util.Random;
public class Add128 implements SymCipher {
	private byte[] key;
	public Add128() {
		key = new byte[128];
		Random randNum = new Random();
		randNum.nextBytes(key);
	}
	public Add128(byte[] k) {
		key = k;
	}
	public byte[] getKey() {
		return key;
	}
	public byte[] encode(String s) {
		byte[] bytes = s.getBytes();
		int kIndex;
		int bIndex;
		for (kIndex = 0, bIndex = 0; bIndex < bytes.length; kIndex++, bIndex++) {
			if (kIndex == key.length) {
				kIndex = 0;
			}
			bytes[bIndex] += key[kIndex];
		}
		return bytes;
	}
	public String decode(byte[] bytes) {
		int kIndex;
		int bIndex;
		for (kIndex = 0, bIndex = 0; bIndex < bytes.length; kIndex++, bIndex++) {
			if (kIndex == key.length) {
				kIndex = 0;
			}
			bytes[bIndex] -= key[kIndex];
		}
		return new String(bytes);
	}
}