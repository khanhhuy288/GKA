package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.graphstream.graph.Node;
import org.junit.Test;

import gka1.AlgoEdmondsKarp;
import gka1.AlgoFordFulkerson;
import gka1.GkaGraph;
import gka1.GkaUtils;

public class CompareFordFulkersonEdmondsKarpTest {

	@Test
	public void graph04Test1() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("graph04.gka");

		long start = System.nanoTime();
		int maxFlowFF = AlgoFordFulkerson.solve(graph, "v1", "v8");
		long end = System.nanoTime();

		System.out.printf("Runtime (FF) = %d ns%n", end - start);
		assertEquals(20, maxFlowFF);
		
		start = System.nanoTime();
		int maxFlowEK = AlgoEdmondsKarp.solve(graph, "v1", "v8");
		end = System.nanoTime();

        assertEquals(20, maxFlowEK);
        System.out.printf("Runtime (EK) = %d ns%n%n", end - start);
	}

	@Test
	public void graph04Test2() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("graph04.gka");

		long start = System.nanoTime();
		int maxFlowFF = AlgoFordFulkerson.solve(graph, "v1", "s");
		long end = System.nanoTime();

		System.out.printf("Runtime (FF) = %d ns%n", end - start);
		assertEquals(23, maxFlowFF);
		
		start = System.nanoTime();
		int maxFlowEK = AlgoEdmondsKarp.solve(graph, "v1", "s");
		end = System.nanoTime();
		
        assertEquals(23, maxFlowEK);
        System.out.printf("Runtime (EK) = %d ns%n%n", end - start);
	}
}
