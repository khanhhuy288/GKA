package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Stack;
import org.graphstream.graph.Node;

/**
 * Contain methods to find the maximum flow of a flow network using
 * depth-first-search (DFS).
 * 
 * @author Huy Tran
 *
 */
public class AlgoFordFulkerson {
	/**
	 * Create the adjacency matrix of a given graph. Cell equals the capacity
	 * between 2 nodes if there's an edge from 1 node to the other, 0 otherwise.
	 * 
	 * @param graph The graph to work with.
	 * @return Adjacency matrix.
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

	/**
	 * Find an augmenting path from source 's' to sink 't' in the residual graph
	 * using depth-first-search.
	 *
	 * @param graphMatrix Adjacency matrix of the residual graph.
	 * @param sourceIndex Index of the source node in the network.
	 * @param sinkIndex   Index of the sink node in the network.
	 * @param parent        Array to store previous node of each node on the path.
	 * @return true if there is an augmenting path from source to sink, otherwise
	 *         false.
	 */
	public static boolean getAugmentingPath(int[][] graphMatrix, int sourceIndex, int sinkIndex, int[] parent) {
		// get number of nodes
		int nodeNr = graphMatrix.length;

		// create an array for tracking visited nodes
		boolean visited[] = new boolean[nodeNr];
		for (int i = 0; i < nodeNr; i++) {
			visited[i] = false;
		}

		// create a stack for DFS, the path from source to sink will be store in the
		// stack in reverse order (source at the bottom, sink at the top)
		Stack<Integer> stack = new Stack<>();
		stack.push(sourceIndex);

		// loop through the stack to find the path from source to sink
		// pop that node to backtrack and then continue when reaching dead end
		while (!stack.empty()) {
			// create a flag for new node in stack
			boolean stackIncreased = false;

			// get a node from stack
			int curr = stack.peek();

			// mark current node as visited
			if (!visited[curr]) {
				visited[curr] = true;
			}

			// traceback the path with parent[] and end the loop if sink is reached
			if (curr == sinkIndex) {
				while (!stack.empty()) {
					int i = stack.pop();
					if (!stack.empty()) {
						parent[i] = stack.peek();
					} else {
						parent[i] = -1;
					}
				}
				break;
			}

			// find one unvisited neighbor of current node
			for (int v = 0; v < nodeNr; v++) {
				// push it into stack if it's not visited and the remaining capacity of edge u-v
				// > 0
				if (!visited[v] && graphMatrix[curr][v] > 0) {
					stack.push(v);
					stackIncreased = true;
					break;
				}
			}

			// remove current node if it doesn't have an unvisited neighbor
			if (stackIncreased == false) {
				stack.pop();
			}
		}

		// return true if the sink could be reached from the source, else false
		return visited[sinkIndex];
	}

	/**
	 * Calculate the maximum flow from source to sink of a flow network.
	 * 
	 * @param graph      The graph to work with.
	 * @param sourceName Name of the source node.
	 * @param sinkName   Name of the sink node.
	 * @return Maximum flow of the network.
	 */
	public static int maxFlow(GkaGraph graph, String sourceName, String sinkName) {
		// check if nodes exist
		if (!graph.getNodeNames().contains(sourceName) || !graph.getNodeNames().contains(sinkName)) {
			throw new IllegalArgumentException("Node not found in graph.");
		}

		// get index of souce and sink nodes
		int sourceIndex = graph.getNode(graph.createNode(sourceName)).getIndex();
		int sinkIndex = graph.getNode(graph.createNode(sinkName)).getIndex();

		// create the adjacency matrix of the graph
		int[][] graphMatrix = graphMatrix(graph);
		int nodeNr = graph.getNodeCount();
		int maxFlow = 0;

		/*
		 * create the adjacency matrix of the residual graph. Its value at position
		 * (i,j) indicates the remaining capacity of the edge from i to j. Initial
		 * values are edge capacities.
		 */
		int rGraph[][] = new int[nodeNr][nodeNr];

		for (int u = 0; u < nodeNr; u++) {
			for (int v = 0; v < nodeNr; v++) {
				rGraph[u][v] = graphMatrix[u][v];
			}
		}

		// create an array for storing path
		int parent[] = new int[nodeNr];

		// augment the flow while there is path from source to sink with DFS
		while (getAugmentingPath(rGraph, sourceIndex, sinkIndex, parent)) {
			// find minimum residual capacity (bottleneck) of the edges along the path
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

				// backward edge, remaining capacity = flow,
				// flow increases => r.cap. increases
				rGraph[v][u] += bottleneck;
			}

			// add path flow to overall flow
			maxFlow += bottleneck;
		}

		// return the overall flow
		return maxFlow;
	}

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("BFSsave.gka");
		int maxflow = AlgoFordFulkerson.maxFlow(graph, "s", "t");
		System.out.println("maxflow = " + maxflow);
	}
}
