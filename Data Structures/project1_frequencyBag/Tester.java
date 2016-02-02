public class Tester
{
	public static void main(String[] args)
	{
		FrequencyBag<Integer> ints = new FrequencyBag<Integer>();
		ints.add(5);
		ints.add(6);
		ints.add(5);
		ints.add(3);
		ints.add(5);
		if (ints.getFrequencyOf(5) == 3)
		{
			System.out.println("freq: pass");
		}
		else
		{
			System.out.println("freq: fail");
		}
		if (ints.getMaxFrequency() == 3)
		{
			System.out.println("max: pass");
		}
		else
		{
			System.out.println("max: fail");
		}
		if (ints.getProbabilityOf(5) == 3/5)
		{
			System.out.println("prob: pass");
		}
	}
}