public class City {
	private int id;
	private String name;
	public City(int i, String n) {
		id = i;
		name = n;
	}
	public int id() {
		return id;
	}
	public String toString() {
		return name;
	}
}