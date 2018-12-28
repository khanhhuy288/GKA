package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

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
	private String startNodeName03_1;
	private String endNodeName03_1;
	private String startNodeName03_2;
	private String endNodeName03_2;
	private String startNodeName03_3;
	private String endNodeName03_3;
	private GkaGraph graph03;
	private List<Node> shortestPathTest03_1;
	private double shortestDistanceTest03_1;
	private double shortestDistanceResult03_1;
	private List<Node> shortestPathTest03_2;
	private double shortestDistanceTest03_2;
	private double shortestDistanceResult03_2;
	private List<Node> shortestPathTest03_3;
	private double shortestDistanceTest03_3;
	private double shortestDistanceResult03_3;
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
	
	private GkaGraph[] bigGraphs;
	private double[] shortestDistanceTestBigs;
	private double[] shortestDistanceResultBigs;
	private APSP[] apspBigs;
	private APSPInfo[] infoBigs;

	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		filename = "VLtest.gka";
		graph = GkaUtils.read(filename);
		startNodeName = "1";
		endNodeName = "6";

		graph03File = "graph03.gka";
		graph03 = GkaUtils.read(graph03File);
		startNodeName03_1 = "Hamburg";
		endNodeName03_1 = "Lübeck";
		startNodeName03_2 = "Kiel";
		endNodeName03_2 = "Husum";
		startNodeName03_3 = "Lübeck";
		endNodeName03_3 = "Husum";
		apsp03 = new APSP(graph03, "weight", false);

		big = GkaUtils.generateRandom(100, 3500, true, false, 1, 10);
		startNodeNameBig = "0";
		endNodeNameBig = "99";
		apspBig = new APSP(big, "weight", true);

//		for (int i = 0; i < 100; i++) {
//			bigGraphs[i] = GkaUtils.generateRandom(100, 3500, true, false, 1, 10);
//			apspBigs[i] = new APSP(bigGraphs[i], "weight", true);
//		}
	}

	/**
	 * Test shortest path between 2 valid nodes in the graph read from VLtest.gka.
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
	 * Test shortest path between 2 valid nodes in the graph read from graph03.gka.
	 */
	@Test
	public void testShortestPath03() {
		// Hamburg -> Lübeck
		shortestPathTest03_1 = AlgoFloydWarshall.shortestPath(graph03, startNodeName03_1, endNodeName03_1, false);
		shortestDistanceTest03_1 = AlgoFloydWarshall.distance(graph03, startNodeName03_1, endNodeName03_1);

		apsp03.compute();
		info03 = graph03.getNode(graph03.createNode(startNodeName03_1)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResult03_1 = info03.getLengthTo(graph03.createNode(endNodeName03_1));
		assertEquals(shortestDistanceResult03_1, shortestDistanceTest03_1, 1);

		System.out.println(
				String.format("Shortest path on %s from %s to %s:", graph03File, startNodeName03_1, endNodeName03_1));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTest03_1)));
		System.out.println();

		// Kiel -> Husum
		shortestPathTest03_2 = AlgoFloydWarshall.shortestPath(graph03, startNodeName03_2, endNodeName03_2, false);
		shortestDistanceTest03_2 = AlgoFloydWarshall.distance(graph03, startNodeName03_2, endNodeName03_2);

		apsp03.compute();
		info03 = graph03.getNode(graph03.createNode(startNodeName03_2)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResult03_2 = info03.getLengthTo(graph03.createNode(endNodeName03_2));
		assertEquals(shortestDistanceResult03_2, shortestDistanceTest03_2, 1);

		System.out.println(
				String.format("Shortest path on %s from %s to %s:", graph03File, startNodeName03_2, endNodeName03_2));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTest03_2)));
		System.out.println();

		// Lübeck -> Husum
		shortestPathTest03_3 = AlgoFloydWarshall.shortestPath(graph03, startNodeName03_3, endNodeName03_3, false);
		shortestDistanceTest03_3 = AlgoFloydWarshall.distance(graph03, startNodeName03_3, endNodeName03_3);

		apsp03.compute();
		info03 = graph03.getNode(graph03.createNode(startNodeName03_3)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResult03_3 = info03.getLengthTo(graph03.createNode(endNodeName03_3));
		assertEquals(shortestDistanceResult03_3, shortestDistanceTest03_3, 1);

		System.out.println(
				String.format("Shortest path on %s from %s to %s:", graph03File, startNodeName03_3, endNodeName03_3));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTest03_3)));
		System.out.println();
	}

	/**
	 * Test shortest path between 2 valid nodes in a big graph with 100 nodes.
	 */
	@Test
	public void testShortestPathBig() {
		shortestPathTestBig = AlgoFloydWarshall.shortestPath(big, startNodeNameBig, endNodeNameBig, false);
		shortestDistanceTestBig = AlgoFloydWarshall.distance(big, startNodeNameBig, endNodeNameBig);

		apspBig.compute();
		infoBig = big.getNode(big.createNode(startNodeNameBig)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		shortestDistanceResultBig = infoBig.getLengthTo(big.createNode(endNodeNameBig));

		// print to console
		System.out.println(String.format("Shortest path on BIG from %s to %s:", startNodeNameBig, endNodeNameBig));
		System.out.println(String.join(" -> ", GkaUtils.toNodesString(shortestPathTestBig)));
		System.out.println("Path length according to my Floyd-Warshall: " + shortestDistanceTestBig);
		System.out.println("Path length according to GraphStream: " + shortestDistanceResultBig);
		System.out.println();
		
		assertEquals(shortestDistanceResultBig, shortestDistanceTestBig, 0.000000001);
	}
	
//	/**
//	 * Test shortest path between 2 valid nodes in 100 big graphs with 100 nodes.
//	 */
//	@Test
//	public void testShortestPathBig100() {
//		for (int i = 0; i < 99; i++) {
//			shortestDistanceTestBigs[i] = AlgoFloydWarshall.distance(bigGraphs[i], startNodeNameBig, endNodeNameBig);
//
//			apspBigs[i].compute();
//			infoBigs[i] = big.getNode(big.createNode(startNodeNameBig)).getAttribute(APSPInfo.ATTRIBUTE_NAME);
//			shortestDistanceResultBigs[i] = infoBigs[i].getLengthTo(big.createNode(endNodeNameBig));
//			assertEquals(shortestDistanceResultBigs[i], shortestDistanceTestBigs[i], 0.000000001);
//		}
//	}
}
