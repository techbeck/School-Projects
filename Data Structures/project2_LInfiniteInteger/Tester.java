public class Tester {
	public static void main(String[] args) {
		LInfiniteInteger infIntI = new LInfiniteInteger(-12345);
		System.out.println(infIntI);
		//LInfiniteInteger infIntS = new LInfiniteInteger("-000012345");
		//System.out.println(infIntS);
		int first = infIntI.getFirstData();
		int last = infIntI.getLastData();
		System.out.println(first);
		System.out.println(last);
	}
}