public class Route implements Comparable<Route> { 

    private final City city1;
    private final City city2;
    private final int distance;
    private final double price;
    public static boolean compareDist = true;

   /**
     * Create an route between city1 and city2 with given weight.
     */
    public Route(City city1, City city2, int distance, double price) {
        this.city1 = city1;
        this.city2 = city2;
        this.distance = distance;
        this.price = price;
    }

   /**
     * Return the distance of this route.
     */
    public int distance() {
        return distance;
    }

    /**
     * Return the price of this route.
     */
    public double price() {
        return price;
    }

   /**
     * Return first endpoint of this route.
     */
    public City fst() {
        return city1;
    }

   /**
     * Return second endpoint of this route.
     */
    public City snd() {
        return city2;
    }

   /**
     * Return the endpoint of this route that is different from the given vertex
     * (unless a self-loop).
     */
    public City other(City vertex) {
        if      (vertex.equals(city1)) return city2;
        else if (vertex.equals(city2)) return city1;
        else throw new RuntimeException("Illegal endpoint");
    }

   /**
    * Compares routes based on price
    */
    public int comparePrice(Route r2) {
        double val = this.price - r2.price;
        if (val > 0.01) return 1;   // just in case of more precise prices
        if (val < -0.01) return -1; // but should be unnecessary
        return 0;
    }

    public int compareTo(Route r2) {
        if (compareDist == true) return compareDistance(r2);
        else return comparePrice(r2);
    }

   /**
    * Compares routes based on distance
    */
    public int compareDistance(Route r2) {
        return this.distance - r2.distance;
    }

   /**
     * Return a string representation of this route.
     */
    public String toString() {
        int id1 = city1.id();
        int id2 = city2.id();
        return String.format("%d %d %d $%.2f", id1, id2, distance, price);
    }

   /**
    * Return a string representation using names instead of IDs
    */
    public String toStringWN() {
        return String.format("%s %s %d $%.2f", city1, city2, distance, price);
    }
}