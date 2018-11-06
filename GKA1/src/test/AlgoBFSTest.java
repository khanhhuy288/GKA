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

public class AlgoBFSTest {
	private GkaGraph graph;
	private ArrayList<String> traverseTest1;
	private ArrayList<String> traverseTest2;	
	private String[] pathArrTest;
	
	private GkaGraph randomGraph;


	@Before
	public void init() throws UnsupportedEncodingException, IOException {
		graph = GkaUtils.read("BFStest.gka");
		
		traverseTest1 = new ArrayList<>(Arrays.asList("s", "f", "a", "d", "e", "c", "b", "t"));
		traverseTest2 = new ArrayList<>(Arrays.asList("s", "f", "a", "d", "e", "c", "t", "b"));
		pathArrTest = new String[] {"s", "a", "e", "t"};

		
		
	}
	
	@Test
	public void testTraverse() {
		LinkedList<String> traverseList = new LinkedList<>();
		for (Node node : AlgoBFS.traverse(graph, "s")) {
			traverseList.add(node.getAttribute("name") + "");
		}
		List<String> traverseAL = new ArrayList<String>(traverseList);
		System.out.println(traverseAL);
		System.out.println(traverseTest2);
		boolean test1 = traverseAL.containsAll(traverseTest1) && traverseTest1.containsAll(traverseAL);
		boolean test2 = traverseAL.containsAll(traverseTest2) && traverseTest2.containsAll(traverseAL);
		assertTrue(test1 || test2);
	}
	
	@Test
	public void testDistance() {
		assertEquals(1, AlgoBFS.distance(graph, "s", "a"));
		assertEquals(4, AlgoBFS.distance(graph, "s", "b"));
		assertEquals(3, AlgoBFS.distance(graph, "s", "c"));
		assertEquals(2, AlgoBFS.distance(graph, "s", "d"));
		assertEquals(2, AlgoBFS.distance(graph, "s", "e"));
		assertEquals(1, AlgoBFS.distance(graph, "s", "f"));
		assertEquals(-1, AlgoBFS.distance(graph, "s", "g"));
		assertEquals(3, AlgoBFS.distance(graph, "s", "t"));
	}
	
	@Test
	public void testShortestPath() {
		Node[] pathArray = AlgoBFS.shortestPath(graph, "s", "t");
		String[] pathStrArray = new String[pathArray.length];
		for (int i = 0; i < pathArray.length; i++) {
			pathStrArray[i] = pathArray[i].getAttribute("name");
		}
		assertArrayEquals(pathArrTest, pathStrArray);
	}

}
