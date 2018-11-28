package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import gka1.AlgoDijkstra;
import gka1.GkaGraph;
import gka1.GkaUtils;

public class AlgoDiijkstraTest {
	private String filename;
	private GkaGraph graph;
	private List<String> shortestPathTest;
	private List<String> shortestPath0;
	private List<String> shortestPath1;
	private List<String> shortestPath2;
	private List<String> shortestPath3;

	/**
	 * Test shortest path between 2 valid nodes in graph03.gka.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testShortestPathGraph03() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		filename = "graph03.gka";
		graph = GkaUtils.read(filename);
		shortestPath0 = new ArrayList<>(Arrays.asList("Hamburg", "Lüneburg", "Lübeck"));
		shortestPath1 = new ArrayList<>(Arrays.asList("Kiel", "Husum"));
		shortestPath2 = new ArrayList<>(
				Arrays.asList("Lübeck", "Lüneburg", "Soltau", "Rotenburg", "Uelzen", "Kiel", "Husum"));

		// Hamburg -> Lübeck
		shortestPathTest = GkaUtils.toNodesString(AlgoDijkstra.shortestPath(graph, "Hamburg", "Lübeck", false));
		assertTrue(shortestPathTest.equals(shortestPath0));
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, "Hamburg", "Lübeck"));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();

		// Kiel -> Husum
		shortestPathTest = GkaUtils.toNodesString(AlgoDijkstra.shortestPath(graph, "Kiel", "Husum", false));
		assertTrue(shortestPathTest.equals(shortestPath1));
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, "Kiel", "Husum"));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();

		// Lübeck -> Husum
		shortestPathTest = GkaUtils.toNodesString(AlgoDijkstra.shortestPath(graph, "Lübeck", "Husum", false));
		assertTrue(shortestPathTest.equals(shortestPath2));
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, "Lübeck", "Husum"));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}

	/**
	 * Test shortest path between 2 valid nodes in a big graph with 100 nodes
	 * and 3500 edges.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testShortestPathBigGraph() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		graph = GkaUtils.generateRandom(100, 3500, true, false, 1, 10, 0);
		shortestPath3 = new ArrayList<>(Arrays.asList("0", "25", "93", "12", "99"));

		// Node 0 -> Node 99
		shortestPathTest = GkaUtils.toNodesString(AlgoDijkstra.shortestPath(graph, "0", "99", false));
		assertTrue(shortestPathTest.equals(shortestPath3));
		System.out.println(String.format("Shortest path on %s from %s to %s:", "Big Graph", "Node 1", "Node 99"));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}
}
