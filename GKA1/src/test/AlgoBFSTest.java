package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Node;
import org.junit.Before;
import org.junit.Test;

import gka1.AlgoBFS;
import gka1.GkaGraph;
import gka1.GkaUtils;

/**
 * Test for AlgoBFS.
 * 
 * @author Tri Pham
 *
 */
public class AlgoBFSTest {
	private String filename;
	private String startNodeName;
	private String endNodeName;
	private String singleNodeName;
	private GkaGraph graph;
	private List<String> traversePathTest;
	private List<String> shortestPathTest;
	private List<String> noShortestPathTest;

	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		filename = "BFStest.gka";
		graph = GkaUtils.read(filename);
		startNodeName = "s";
		endNodeName = "t";
		singleNodeName = "g";
	}

	/**
	 * Test traversal from a node.
	 */
	@Test
	public void testTraverse() {
		traversePathTest = GkaUtils.toNodesString(AlgoBFS.traverse(graph, "s", false));
		
		assertEquals(8, traversePathTest.size());

		// print to console
		System.out.println(String.format("Traversal path on %s starting from %s:", filename, startNodeName));
		System.out.println(String.join(" -> ", traversePathTest));
		System.out.println();
	}

	/**
	 * Test shortest path between 2 valid nodes.
	 */
	@Test
	public void testShortestPath() {
		shortestPathTest = GkaUtils.toNodesString(AlgoBFS.shortestPath(graph, startNodeName, endNodeName, false));
		
		assertEquals(4, shortestPathTest.size());

		// print to console
		System.out.println(String.format("Shortest path on %s from %s to %s:", filename, startNodeName, endNodeName));
		System.out.println(String.join(" -> ", shortestPathTest));
		System.out.println();
	}
	
	/**
	 * Test shortest path between 2 unlinked nodes.
	 */
	@Test
	public void testNoShortestPath() {
		noShortestPathTest = GkaUtils.toNodesString(AlgoBFS.shortestPath(graph, startNodeName, singleNodeName, false));
		
		assertEquals(0, noShortestPathTest.size());
		
		// print to console
		System.out.println(String.format("No shortest path on %s from %s to %s:", filename, startNodeName, singleNodeName));
		System.out.println(String.join(" -> ", noShortestPathTest));
		System.out.println();
	}
}
