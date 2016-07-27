/*
Rebecca Addison
CS1501
*/

import java.io.*;
import java.util.*;

public class Airline {
	// Graph Variables
	private int numCities;
	private int numRoutes;
	private City[] cities;
	private ArrayList<Route> routes = new ArrayList<Route>();
	private Bag<Route>[] adj;	// adjacency list indexed on city id's (adj[id-1])

	// MST Variables
	private Route[] edgeTo;        // edgeTo[v] = shortest edge from tree v non-tree v
	private int[] distTo;      // distTo[v] = weight of shortest such edge
	private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
	private IndexMinPQ<Integer> pq;	// based on city ID

	// Dijkstra Variables
	private double[] costTo;	// costTo[v] = distance  of shortest s->v path
	private IndexMinPQ<Double> costPQ;	// based on city ID, option 4

	public Airline(String filename) {
		System.out.println("INPUT FILE: " + filename);
		System.out.println("-----------------------");
		loadRoutes(filename);
	}

	public void loadRoutes(String filename) {
		Scanner scan = null;
		try {
			scan = new Scanner(new File(filename));
		} catch (IOException io) {
			System.out.println("Problem opening file");
			System.exit(1);
		}
		numCities = scan.nextInt();
		cities = new City[numCities];
		scan.nextLine();	// throw out leftover newline
		for (int i = 0; i < numCities; i++) {
			String name = scan.nextLine();
			cities[i] = new City(i+1, name);
		}
		@SuppressWarnings("unchecked")
		Bag<Route>[] a = (Bag<Route>[]) new Bag[numCities];
		adj = a;
		for (int i = 0; i < numCities; i++) {
			adj[i] = new Bag<Route>();
		}
		while(scan.hasNext()) {
			String input = scan.nextLine();
			String[] inArray = input.split(" ");
			int c1ID = Integer.parseInt(inArray[0]);
			int c2ID = Integer.parseInt(inArray[1]);
			int distance = Integer.parseInt(inArray[2]);
			double price = Double.parseDouble(inArray[3]);
			Route r = new Route(cities[c1ID-1], cities[c2ID-1], distance, price);
			routes.add(r);
			adj[c1ID-1].add(r);
			adj[c2ID-1].add(r);
			numRoutes++;
		}
		scan.close();
	}

	// Option 1
	public void showAllRoutes() {
		System.out.println("SHOWING ALL DIRECT ROUTES");
		System.out.println("Note that routes are duplicated," + 
							" once from each end city's point of view");
		System.out.println("--------------------------------" +
							"-----------------------------------------");
		Object[] objArr = routes.toArray();
		for (int i = 0; i < numRoutes; i++) {
			Route route = (Route) objArr[i];
			System.out.println(route.toStringWN());
		}
	}

	// Option 2
	public void mst() {
		edgeTo = new Route[numCities];
		distTo = new int[numCities];
		marked = new boolean[numCities];
		pq = new IndexMinPQ<Integer>(numCities);
		System.out.println("MINIMUM SPANNING TREE");
		System.out.println("---------------------");
		for (int i = 0; i < numCities; i++) {
			distTo[i] = Integer.MAX_VALUE;
		}
		for (int i = 0; i < numCities; i++) {   // run from each vertex to find
			if (!marked[i]) { 					// minimum spanning forest
				System.out.println("Connected Component:");
				prim(i);
			}
		}
	}

	private void prim(int s) {
		distTo[s] = 0;
		pq.insert(s, distTo[s]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			if (v != s) {
				Route r = edgeTo[v];
				System.out.printf("%s, %s : %d\n", r.fst(), r.snd(), r.distance());
			}
			scan(v);
		}
	}

	private void scan(int v) {
		marked[v] = true;
		for (Route r : adj[v]) {
			int w = r.other(cities[v]).id()-1;	// index of other city in route
			if (marked[w]) 
			{
				continue;         // v-w is obsolete edge
			}
			if (r.distance() < distTo[w]) {
				distTo[w] = r.distance();
				edgeTo[w] = r;
				if (pq.contains(w)) 
				{
						pq.change(w, distTo[w]);
				}
				else              
				{
						pq.insert(w, distTo[w]);
				}
			}
		}
	}

	// Option 3
	public void shortestByDistance(String c1, String c2) {
		System.out.println("SHORTEST DISTANCE PATH from " + c1 + " to " + c2);
		System.out.println("--------------------------------------------------------");
		City city1 = null;
		City city2 = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c1)) {
				city1 = cities[i];
			}
			if (cities[i].name().equals(c2)) {
				city2 = cities[i];
			}
		}
		if (c1.equals(c2) || city1 == null || city2 == null) {
			System.out.println("Invalid city choice(s)");
			return;
		}
		distTo = new int[numCities];
		edgeTo = new Route[numCities];
		for (int i = 0; i < numCities; i++)
			distTo[i] = Integer.MAX_VALUE;
		distTo[city1.id()-1] = 0;
		// relax vertices in order of distance from s
		pq = new IndexMinPQ<Integer>(numCities);
		pq.insert(city1.id()-1, distTo[city1.id()-1]);
		while (!pq.isEmpty()) {
			int v = pq.delMin();
			for (Route r : adj[v])
				relaxD(r, v);
		}
		if (distTo[city2.id()-1] == Integer.MAX_VALUE) {
			System.out.println("No path");
			return;
		}
		System.out.printf("Shortest distance from %s to %s is %d\n",
										c1, c2, distTo[city2.id()-1]);
		System.out.println("Path with edges (in reverse order):");
		City currCity = city2;
		for (Route r = edgeTo[city2.id()-1]; r != null; r = edgeTo[currCity.id()-1]) {
			System.out.print(currCity + " " + r.distance() + " ");
			currCity = r.other(currCity);
		}
		System.out.println(currCity);
	}

	private void relaxD(Route r, int v) {
		// relax edge e and update pq if changed
		City city2 = r.other(cities[v]);
		int w = city2.id()-1;
		if (distTo[w] > distTo[v] + r.distance()) {
			distTo[w] = distTo[v] + r.distance();
			edgeTo[w] = r;
			if (pq.contains(w)) pq.change(w, distTo[w]);
			else               		pq.insert(w, distTo[w]);
		}
	}

	// Option 4
	public void shortestByCost(String c1, String c2) {
		System.out.println("SHORTEST COST PATH from " + c1 + " to " + c2);
		System.out.println("--------------------------------------------------------");
		City city1 = null;
		City city2 = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c1)) {
				city1 = cities[i];
			}
			if (cities[i].name().equals(c2)) {
				city2 = cities[i];
			}
		}
		if (c1.equals(c2) || city1 == null || city2 == null) {
			System.out.println("Invalid city choice(s)");
			return;
		}
		costTo = new double[numCities];
		edgeTo = new Route[numCities];
		for (int i = 0; i < numCities; i++)
			costTo[i] = Double.POSITIVE_INFINITY;
		costTo[city1.id()-1] = 0;
		// relax vertices in order of distance from s
		costPQ = new IndexMinPQ<Double>(numCities);
		costPQ.insert(city1.id()-1, costTo[city1.id()-1]);
		while (!costPQ.isEmpty()) {
			int v = costPQ.delMin();
			for (Route r : adj[v])
				relaxC(r, v);
		}
		if (costTo[city2.id()-1] == Double.POSITIVE_INFINITY) {
			System.out.println("No path");
			return;
		}
		System.out.printf("Shortest cost from %s to %s is %.2f\n",
										c1, c2, costTo[city2.id()-1]);
		System.out.println("Path with edges (in reverse order):");
		City currCity = city2;
		for (Route r = edgeTo[city2.id()-1]; r != null; r = edgeTo[currCity.id()-1]) {
			System.out.print(currCity + " " + r.price() + " ");
			currCity = r.other(currCity);
		}
		System.out.println(currCity);
	}

	// relax edge e and update pq if changed
	private void relaxC(Route r, int v) {
		City city2 = r.other(cities[v]);
		int w = city2.id()-1;
		if (costTo[w] > costTo[v] + r.price()) {
			costTo[w] = costTo[v] + r.price();
			edgeTo[w] = r;
			if (costPQ.contains(w)) costPQ.change(w, costTo[w]);
			else               		costPQ.insert(w, costTo[w]);
		}
	}

	// Option 5
	public void shortestByHops(String c1, String c2) {
		System.out.println("FEWEST HOPS from " + c1 + " to " + c2);
		System.out.println("---------------------------------------------");
		City city1 = null;
		City city2 = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c1)) {
				city1 = cities[i];
			}
			if (cities[i].name().equals(c2)) {
				city2 = cities[i];
			}
		}
		if (c1.equals(c2) || city1 == null || city2 == null) {
			System.out.println("Invalid city choice(s)");
			return;
		}
		marked = new boolean[numCities];
		distTo = new int[numCities];
		edgeTo = new Route[numCities];
		for (int i = 0; i < numCities; i++) distTo[i] = Integer.MAX_VALUE;
		bfs(city1.id()-1);
		if (distTo[city2.id()-1] == Integer.MAX_VALUE) {
			System.out.println("No path");
			return;
		}
		System.out.printf("Fewest hops from %s to %s is %d\n",
										c1, c2, distTo[city2.id()-1]);
		City currCity = city2;
		for (Route r = edgeTo[city2.id()-1]; r != null; r = edgeTo[currCity.id()-1]) {
			System.out.print(currCity + " ");
			currCity = r.other(currCity);
		}
		System.out.println(currCity);
	}

	public void bfs(int s) {
		Queue<Integer> q = new Queue<Integer>();
		distTo[s] = 0;
		marked[s] = true;
		q.enqueue(s);
		while (!q.isEmpty()) {
			int v = q.dequeue();
			for (Route r : adj[v]) {
				int w = r.other(cities[v]).id()-1; // index of other city on route
				if (!marked[w]) {
					edgeTo[w] = r;
					distTo[w] = distTo[v] + 1;
					marked[w] = true;
					q.enqueue(w);
				}
			}
		}
	}

	// Option 6
	public void pathsUnderCost(double maxCost) {
		System.out.printf("ALL PATHS OF COST %.0f OR LESS\n", maxCost);
		System.out.println("Note that routes are duplicated," + 
							" once from each end city's point of view");
		System.out.println("--------------------------------" +
							"-----------------------------------------");
		// find all paths starting at each city
		for (int i = 0; i < numCities; i++) {
			edgeTo = new Route[numCities];	// reset for each start city
			marked = new boolean[numCities];
			recPaths(maxCost, 0, cities[i]);
		}
	}

	private void recPaths(double maxCost, double currCost, City currCity) {
		// backtrack if above max cost
		if (currCost > maxCost) {
			marked[currCity.id()-1] = false;
			return;
		}
		if (marked[currCity.id()-1]) {
			return;
		}
		// Print current path before continuing along routes
		if (edgeTo[currCity.id()-1] != null) {
			System.out.printf("Cost: %.0f Path (reversed): ", currCost);
			City temp = currCity;
			for (Route r = edgeTo[temp.id()-1]; r != null; r = edgeTo[temp.id()-1]) {
				System.out.printf("%s %.0f ", temp, r.price());
				temp = r.other(temp);
			}
			System.out.println(temp);
		}
		// Recursion
		marked[currCity.id()-1] = true;
		for (Route r : adj[currCity.id()-1]) {
			// TO DO:
			City other = r.other(currCity);
			if (!marked[other.id()-1]) {
				edgeTo[other.id()-1] = r;
				currCost += r.price();
				recPaths(maxCost, currCost, other);
			}
		}
		// traversed all paths from currCity, backtrack to previous city
		marked[currCity.id()-1] = false;
	}

	// Option 7
	public void addRoute(String c1, String c2, int distance, double price) {
		if (c1.equals(c2) || distance < 0 || price < 0) {
			System.out.println("Invalid route info");
			return;
		}
		City city1 = null;
		City city2 = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c1)) {
				city1 = cities[i];
			}
			if (cities[i].name().equals(c2)) {
				city2 = cities[i];
			}
		}
		if (city1 == null || city2 == null) {
			System.out.println("Invalid city choice(s)");
			return;
		}
		Route r = new Route(city1, city2, distance, price);
		routes.add(r);
		adj[city1.id()-1].add(r);
		adj[city2.id()-1].add(r);
		numRoutes++;
	}

	// Option 8
	public void removeRoute(String c1, String c2) {
		City city1 = null;
		City city2 = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c1)) {
				city1 = cities[i];
			}
			if (cities[i].name().equals(c2)) {
				city2 = cities[i];
			}
		}
		if (c1.equals(c2) || city1 == null || city2 == null) {
			System.out.println("Invalid city choice(s)");
			return;
		}
		removeRoute(city1, city2);
	}

	public void removeRoute(City city1, City city2) {
		Route route = null;
		for (Route r : adj[city1.id()-1]) {
			if (r.other(city1).equals(city2)) {
				route = r;
				break;
			}
		}
		adj[city1.id()-1].remove(route);
		adj[city2.id()-1].remove(route);
		routes.remove(route);
		numRoutes--;
	}

	// Option 9
	public void addCity(String c) {
		if (c == null || c.length() == 0) {
			System.out.println("Invalid city choice");
			return;
		}
		City newCity = new City(numCities+1, c);
		// Add to cities and adj
		if (numCities >= cities.length) {
			resizeCities(2*cities.length);
			resizeAdj(2*cities.length);
		}
		cities[numCities] = newCity;
		adj[numCities] = new Bag<Route>();
		numCities++;
	}

	// Option 10
	public void removeCity(String c) {
		City city = null;
		for (int i = 0; i < numCities; i++) {
			if (cities[i].name().equals(c)) {
				city = cities[i];
			}
		}
		if (city == null) {
			System.out.println("Invalid city choice");
			return;
		}
		// Remove all routes connected to the city
		for (Route r : adj[city.id()-1]) {
			City other = r.other(city);
			adj[other.id()-1].remove(r);
			routes.remove(r);
			numRoutes--;
		}
		cities[city.id()-1] = null;
		adj[city.id()-1] = null;
		numCities--;
		// Shift and resize arrays as necessary
		shiftCities(city.id()-1);
		shiftAdj(city.id()-1);
		if (numCities < cities.length/2) {	// halve the lengths of the arrays
			resizeCities(cities.length/2);
			resizeAdj(cities.length/2);
		}
	}

	private void shiftCities(int index) {
		for (int i = index; i < numCities; i++) {
			cities[i] = cities[i+1];
		}
		cities[numCities] = null;
	}

	private void shiftAdj(int index) {
		for (int i = index; i < numCities; i++) {
			adj[i] = adj[i+1];
		}
		adj[numCities] = null;
	}

	private void resizeCities(int newSize) {
		City[] temp = new City[newSize];
		for (int i = 0; i < numCities; i++) {
			temp[i] = cities[i];
		}
		cities = temp;
	}

	private void resizeAdj(int newSize) {
		@SuppressWarnings("unchecked")
		Bag<Route>[] temp = (Bag<Route>[]) new Bag[newSize];
		for (int i = 0; i < numCities; i++) {
			temp[i] = adj[i];
		}
		adj = temp;
	}

	// Option 11
	public void saveRoutes(String filename) {
		PrintWriter p = null;
		try {
			p = new PrintWriter(filename);
		} catch (IOException e) {
			System.out.println("Problem saving routes");
			System.exit(1);
		}
		p.println(numCities);
		for (int i = 0; i < numCities; i++) {
			p.println(cities[i]);
		}
		Object[] objArr = routes.toArray();
		for (int i = 0; i < numRoutes; i++) {
			Route route = (Route) objArr[i];
			p.println(route);
		}
		p.close();
	}

	// Driver
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		System.out.println("What file do you want to use for input/output?");
		String filename = input.nextLine();
		Airline a = new Airline(filename);
		String city1, city2;
		loop:
		while (true) {
			System.out.println("\n\tWhat would you like to do?");
			System.out.println("\t1: See All Direct Routes");
			System.out.println("\t2: Find Minimum Spanning Tree");
			System.out.println("\t3: Find Shortest Path by Distance");
			System.out.println("\t4: Find Shortest Path by Cost");
			System.out.println("\t5: Find Shortest Path by Hops");
			System.out.println("\t6: Find Trips Under Cost");
			System.out.println("\t7: Add a Route");
			System.out.println("\t8: Remove a Route");
			System.out.println("\t9: Add a City");
			System.out.println("\t10: Remove a City");
			System.out.println("\t11: Quit");
			System.out.println("\t12: Quit Without Saving");
			System.out.print("Enter numeric choice: ");
			int choice = input.nextInt();
			input.nextLine(); // throw out leftover newline
			switch (choice) {
				case 1:
					a.showAllRoutes();
					break;
				case 2:
					a.mst();
					break;
				case 3:
					System.out.print("Enter the first city: ");
					city1 = input.nextLine();
					System.out.print("Enter the second city: ");
					city2 = input.nextLine();
					a.shortestByDistance(city1, city2);
					break;
				case 4:
					System.out.print("Enter the first city: ");
					city1 = input.nextLine();
					System.out.print("Enter the second city: ");
					city2 = input.nextLine();
					a.shortestByCost(city1, city2);
					break;
				case 5:
					System.out.print("Enter the first city: ");
					city1 = input.nextLine();
					System.out.print("Enter the second city: ");
					city2 = input.nextLine();
					a.shortestByHops(city1, city2);
					break;
				case 6:
					System.out.print("Enter max cost: ");
					double cost = input.nextInt();
					a.pathsUnderCost(cost);
					break;
				case 7:
					System.out.print("Enter the first city: ");
					city1 = input.nextLine();
					System.out.print("Enter the second city: ");
					city2 = input.nextLine();
					System.out.print("Enter the distance: ");
					int distance = input.nextInt();
					System.out.print("Enter the price: ");
					double price = input.nextDouble();
					a.addRoute(city1, city2, distance, price);
					break;
				case 8:
					System.out.print("Enter the first city: ");
					city1 = input.nextLine();
					System.out.print("Enter the second city: ");
					city2 = input.nextLine();
					a.removeRoute(city1, city2);
					break;
				case 9:
					System.out.print("Enter the city name: ");
					city1 = input.nextLine();
					a.addCity(city1);
					break;
				case 10:
					System.out.print("Enter the city name: ");
					city1 = input.nextLine();
					a.removeCity(city1);
					break;
				case 11:
					a.saveRoutes(filename);
					break loop;
				case 12:
					break loop;
			}
		}
	}
}