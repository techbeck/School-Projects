/*
Rebecca Addison
CS1501
*/

import java.io.*;
import java.util.Scanner;

public class Airline {
	// Graph Variables
	private int numCities;
	private int numRoutes;
	private City[] cities;
	private Bag<Route>[] adj;	// adjacency list indexed on city id's (adj[id-1])

	// MST Variables
	private Route[] edgeTo;        // edgeTo[v] = shortest edge from tree vertex to non-tree vertex
	private int[] distTo;      // distTo[v] = weight of shortest such edge
	private boolean[] marked;     // marked[v] = true if v on tree, false otherwise
	private IndexMinPQ<Integer> pq;

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
		scan.nextLine();	// throw out leftover newline
		for (int i = 0; i < numCities; i++) {
			String name = scan.nextLine();
			System.out.println(name);
			cities[i] = new City(i+1, name);
			System.out.println(cities[i]);
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
			int city1ID = Integer.parseInt(inArray[0]);
			int city2ID = Integer.parseInt(inArray[1]);
			int distance = Integer.parseInt(inArray[2]);
			double price = Double.parseDouble(inArray[3]);
			addRoute(new Route(cities[city1ID-1], cities[city2ID-1], distance, price));
		}
		scan.close();
	}

	public void addRoute(Route r) {
		City city1 = r.fst();
		City city2 = r.snd();
		adj[city1.id()-1].add(r);
		adj[city2.id()-1].add(r);
		numRoutes++;
	}

	public void showAllRoutes() {
		System.out.println("SHOWING ALL DIRECT ROUTES");
		System.out.println("Note that routes are duplicated," + 
							" once from each end city's point of view");
		System.out.println("--------------------------------" +
							"-----------------------------------------");
		for (int i = 0; i < numCities; i++) {
			for (Route r : adj[i]) {
				System.out.println(r.toStringWN());
			}
		}
	}

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
				System.out.println(r.fst() + "," + r.snd() + " : " + r.distance());
			}
			scan(v);
		}
	}

	// scan city v
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
		for (int i = 0; i < numCities; i++) {
			for (Route r : adj[i]) {
				System.out.println(r);
			}
		}
	}

	// Tester
	public static void main(String[] args) {
		Airline a = new Airline("a5data1.txt");
		a.mst();
		a.saveRoutes("a5data1.txt");
	}
}