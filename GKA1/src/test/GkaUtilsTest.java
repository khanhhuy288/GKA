package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.junit.Before;
import org.junit.Test;

import gka1.GkaGraph;
import gka1.GkaUtils;

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

	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		nodesNum = 9;
		edgesNum = 13;		
		nodeNames = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "x", "z"));
		edgeNames = new ArrayList<>(Arrays.asList("1 23", "Tokyo", "New York", "Hamburg"));
		
		graph = GkaUtils.read("graphTest1.gka");
		GkaUtils.save(graph, "savedGraphTest1.gka");
		savedGraph = GkaUtils.read("savedGraphTest1.gka");
	}

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

	@Test()
	public void generateRandomtest() {
		randomGraph = GkaUtils.generateRandom(10, 200, true, true);
		assertEquals(10, randomGraph.getNodeCount());
	}

}
