public class Tester {
	public static void main(String[] args) {
		LInfiniteInteger infIntI = new LInfiniteInteger(-12345);
		System.out.println("From int " + infIntI);
		String s = "-7654321";
		LInfiniteInteger infIntS = new LInfiniteInteger(s);
		System.out.println("From string " + infIntS);
		/*if (infIntS.getNumberOfDigits() == s.length())
		{
			System.out.println("num digits pass");
		}
		else
		{
			System.out.println("num digits fail " + infIntS.getNumberOfDigits());
		}
		if (infIntS.compareTo(infIntI) == 1)
		{
			System.out.println("compareTo() pass");
		}
		else
		{
			System.out.println("compareTo() fail " + infIntS.compareTo(infIntI));
		}*/
		System.out.println("I+S: " + infIntI.plus(infIntS));
		System.out.println("I-S: " + infIntI.minus(infIntS));
	}
}