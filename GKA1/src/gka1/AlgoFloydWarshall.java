package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Contain methods to find the shortest path using Floyd-Warshall.
 * 
 * @author Tri Pham
 *
 */
public class AlgoFloydWarshall {

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("VLtest.gka");
		List<Node> shortestPath = AlgoFloydWarshall.shortestPath(graph, "1", "6", true);
		System.out.println(GkaUtils.toNodesString(shortestPath));
		System.out.println(AlgoFloydWarshall.distance(graph, "1", "6"));

	}

	/**
	 * Array of distances
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return a table that contains the shortest distance between two nodes.
	 */
	public static int[][] distanceArray(GkaGraph graph) {
		return algo(graph)[0];
	}

	/**
	 * Array of transit
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return a table that indicates for each pair of nodes s and t the node that
	 *         on the shortest path from s to t and has the shortest distance to t.
	 */
	public static int[][] transitArray(GkaGraph graph) {
		return algo(graph)[1];
	}

	/**
	 * Calculate the shortest distance between two nodes
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param endNodeName
	 *            Name of the end node.
	 * @return The length of the shortest path between two nodes.
	 */
	public static int distance(GkaGraph graph, String startNodeName, String endNodeName) {
		int startIndex = graph.getNode(graph.createNode(startNodeName)).getIndex();
		int endIndex = graph.getNode(graph.createNode(endNodeName)).getIndex();
		return distanceArray(graph)[startIndex][endIndex];
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

		// get start and end nodes
		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		// get index of start node in the graph
		int startIndex = start.getIndex();

		// create a HashMap between each node and its previous node on the shortest path
		Map<Node, Node> prevNodes = new HashMap<>();

		Node curr = end;
		while (transitArray(graph)[startIndex][curr.getIndex()] > 0) {
			// (index of) the nearest node to current node (isn't necessarily adjacent to
			// current node)
			int trIndex = transitArray(graph)[startIndex][curr.getIndex()];

			// in case the nearest node isn't adjacent to current node
			while (!graph.getNode(trIndex).hasEdgeToward(curr.getIndex())) {
				// continue to check through the transit nodes between the current transit node
				// and current end node until the adjacent node is found
				trIndex = transitArray(graph)[trIndex][curr.getIndex()];
			}
			prevNodes.put(curr, graph.getNode(trIndex));
			curr = prevNodes.get(curr);
		}
		prevNodes.put(curr, start);

		// trace back the path with HashMap
		List<Node> out = new LinkedList<>();
		curr = end;
		while (curr != null) {
			out.add(0, curr);
			curr = prevNodes.get(curr);
		}

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

	/**
	 * Mechanism of Floyd-Warshall
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @return 1. a distance matrix to calculate the shortest distance between any
	 *         two nodes of the graph <br>
	 *         2. a transit matrix to help reconstruct the shortest path between any
	 *         two nodes of the graph
	 */
	public static int[][][] algo(GkaGraph graph) {
		int nodeNr = graph.getNodeCount();
		int[][][] result = new int[2][nodeNr][nodeNr];
		int[][] distance = result[0];
		int[][] transit = result[1];

		for (int i = 0; i < nodeNr; i++) {
			for (int j = 0; j < nodeNr; j++) {
				if (i == j) {
					distance[i][j] = 0;
				} else if (graph.getNode(i).hasEdgeToward(j)) {
					Edge edge = graph.getNode(i).getEdgeToward(j);
					distance[i][j] = (int) edge.getAttribute("weight");
				} else {
					distance[i][j] = 99999;
				}

				transit[i][j] = -1;
			}
		}

		for (int j = 0; j < nodeNr; j++) {
			for (int i = 0; i < nodeNr; i++) {
				for (int k = 0; k < nodeNr; k++) {
					if (distance[i][j] + distance[j][k] < distance[i][k]) {
						distance[i][k] = distance[i][j] + distance[j][k];
						transit[i][k] = j;
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
