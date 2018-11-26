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

public class AlgoFloydWarshall {

	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("VLtest.gka");
		List<Node> shortestPath = AlgoFloydWarshall.shortestPath(graph, "1", "6");
		System.out.println(GkaUtils.toNodesString(shortestPath));

	}

	public static int[][] distanceArray(GkaGraph graph) {
		return algo(graph)[0];
	}

	public static int[][] transitArray(GkaGraph graph) {
		return algo(graph)[1];
	}

	public static int distance(GkaGraph graph, String startNodeName, String endNodeName) {
		int startIndex = graph.getNode(graph.createNode(startNodeName)).getIndex();
		int endIndex = graph.getNode(graph.createNode(endNodeName)).getIndex();
		return distanceArray(graph)[startIndex][endIndex];
	}

	public static List<Node> shortestPath(GkaGraph graph, String startNodeName, String endNodeName) {
		// check if nodes exist
		if (!graph.getNodeNames().contains(startNodeName) || !graph.getNodeNames().contains(endNodeName)) {
			throw new IllegalArgumentException("Node not found in graph.");
		}

		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));
		int startIndex = start.getIndex();
		int endIndex = end.getIndex();
		// int transitIndex = transitArray(graph)[startIndex][endIndex];

		// create a HashMap between each node and its previous node
		Map<Node, Node> prevNodes = new HashMap<>();

		Node curr = end;
		while (transitArray(graph)[startIndex][curr.getIndex()] > 0) {
			int trIndex = transitArray(graph)[startIndex][curr.getIndex()];
			while (!graph.getNode(trIndex).hasEdgeToward(curr.getIndex())) {
				int tr = transitArray(graph)[trIndex][curr.getIndex()];
				trIndex = tr;
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

		return out;
	}

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

}
