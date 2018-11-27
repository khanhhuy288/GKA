package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import gka1.AlgoFloydWarshall;
import gka1.GkaGraph;
import gka1.GkaUtils;

public class AlgoFloydWarshallTest {

	private String filename;
	private String startNodeName;
	private String endNodeName;
	private GkaGraph graph;
	private List<String> shortestPathTest;
	private double shortestDistanceTest;
	
	private String graph03File;
	private String startNodeName03;
	private String endNodeName03;
	private GkaGraph graph03;
	private List<String> shortestPathTest03;

	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		filename = "VLtest.gka";
		graph = GkaUtils.read(filename);
		startNodeName = "1";
		endNodeName = "6";
		
		graph03File = "graph03.gka";
		graph03 = GkaUtils.read(graph03File);
		startNodeName03 = "Hamburg";
		endNodeName03 = "Hameln";
	}

	
	/**
	 * Test shortest path between 2 nodes in the graph read from VLtest.gka.
	 */
	@Test
	public void testShortestPath() {
		shortestPathTest = GkaUtils.toNodesString(AlgoFloydWarshall.shortestPath(graph, startNodeName, endNodeName, false));
		shortestDistanceTest = AlgoFloydWarshall.distance(graph, startNodeName, endNodeName);
		
		assertEquals(7.0, shortestDistanceTest, 1);

		// print to console
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, startNodeName, endNodeName));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}
	
	/**
	 * Test shortest path between 2 nodes read from graph03.gka.
	 */
	@Test
	public void testShortestPath03() {
		shortestPathTest = GkaUtils.toNodesString(AlgoFloydWarshall.shortestPath(graph03, startNodeName03, endNodeName03, false));
		
		// print to console
		System.out.println(String.format("Shortest path on %s from %s to %s:", graph03File, startNodeName03, endNodeName03));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}

}
