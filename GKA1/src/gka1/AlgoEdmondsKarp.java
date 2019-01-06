package gka1;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractNode;

public class AlgoEdmondsKarp {
	/**
	 * Find an shortest augmenting path from source 's' to sink 't' in the residual graph using
	 * breath-first-search.
	 *
	 * @param graphMatrix
	 *            adjacency matrix of the residual graph
	 * @param sourceIndex
	 *            index of start node in the graph
	 * @param sinkIndex
	 *            index of end node in the graph
	 * @param prev
	 *            array to store previous node of each node on the path
	 * @return true if an augmenting path is available.
	 */
	public static boolean augmentPath(int[][] graphMatrix, int sourceIndex, int sinkIndex, int prev[]) {
		int nodeNr = graphMatrix.length;

		// an array to track whether a node is visited
		boolean visited[] = new boolean[nodeNr];
		for (int i = 0; i < nodeNr; i++) {
			visited[i] = false;
		}

		// create a list for BFS for an augmenting path
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(sourceIndex);
		visited[sourceIndex] = true;
		prev[sourceIndex] = -1;

		while (!queue.isEmpty()) {
			int u = queue.poll();

			for (int v = 0; v < nodeNr; v++) {
				// if v isn't visited yet and the remaining capacity of edge u-v > 0
				if (!visited[v] && graphMatrix[u][v] > 0) {
					queue.add(v);
					prev[v] = u;
					visited[v] = true;
				}
			}
		}

		// return true if the sink could be reached from the source, else false
		return visited[sinkIndex];
	}

	/**
	 * Returns the maximum flow from source to sink in the given graph
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param sourceName
	 *            Name of source
	 * @param sinkName
	 *            Name of sink
	 * @return maximum flow through the graph
	 */
	public static int maxFlow(GkaGraph graph, String sourceName, String sinkName) {
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
				
				// backward edge, remaining capacity = flow, 
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

}
