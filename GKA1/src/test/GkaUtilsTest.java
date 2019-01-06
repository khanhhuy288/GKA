package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.graphstream.graph.Edge;
import org.junit.Before;
import org.junit.Test;

import gka1.GkaGraph;
import gka1.GkaUtils;

/**
 * Test for GkaUtils.
 * 
 * @author Huy Tran PC
 *
 */
public class GkaUtilsTest {
	private String filename;
	private GkaGraph graph;
	private GkaGraph savedGraph;
	private List<String> nodeNames;
	private List<String> edgeNames;
	private int nodesNum;
	private int edgesNum;
	private List<String> nodeNamesTest;
	private List<String> edgeNamesTest;
	private List<String> nodeNamesSaveTest;
	private List<String> edgeNamesSaveTest;
	private GkaGraph randomGraph;

	/**
	 * Set up the correct attributes of graphTest1
	 *
	 */
	@Before
	public void init() {
		filename = "graphTest1.gka";
		nodesNum = 9;
		edgesNum = 13;
		nodeNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "x", "z"));
		edgeNames = new ArrayList<>(Arrays.asList("1 23", "Tokyo", "New York", "Hamburg"));
	}

	/**
	 * Test GkaUtils.read(). Read the file graphTest1.gka whose attributes are
	 * known. The file contains lots of unusual format, suitable for testing
	 * corner cases.
	 * 
	 * @throws IOException
	 *             - if an I/0 exception occurs
	 * @throws FileNotFoundException
	 *             - if file is not found
	 * @throws UnsupportedEncodingException
	 *             - if encoding of the file is not supported
	 */
	@Test()
	public void readTest() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		graph = GkaUtils.read(filename);

		// get nodes and edge of graph
		nodeNamesTest = graph.getNodeNames();
		edgeNamesTest = graph.getEdgeNames();

		assertTrue(nodeNamesTest.containsAll(nodeNames) && nodeNames.containsAll(nodeNamesTest));
		assertTrue(edgeNamesTest.containsAll(edgeNames) && edgeNames.containsAll(edgeNamesTest));
		assertEquals(nodesNum, graph.getNodeCount());
		assertEquals(edgesNum, graph.getEdgeCount());

		// print to console
		System.out.println("Reading " + filename + ":");
		System.out.println("Node names: " + nodeNamesTest);
		System.out.println("Edge names: " + edgeNamesTest);
		System.out.println();

	}

	/**
	 * Test GkaUtils.save(). Read the file graphTest1.gka and save the newly
	 * created graph from graphTest1.gka and try to read it and check if all the
	 * attributes are correct.
	 * 
	 * @throws IOException
	 *             - if an I/0 exception occurs
	 * @throws FileNotFoundException
	 *             - if file is not found
	 * @throws UnsupportedEncodingException
	 *             - if encoding of the file is not supported
	 */
	@Test()
	public void saveTest() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		graph = GkaUtils.read("graphTest1.gka");
		GkaUtils.save(graph, "savedGraphTest1.gka");
		savedGraph = GkaUtils.read("savedGraphTest1.gka");

		// get nodes and edges of savedGraph
		nodeNamesSaveTest = savedGraph.getNodeNames();
		edgeNamesSaveTest = savedGraph.getEdgeNames();

		assertTrue(nodeNamesSaveTest.containsAll(nodeNames) && nodeNames.containsAll(nodeNamesSaveTest));
		assertTrue(edgeNamesSaveTest.containsAll(edgeNames) && edgeNames.containsAll(edgeNamesSaveTest));
		assertEquals(nodesNum, graph.getNodeCount());
		assertEquals(edgesNum, graph.getEdgeCount());

		// print to console
		System.out.println("Saving " + filename + ":");
		System.out.println("Node names: " + nodeNamesSaveTest);
		System.out.println("Edge names: " + edgeNamesSaveTest);
		System.out.println();
	}

//	/**
//	 * Test GkaUtils.generateRandom().
//	 */
//	@Test()
//	public void generateRandomtest() {
//		int edgeWeight;
//
//		// test first random graph
//		randomGraph = GkaUtils.generateRandom(1, 100, true, true);
//		assertEquals(1, randomGraph.getNodeCount());
//		assertEquals(100, randomGraph.getEdgeCount());
//
//		for (Edge edge : randomGraph.getEachEdge()) {
//			assertTrue(edge.isDirected());
//			assertTrue(edge.hasAttribute("name"));
//		}
//
//		// test second random graph
//		randomGraph = GkaUtils.generateRandom(10, 200, false, true, -4, 1000);
//		assertEquals(10, randomGraph.getNodeCount());
//		assertEquals(200, randomGraph.getEdgeCount());
//
//		for (Edge edge : randomGraph.getEachEdge()) {
//			assertFalse(edge.isDirected());
//			assertTrue(edge.hasAttribute("name"));
//			edgeWeight = Integer.valueOf(edge.getAttribute("weight").toString());
//			assertTrue(edgeWeight >= -4 && edgeWeight <= 1000);
//		}
//
//		// test third random graph
//		randomGraph = GkaUtils.generateRandom(100, 2000, true, false, 0, 100);
//		assertEquals(100, randomGraph.getNodeCount());
//		assertEquals(2000, randomGraph.getEdgeCount());
//
//		for (Edge edge : randomGraph.getEachEdge()) {
//			assertTrue(edge.isDirected());
//			assertFalse(edge.hasAttribute("name"));
//			edgeWeight = Integer.valueOf(edge.getAttribute("weight").toString());
//			assertTrue(edgeWeight >= 0 && edgeWeight <= 100);
//		}
//	}

	/**
	 * Test GkaUtils.generateNetwork().
	 */
	@Test()
	public void generateNetworktest() {
		int edgeWeight;
		
		for (int i = 0; i < 5; i++) {
			int nodeNum = 10;
			int edgeNum = 50;
			GkaGraph bigNet = GkaUtils.generateNetwork(nodeNum, edgeNum, 1, 50);
			assertEquals(nodeNum, bigNet.getNodeCount());
			assertEquals(edgeNum, bigNet.getEdgeCount());
		}

	}

}
