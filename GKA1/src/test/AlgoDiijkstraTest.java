package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphstream.algorithm.Dijkstra;
import org.graphstream.algorithm.Dijkstra.Element;
import org.junit.Test;

import gka1.AlgoDijkstra;
import gka1.GkaGraph;
import gka1.GkaUtils;

public class AlgoDiijkstraTest {
	private String filename;
	private GkaGraph graph;
	private Double shortestPathTest;

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

		// Hamburg -> Lübeck
		shortestPathTest = AlgoDijkstra.shortestPath(graph, "Hamburg", "Lübeck", false);

		Dijkstra dijkstra = new Dijkstra(Element.EDGE, null, "weight");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(graph.createNode("Hamburg")));
		dijkstra.compute();
		double result = dijkstra.getPathLength(graph.getNode(graph.createNode("Lübeck")));

		assertEquals(result, shortestPathTest, 0.0000001);

		// Kiel -> Husum
		shortestPathTest = AlgoDijkstra.shortestPath(graph, "Kiel", "Husum", false);
		dijkstra = new Dijkstra(Element.EDGE, null, "weight");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(graph.createNode("Kiel")));
		dijkstra.compute();
		result = dijkstra.getPathLength(graph.getNode(graph.createNode("Husum")));

		assertEquals(result, shortestPathTest, 0.0000001);

		// Lübeck -> Husum
		shortestPathTest = AlgoDijkstra.shortestPath(graph, "Lübeck", "Husum", false);
		dijkstra = new Dijkstra(Element.EDGE, null, "weight");
		dijkstra.init(graph);
		dijkstra.setSource(graph.getNode(graph.createNode("Lübeck")));
		dijkstra.compute();
		result = dijkstra.getPathLength(graph.getNode(graph.createNode("Husum")));

		assertEquals(result, shortestPathTest, 0.0000001);
	}

	/**
	 * Test shortest path between 2 valid nodes in a big graph with 100 nodes and
	 * 3500 edges.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void testShortestPathBigGraph() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		for (int i = 0; i < 100; i++) {
			graph = GkaUtils.generateRandom(100, 3500, true, false, 99, 1000);

			// Node 0 -> Node 99
			shortestPathTest = AlgoDijkstra.shortestPath(graph, "0", "99", false);
			Dijkstra dijkstra = new Dijkstra(Element.EDGE, null, "weight");
			dijkstra.init(graph);
			dijkstra.setSource(graph.getNode(graph.createNode("0")));
			dijkstra.compute();
			double result = dijkstra.getPathLength(graph.getNode(graph.createNode("99")));

			assertEquals(result, shortestPathTest, 0.0000001);
		}
	}
}
