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
 * @author Tri Pham
 *
 */
public class AlgoBFSTest {
	private GkaGraph graph;
	private List<String> traversePath;
	private List<String> shortestPath;
	private List<String> traversePathTest;
	private List<String> shortestPathTest;

	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		graph = GkaUtils.read("BFStest.gka");
	}
	
	/**
	 * Test for traversal.
	 */
	@Test
	public void testTraverse() {
		traversePath = new ArrayList<>(Arrays.asList("s", "a", "f", "e", "d", "b", "t", "c"));
		traversePathTest = GkaUtils.toNodesString(AlgoBFS.traverse(graph, "s", false));
		assertTrue(traversePathTest.equals(traversePath));
	}
	
	/**
	 * Test for shorest path.
	 */
	@Test
	public void testShortestPath() {
		shortestPath = new ArrayList<>(Arrays.asList("s", "a", "e", "t"));
		shortestPathTest = GkaUtils.toNodesString(AlgoBFS.shortestPath(graph, "s", "t", false));
		assertEquals(shortestPathTest.size(), shortestPathTest.size());
		assertTrue(shortestPathTest.equals(shortestPath));
	}

}
