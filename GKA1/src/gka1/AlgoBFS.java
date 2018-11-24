package gka1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

/**
 * Contain methods to traverse and to find shortest path using BFS.
 * 
 * @author Tri Pham
 *
 */
public class AlgoBFS {
	/**
	 * Get the sequence of node in BFS-traversal. <br>
	 * Algorithm: <br>
	 * 1. Enqueue start node and mark it as visited.<br>
	 * 2. Dequeue a node in the queue until queue is empty. <br>
	 * 4. Enqueue its unvisited adjacent nodes and mark them as visited. <br>
	 * 5. Go to step 2. <br>
	 * 
	 * @param graph
	 *            A graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param isVisualized
	 *            Whether graph should be visualized.
	 * @return The sequence of nodes during BFS.
	 */
	public static List<Node> traverse(GkaGraph graph, String startNodeName, boolean isVisualized) {
		// check if node exists
		if (!graph.getNodeNames().contains(startNodeName)) {
			throw new IllegalArgumentException("Node not found in graph.");
		}

		// get start node
		Node start = graph.getNode(graph.createNode(startNodeName));

		// create a list for visited nodes
		List<Node> visited = new ArrayList<>();

		// create a queue for nodes to visit
		Queue<Node> toVisit = new LinkedList<>();

		// enqueue start node
		toVisit.add(start);
		visited.add(start);

		while (!toVisit.isEmpty()) {
			// dequeue visiting node
			Node curr = toVisit.remove();

			// enqueue unvisited adjacent nodes
			Iterator<Node> neighbors = curr.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node n = neighbors.next();
				if (!visited.contains(n) && curr.hasEdgeToward(n)) {
					toVisit.add(n);
					visited.add(n);
				}
			}
		}

		// visualize the result
		if (isVisualized) {
			graph.display();
			graph.beautify();
			clearMarks(graph);

			for (Node node : visited) {
				sleep();
				node.setAttribute("ui.class", "marked");
				sleep();
			}
		}

		return visited;
	}

	/**
	 * Find the shortest path between 2 given nodes in graph. <br>
	 * Algorithm: <br>
	 * 1. Enqueue start node and add it to a HashMap between each node.
	 * and its parent. <br>
	 * 2. Dequeue a node in the queue until queue is empty. <br>
	 * 3. Go to step 7 if queue is empty. <br>
	 * 4. Stop if end node is found. <br>
	 * 5. Enqueue its adjacent nodes that is not in the HashMap and add them to HashMap. <br>
	 * 6. Go to step 2. <br>
	 * 7. Stop as end node can't be found. 
	 * 8. Trace back the path. 
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

		// create a queue for nodes to visit
		Queue<Node> toVisit = new LinkedList<>();

		// create a HashMap between each node and its previous node
		HashMap<Node, Node> prevNodes = new HashMap<>();

		// enqueue start node
		toVisit.add(start);
		prevNodes.put(start, null);

		while (!toVisit.isEmpty()) {
			// dequeue visiting node
			Node curr = toVisit.remove();

			// stop if end node is found
			if (curr.equals(end)) {
				break;
			}

			// enqueue unvisited adjacent nodes
			Iterator<Node> neighbors = curr.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node n = neighbors.next();
				if (!prevNodes.containsKey(n) && curr.hasEdgeToward(n)) {
					toVisit.add(n);
					prevNodes.put(n, curr);
				}
			}
		}

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

	/**
	 * Pause during traversal between each node.
	 */
	protected static void sleep() {
		try {
			Thread.sleep(800);
		} catch (Exception e) {
		}
	}

}
