package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Contain methods to find the shortest path using Dijkstra.
 * 
 * @author Huy Tran PC
 *
 */
public class AlgoDijkstra {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
//		GkaGraph graph = GkaUtils.read("graph03.gka");
//		List<Node> shortestPath = AlgoDijkstra.shortestPath(graph, "Lübeck", "Husum", true);
//		System.out.println(GkaUtils.toNodesString(shortestPath));
		
		GkaGraph graph = GkaUtils.generateRandom(100, 3500, true, false, 1, 10);
		List<Node> shortestPath = AlgoDijkstra.shortestPath(graph, "0", "99", true);
		System.out.println(GkaUtils.toNodesString(shortestPath));
	}

	/**
	 * Find the shortest path between 2 nodes using Dijkstra.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param endNodeName
	 *            Name of the end node.
	 * @param isVisualized
	 *            Whether the graph should be visualized.
	 * @return Sequence of nodes on the shortest path between start and end
	 *         nodes.
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

		// create a HashMap between each node and the total cost to reach it
		Map<Node, Double> totalCosts = new HashMap<>();

		// create a HashMap between each node and its previous node
		Map<Node, Node> prevNodes = new HashMap<>();

		// create comparator for cost to start node
		Comparator<Node> costToStartComparator = (n1, n2) -> {
			return (int) (Double.valueOf(n1.getAttribute("cost").toString())
					- Double.valueOf(n2.getAttribute("cost").toString()));
		};

		// create a min priority queue ordered by the cost to start node
		PriorityQueue<Node> minPQ = new PriorityQueue<>(costToStartComparator);

		// create a list for visited nodes
		Set<Node> visited = new HashSet<>();

		// set start node's initial values
		start.addAttribute("cost", 0.0);
		prevNodes.put(start, null);
		totalCosts.put(start, 0.0);
		minPQ.add(start);

		// set the other nodes' initial values
		for (Node node : graph) {
			if (node != start) {
				node.addAttribute("cost", Double.POSITIVE_INFINITY);
				totalCosts.put(node, Double.POSITIVE_INFINITY);
			}
		}

		while (!minPQ.isEmpty()) {
			// dequeue the node with smallest cost to start
			Node curr = minPQ.remove();

			// stop if end is found
			if (curr.equals(end)) {
				break;
			}

			// mark it as visited
			visited.add(curr);

			// iterate through unvisited adjacent nodes
			Iterator<Node> neighbors = curr.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node n = neighbors.next();
				if (!visited.contains(n) && curr.hasEdgeToward(n)) {
					// get cost between current node and its neighbor
					double distance = graph.getShortestDist(curr, n);

					// get cost between start and its neighbor
					double totalCost = totalCosts.get(curr) + distance;

					// update total cost and previous node if path is better
					if (totalCost < totalCosts.get(n)) {
						// update cost in min priority queue
						if (minPQ.contains(n)) {
							minPQ.remove(n);
						}
						n.addAttribute("cost", totalCost);
						minPQ.add(n);

						totalCosts.put(n, totalCost);
						prevNodes.put(n, curr);
					}
				}
			}
		}

		// print totalCosts und prevNodes in console
		// for (Map.Entry<Node, Double> entry : totalCosts.entrySet()) {
		// Node key = entry.getKey();
		// double value = entry.getValue();
		// System.out.println("Key: " + key.getAttribute("name") + "; " +
		// "Value: " + value);
		// }
		//
		// System.out.println();
		// for (Map.Entry<Node, Node> entry : prevNodes.entrySet()) {
		// Node key = entry.getKey();
		// Node value = entry.getValue();
		// if (value != null) {
		// System.out.println("Key: " + key.getAttribute("name") + "; " +
		// "Value: " + value.getAttribute("name"));
		// }
		// }

		// return null if end is not found
		if (prevNodes.get(end) == null) {
			return null;
		}

		// trace back the path with HashMap
		List<Node> out = new LinkedList<>();
		Node curr = end;
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