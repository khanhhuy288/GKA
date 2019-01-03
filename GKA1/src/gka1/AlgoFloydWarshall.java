package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.APSP;
import org.graphstream.algorithm.APSP.APSPInfo;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Contain methods to find the shortest path using Floyd-Warshall.
 * 
 * @author Tri Pham
 *
 */
public class AlgoFloydWarshall {
	private static class Result {
		private int nodeNr;
		public double[][] distance;
		public int[][] transit;

		public Result(int nodeNr) {
			this.nodeNr = nodeNr;
			this.distance = new double[nodeNr][nodeNr];
			this.transit = new int[nodeNr][nodeNr];
		}
	}

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("VLtest.gka");
		List<Node> shortestPath = AlgoFloydWarshall.shortestPath(graph, "1", "6", true);
		System.out.println(GkaUtils.toNodesString(shortestPath));
		System.out.println(AlgoFloydWarshall.distance(graph, "1", "6"));

		APSP apsp = new APSP(graph, "weight", true);
		apsp.compute();
		APSPInfo info = graph.getNode(graph.createNode("1")).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		System.out.println(info.getLengthTo(graph.createNode("6")));

		// GkaGraph graph = GkaUtils.read("graph03.gka");
		// List<Node> shortestPath = AlgoFloydWarshall.shortestPath(graph, "Hamburg",
		// "Norderstedt", true);
		// System.out.println(GkaUtils.toNodesString(shortestPath));
		// System.out.println(AlgoFloydWarshall.distance(graph, "Hamburg",
		// "Norderstedt"));
		//
		// APSP apsp = new APSP(graph, "weight", false);
		// apsp.compute();
		// APSPInfo info =
		// graph.getNode(graph.createNode("Hamburg")).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		// System.out.println(info.getLengthTo(graph.createNode("Norderstedt")));

		// GkaGraph graph = GkaUtils.generateRandom(100, 3500, true, false, 1, 10, 0);
		// List<Node> shortestPath = AlgoFloydWarshall.shortestPath(graph, "0", "99",
		// true);
		// System.out.println(GkaUtils.toNodesString(shortestPath));
		// System.out.println(AlgoFloydWarshall.distance(graph, "0", "99"));
		//
		// APSP apsp = new APSP(graph, "weight", true);
		// apsp.compute();
		// APSPInfo info =
		// graph.getNode(graph.createNode("0")).getAttribute(APSPInfo.ATTRIBUTE_NAME);
		// System.out.println(info.getLengthTo(graph.createNode("99")));

	}

	/**
	 * Array of shortest distances between each pair of nodes.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return a table that contains the shortest distance between two nodes.
	 */
	public static double[][] distanceMatrix(GkaGraph graph) {
		return algorithm(graph).distance;
	}

	/**
	 * Array of transit node for each pair of nodes.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return a table that indicates for each pair of nodes s and t the node with
	 *         the highest index on the shortest path from s to t.
	 */
	public static int[][] transitMatrix(GkaGraph graph) {
		return algorithm(graph).transit;
	}

	/**
	 * Calculate the shortest distance between two nodes.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param endNodeName
	 *            Name of the end node.
	 * @return The length of the shortest path between two nodes.
	 */
	public static double distance(GkaGraph graph, String startNodeName, String endNodeName) {
		int startIndex = graph.getNode(graph.createNode(startNodeName)).getIndex();
		int endIndex = graph.getNode(graph.createNode(endNodeName)).getIndex();
		return distanceMatrix(graph)[startIndex][endIndex];
	}

	/**
	 * Find the shortest path between 2 nodes using Floyd-Warshall.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param endNodeName
	 *            Name of the end node.
	 * @param isVisualized
	 *            Whether the graph should be visualized.
	 * @return Sequence of nodes on the shortest path between start and end nodes.
	 */
	public static List<Node> shortestPath(GkaGraph graph, String startNodeName, String endNodeName,
			boolean isVisualized) {
		// check if nodes exist
		if (!graph.getNodeNames().contains(startNodeName) || !graph.getNodeNames().contains(endNodeName)) {
			throw new IllegalArgumentException("Node not found in graph.");
		}

		List<Node> out = new LinkedList<>();
		for (Node node : extractPath(graph, startNodeName, endNodeName)) {
			out.add(node);
		}
		Node end = graph.getNode(graph.createNode(endNodeName));
		out.add(end);

		
//		// create a HashMap between each node and its previous node on the shortest path
//		Map<Node, Node> prevNodes = new HashMap<>();
//
//		Node curr = end;
//		while (transitMatrix(graph)[startIndex][curr.getIndex()] > 0) {
//			// the node with the highest index on the shortest path from start node to
//			// current node (isn't necessarily adjacent to current node)
//			int trIndex = transitMatrix(graph)[startIndex][curr.getIndex()];
//
//			// in case the nearest node isn't adjacent to current node
//			while (!graph.getNode(trIndex).hasEdgeToward(curr.getIndex())) {
//				// continue to check through the transit nodes between the current transit node
//				// and current end node until the adjacent node is found
//				trIndex = transitMatrix(graph)[trIndex][curr.getIndex()];
//			}
//			prevNodes.put(curr, graph.getNode(trIndex));
//			curr = prevNodes.get(curr);
//		}
//		prevNodes.put(curr, start);
//
//		// trace back the path with HashMap
//		List<Node> out = new LinkedList<>();
//		curr = end;
//		while (curr != null) {
//			out.add(0, curr);
//			curr = prevNodes.get(curr);
//		}
//
		// visualize the result
		if (isVisualized) {
			graph.display();
			graph.beautify();
			clearMarks(graph);
			out.get(0).addAttribute("ui.class", "special");

			for (int i = 1; i < out.size(); i++) {
				out.get(i).addAttribute("ui.class", "marked");
				out.get(i - 1).getEdgeBetween(out.get(i)).addAttribute("ui.class", "marked");
			}

			out.get(out.size() - 1).addAttribute("ui.class", "special");
		}

		return out;
	}
	
	public static List<Node> extractPath(GkaGraph graph, String startNodeName, String endNodeName) {
		// get start and end nodes
		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		// get index of start node in the graph
		int startIndex = start.getIndex();
		int endIndex = end.getIndex();

		List<Node> path = new LinkedList<>();

		if (transitMatrix(graph)[startIndex][endIndex] == -1) {
			if (start.hasEdgeToward(end)) {
				path.add(start);
				
			} else {
				// no path found
				return new LinkedList<Node>();
			}
		} else {
			String transitName = graph.getNode(transitMatrix(graph)[startIndex][endIndex]).getAttribute("name");

			path.addAll(extractPath(graph, startNodeName, transitName));
			path.addAll(extractPath(graph, transitName, endNodeName));

		}
		
		

		return path;
	}

	/**
	 * Implementation of Floyd-Warshall algorithm.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return 1. a distance matrix to calculate the shortest distance between any
	 *         two nodes of the graph <br>
	 *         2. a transit matrix to help reconstruct the shortest path between any
	 *         two nodes of the graph
	 */
	public static Result algorithm(GkaGraph graph) {
		int nodeNr = graph.getNodeCount();
		// double[][][] result = new double[2][nodeNr][nodeNr];
		// double[][] distance = result[0];
		// double[][] transit = result[1];

		Result result = new Result(nodeNr);
		double[][] distance = result.distance;
		int[][] transit = result.transit;

		// initiate distance and transit
		for (int i = 0; i < nodeNr; i++) {
			for (int j = 0; j < nodeNr; j++) {
				if (i == j) {
					distance[i][j] = 0.0;
				} else {
					distance[i][j] = graph.getShortestDist(graph.getNode(i), graph.getNode(j));
				}

				transit[i][j] = -1;
			}
		}

		for (int j = 0; j < nodeNr; j++) {
			for (int i = 0; i < nodeNr; i++) {
				for (int k = 0; k < nodeNr; k++) {
					// If node j is on the shortest path from i to k,
					// then update the value of distance[i][k] and set j the transit node
					double sum = distance[i][j] + distance[j][k];
					if (i != j && j != k && sum < distance[i][k]) {
						distance[i][k] = sum;
						transit[i][k] = j;
					}

					// If distance of any node from itself becomes negative,
					// there is a negative weight cycle. Stop the algorithm.
					if (distance[i][i] < 0) {
						// ?
						return null;
					}
				}
			}
		}

		return result;
	}

	/**
	 * Clear all previous marks on the graph made by visualized methods.
	 * 
	 * @param graph
	 *            A graph to work with.
	 */
	protected static void clearMarks(GkaGraph graph) {
		for (Node node : graph) {
			node.setAttribute("ui.class", "unmarked");
		}
		for (Edge edge : graph.getEachEdge()) {
			edge.setAttribute("ui.class", "unmarked");
		}
	}
}
