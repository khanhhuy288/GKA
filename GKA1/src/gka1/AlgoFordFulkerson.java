package gka1;

import static java.lang.Math.min;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class AlgoFordFulkerson {

	/**
	 * Find an augmenting path from source 's' to sink 't' in the residual graph using
	 * depth-first-search.
	 *
	 * @param graphMatrix
	 *            adjacency matrix of the residual graph
	 * @param sourceIndex
	 *            index of start node in the graph
	 * @param sinkIndex
	 *            index of end node in the graph
	 * @param prev
	 *            array to store previous node of each node on the path
	 * @return true if there is an augmenting path from source to sink.
	 */
	public static boolean augmentPath(int[][] graphMatrix, int sourceIndex, int sinkIndex, int[] prev) {
		int nodeNr = graphMatrix.length;

		// an array to track whether a node is visited
		boolean visited[] = new boolean[nodeNr];
		for (int i = 0; i < nodeNr; i++) {
			visited[i] = false;
		}

		// Create a stack for DFS, the path from source to sink will be store in the
		// stack in reversed order (sink on top, source at the bottom)
		Stack<Integer> stack = new Stack<>();

		stack.push(sourceIndex);
		int stackLength = 1;

		// loop through the stack to find and rebuild the path from source to sink. If
		// we reach a dead end, pop that node to backtrack and then continue
		while (!stack.empty()) {
			// check if a new node is pushed into the stack
			boolean stackIncreased = false;

			int curr = stack.peek();

			if (!visited[curr]) {
				visited[curr] = true;
			}

			// if sink is reached, fill prev[] and then end the loop
			if (curr == sinkIndex) {
				// pop the top node. The new node at the top
				// is the previous node of the popped node
				while (!stack.empty()) {
					int i = stack.pop();
					if (!stack.empty()) {
						prev[i] = stack.peek();
					} else {
						// source is reached, prev[source] = -1
						prev[i] = -1;
					}
				}
				break;
			}

			// find an unvisited neighbor of curr
			for (int v = 0; v < nodeNr; v++) {
				// if v isn't visited yet and the remaining capacity of edge u-v > 0
				if (!visited[v] && graphMatrix[curr][v] > 0) {
					stack.push(v);
					stackLength++;
					stackIncreased = true;
					break;
				}
			}

			// backtrack if curr doesn't have unvisited neighbor
			if (stackIncreased == false) {
				stack.pop();
				stackLength--;
			}
		}

		// return true if the sink could be reached from the source, else false
		return visited[sinkIndex];
	}

	/**
	 * Calculate the maximum flow from source to sink in the given graph
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param sourceName
	 *            Name of source
	 * @param sinkName
	 *            Name of sink
	 * @return maximum flow through the graph
	 */
	public static int solve(GkaGraph graph, String sourceName, String sinkName) {
		// get index of start and end nodes
		int sourceIndex = graph.getNode(graph.createNode(sourceName)).getIndex();
		int sinkIndex = graph.getNode(graph.createNode(sinkName)).getIndex();

		// adjacency matrix and number of nodes of the graph
		int[][] graphMatrix = graphMatrix(graph);
		int nodeNr = graph.getNodeCount();

		int maxFlow = 0;

		/*
		 * Adjacency matrix of the residual graph. Its value at position (i,j) indicates
		 * the remaining capacity of the edge from i to j. Initial values are edge
		 * capacities.
		 */
		int rGraph[][] = new int[nodeNr][nodeNr];

		for (int u = 0; u < nodeNr; u++) {
			for (int v = 0; v < nodeNr; v++) {
				rGraph[u][v] = graphMatrix[u][v];
			}

		}
		// This array will be filled when augmenting path to store path
		int parent[] = new int[nodeNr];

		// Augment the flow while there is path from source to sink
		while (augmentPath(rGraph, sourceIndex, sinkIndex, parent)) {

			// minimum residual capacity of the edges along the path filled by BFS.
			int bottleneck = Integer.MAX_VALUE;
			for (int v = sinkIndex; v != sourceIndex; v = parent[v]) {
				int u = parent[v];
				bottleneck = Math.min(bottleneck, rGraph[u][v]);
			}

			// update residual capacities of the forward and backward edges along the path
			for (int v = sinkIndex; v != sourceIndex; v = parent[v]) {
				int u = parent[v];

				// forward edge, remaining capacity = capacity - flow,
				// flow increases => r.cap. decreases
				rGraph[u][v] -= bottleneck;

				// backward edge, 'remaining capacity' = flow,
				// flow increases => r.cap. increases
				rGraph[v][u] += bottleneck;
			}

			// Add path flow to overall flow
			maxFlow += bottleneck;
		}

		// Return the overall flow
		return maxFlow;
	}

	/**
	 * Give the adjacency matrix of a given graph
	 * 
	 * @param graph
	 *            The graph to work with
	 * @return adjacency matrix
	 */
	public static int[][] graphMatrix(GkaGraph graph) {
		int nodeNr = graph.getNodeCount();
		int graphMatrix[][] = new int[nodeNr][nodeNr];

		for (int i = 0; i < nodeNr; i++) {
			for (int j = 0; j < nodeNr; j++) {
				Node iNode = graph.getNode(i);
				Node jNode = graph.getNode(j);
				if (iNode.hasEdgeToward(jNode)) {
					graphMatrix[i][j] = iNode.getEdgeToward(jNode).getAttribute("weight");

				} else {
					graphMatrix[i][j] = 0;
				}
			}
		}

		return graphMatrix;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("BFSsave.gka");
		int maxflow = AlgoFordFulkerson.solve(graph, "s", "t");
		System.out.println("maxflow = " + maxflow);
	}
}
