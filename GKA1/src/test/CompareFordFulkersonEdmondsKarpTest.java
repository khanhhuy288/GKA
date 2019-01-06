package test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

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
		int maxFlowFF = AlgoFordFulkerson.maxFlow(graph, "v1", "v8");
		long end = System.nanoTime();

		System.out.printf("Runtime graph04Test1 (FF) = %d ns%n", end - start);
		assertEquals(20, maxFlowFF);

		start = System.nanoTime();
		int maxFlowEK = AlgoEdmondsKarp.maxFlow(graph, "v1", "v8");
		end = System.nanoTime();

		assertEquals(20, maxFlowEK);
		System.out.printf("Runtime graph04Test1 (EK) = %d ns%n%n", end - start);
	}

	@Test
	public void graph04Test2() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("graph04.gka");

		long start = System.nanoTime();
		int maxFlowFF = AlgoFordFulkerson.maxFlow(graph, "v1", "s");
		long end = System.nanoTime();

		System.out.printf("Runtime graph04Test2 (FF) = %d ns%n", end - start);
		assertEquals(23, maxFlowFF);

		start = System.nanoTime();
		int maxFlowEK = AlgoEdmondsKarp.maxFlow(graph, "v1", "s");
		end = System.nanoTime();

		assertEquals(23, maxFlowEK);
		System.out.printf("Runtime graph04Test2 (EK) = %d ns%n%n", end - start);
	}

	@Test
	public void bigNetTest() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph bigNet = GkaUtils.generateNetwork(50, 800, 1, 100);

		long start = System.nanoTime();
		int maxFlowFF = AlgoFordFulkerson.maxFlow(bigNet, "0", "49");
		long end = System.nanoTime();
		System.out.printf("Runtime bigNetTest (FF) = %d ns%n", end - start);

		start = System.nanoTime();
		int maxFlowEK = AlgoEdmondsKarp.maxFlow(bigNet, "0", "49");
		end = System.nanoTime();
		System.out.printf("Runtime bigNetTest (EK) = %d ns%n%n", end - start);

		assertEquals(maxFlowFF, maxFlowEK);

	}

	@Test
	public void bigNetTest100() throws UnsupportedEncodingException, FileNotFoundException, IOException {
		double meanRelDiff = 0.0;
		for (int i = 0; i < 99; i++) {
			Random rand = new Random();
			
			// 2 <= nodeNum <= 100
			int minNodeNum = 2;
			int maxNodeNum = 100;
			int nodeNum = minNodeNum + rand.nextInt(maxNodeNum - minNodeNum + 1);
			
			// n-1 <= edgeNum <= n(n-1)/2, n is nodeNum
			int minEdgeNum = nodeNum - 1;
			int maxEdgeNum = nodeNum * (nodeNum - 1) / 2;
			int edgeNum = minEdgeNum + rand.nextInt(maxEdgeNum - minEdgeNum + 1);
			
			GkaGraph bigNet = GkaUtils.generateNetwork(nodeNum, edgeNum, 1, 100);

			long start = System.nanoTime();
			int maxFlowFF = AlgoFordFulkerson.maxFlow(bigNet, "0", String.valueOf(nodeNum - 1));
			long end = System.nanoTime();
			long runTimeFF = end - start;

			start = System.nanoTime();
			int maxFlowEK = AlgoEdmondsKarp.maxFlow(bigNet, "0", String.valueOf(nodeNum - 1));
			end = System.nanoTime();
			long runTimeEK = end - start;
			
			double relativeDifference = (double)(runTimeFF - runTimeEK) / runTimeEK * 100;
			meanRelDiff += relativeDifference;

			assertEquals(maxFlowFF, maxFlowEK);
		}
		
		System.out.printf("Edmonds-Karp runs faster than Ford-Fulkerson by average %.2f%% %n%n", (meanRelDiff / 100));

	}
}
