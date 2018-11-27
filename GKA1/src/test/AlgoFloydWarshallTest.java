package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.Node;
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
	private List<Node> shortestPathTest03;
	private double shortestDistanceTest03;
	private double shortestDistanceResult03;
	private APSP apsp03;
	private APSPInfo info03;
	
	private GkaGraph big;
	private String startNodeNameBig;
	private String endNodeNameBig;
	private List<Node> shortestPathTestBig;
	private double shortestDistanceTestBig;
	private double shortestDistanceResultBig;
	private APSP apspBig;
	private APSPInfo infoBig;
	
	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		filename = "VLtest.gka";
		graph = GkaUtils.read(filename);
		startNodeName = "1";
		endNodeName = "6";

		graph03File = "graph03.gka";
		graph03 = GkaUtils.read(graph03File);
		startNodeName03 = "Hamburg";
		endNodeName03 = "Norderstedt";		
		apsp03 = new APSP(graph03, "weight", false);
		
		GkaGraph big = GkaUtils.generateRandom(100, 3500, true, false, 1, 10);
		startNodeNameBig = "0";
		endNodeNameBig = "99";
		apspBig = new APSP(big, "weight", true);
		
		
	}

	/**
	 * Test shortest path between 2 nodes in the graph read from VLtest.gka.
	 */
	@Test
	public void testShortestPath() {
		shortestPathTest = GkaUtils
				.toNodesString(AlgoFloydWarshall.shortestPath(graph, startNodeName, endNodeName, false));
		shortestDistanceTest = AlgoFloydWarshall.distance(graph, startNodeName, endNodeName);

		assertEquals(7.0, shortestDistanceTest, 1);

		// print to console
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, startNodeName, endNodeName));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}

	/**
	 * Test shortest path between 2 nodes in the graph read from graph03.gka.
	 */
	@Test
	public void testShortestPath03() {
		shortestPathTest03 = AlgoFloydWarshall.shortestPath(graph03, startNodeName03, endNodeName03, false);
		shortestDistanceTest03 = AlgoFloydWarshall.distance(graph03, startNodeName03, endNodeName03);
		
		apsp03.compute();
		info03 = graph03.getNode(graph03.createNode(startNodeName03)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResult03 = info03.getLengthTo(graph03.createNode(endNodeName03));
		assertEquals(shortestDistanceResult03, shortestDistanceTest03, 1);

		// print to console
		System.out.println(
				String.format("Shortest path on %s from %s to %s:", graph03File, startNodeName03, endNodeName03));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTest03)));
		System.out.println();
	}
	
	/**
	 * Test shortest path between 2 nodes in BIG.
	 */
	@Test
	public void testShortestPathBig() {
		shortestPathTestBig = AlgoFloydWarshall.shortestPath(big, startNodeNameBig, endNodeNameBig, false);
		shortestDistanceTestBig = AlgoFloydWarshall.distance(big, startNodeNameBig, endNodeNameBig);
		
		apspBig.compute();
		infoBig = big.getNode(big.createNode(startNodeNameBig)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResultBig = infoBig.getLengthTo(big.createNode(endNodeNameBig));
		assertEquals(shortestDistanceResultBig, shortestDistanceTestBig, 1);

		// print to console
		System.out.println(
				String.format("Shortest path on BIG from %s to %s:", startNodeNameBig, endNodeNameBig));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTestBig)));
		System.out.println();
	}
}
