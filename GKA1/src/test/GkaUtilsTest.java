package test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
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
	private GkaGraph graph;
	private GkaGraph savedGraph;
	private ArrayList<String> nodeNames;
	private ArrayList<String> edgeNames;
	private int nodesNum;
	private int edgesNum;
	private ArrayList<String> nodeNamesTest;
	private ArrayList<String> edgeNamesTest;
	private ArrayList<String> nodeNamesSaveTest;
	private ArrayList<String> edgeNamesSaveTest;
	private GkaGraph randomGraph;

	/**
	 * To test GkaUtils.read, read the file graphTest1.gka whose attributes are
	 * known. The file contains lots of unusual format, suitable for testing
	 * corner cases. <br>
	 * To test GkaUtils.save, save the newly created graph from graphTest1.gka and try
	 * to read it and check if all the attributes are correct.
	 * 
	 * @throws UnsupportedEncodingException
	 *             - if encoding is not supported
	 * @throws IOException
	 *             - if an I/O Exception occurs
	 */
	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		nodesNum = 9;
		edgesNum = 13;
		nodeNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "x", "z"));
		edgeNames = new ArrayList<>(Arrays.asList("1 23", "Tokyo", "New York", "Hamburg"));

		// test read
		graph = GkaUtils.read("graphTest1.gka");

		// test save
		GkaUtils.save(graph, "savedGraphTest1.gka");
		savedGraph = GkaUtils.read("savedGraphTest1.gka");
	}

	/**
	 * Test GkaUtils.read().
	 */
	@Test()
	public void readTest() {
		nodeNamesTest = new ArrayList<>();
		edgeNamesTest = new ArrayList<>();

		for (Node node : graph) {
			nodeNamesTest.add(node.getAttribute("name").toString());
		}

		for (Edge edge : graph.getEachEdge()) {
			if (edge.hasAttribute("name")) {
				edgeNamesTest.add(edge.getAttribute("name").toString());
			}
		}

		assertTrue(nodeNamesTest.containsAll(nodeNames) && nodeNames.containsAll(nodeNamesTest));
		assertTrue(edgeNamesTest.containsAll(edgeNames) && edgeNames.containsAll(edgeNamesTest));
		assertEquals(nodesNum, graph.getNodeCount());
		assertEquals(edgesNum, graph.getEdgeCount());
	}

	/**
	 * Test GkaUtils.save().
	 */
	@Test()
	public void saveTest() {
		nodeNamesSaveTest = new ArrayList<>();
		edgeNamesSaveTest = new ArrayList<>();

		for (Node node : savedGraph) {
			nodeNamesSaveTest.add(node.getAttribute("name").toString());
		}

		for (Edge edge : savedGraph.getEachEdge()) {
			if (edge.hasAttribute("name")) {
				edgeNamesSaveTest.add(edge.getAttribute("name").toString());
			}
		}

		assertTrue(nodeNamesSaveTest.containsAll(nodeNames) && nodeNames.containsAll(nodeNamesSaveTest));
		assertTrue(edgeNamesSaveTest.containsAll(edgeNames) && edgeNames.containsAll(edgeNamesSaveTest));
		assertEquals(nodesNum, graph.getNodeCount());
		assertEquals(edgesNum, graph.getEdgeCount());
	}

	/**
	 * Test GkaUtils.generateRandom().
	 */
	@Test()
	public void generateRandomtest() {
		randomGraph = GkaUtils.generateRandom(1, 100, true, true);
		assertEquals(1, randomGraph.getNodeCount());
		assertEquals(100, randomGraph.getEdgeCount());
		randomGraph = GkaUtils.generateRandom(10, 200, false, true, -4, 1000);
		assertEquals(10, randomGraph.getNodeCount());
		assertEquals(200, randomGraph.getEdgeCount());
		randomGraph = GkaUtils.generateRandom(100, 2000, true, false, 0, 100);
		assertEquals(100, randomGraph.getNodeCount());
		assertEquals(2000, randomGraph.getEdgeCount());
	}

}
