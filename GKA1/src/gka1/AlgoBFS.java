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
 * Contain methods to traverse and find shortest path using BFS.
 * 
 * @author Tri Pham
 *
 */
public class AlgoBFS {

	/**
	 * Print out BFS traversal starting from a given node. <br>
	 * Algorithm: <br>
	 * 1. Mark the start node and add it to queue. <br>
	 * 2. Dequeue the first node in the queue and print it out. <br>
	 * 3. Iterate through its adjacent nodes, if the node isn't marked, mark and
	 * enqueue it. <br>
	 * 4. If the queue is empty, the algorithm ends, else go to step 2. <br>
	 * 
	 * @param graph
	 *            A graph to work with
	 * @param startNodeName
	 *            Name of the start node
	 * @return The sequence of nodes during BFS
	 */
	public static List<Node> traverse(GkaGraph graph, String startNodeName, boolean isVisualized) {
		// check if node exists
		if (!graph.getNodeNames().contains(startNodeName)) {
			return null;
		}
		
		// get start node
		Node start = graph.getNode(graph.createNode(startNodeName));
		
		// create a list for visited nodes
		List<Node> visited = new ArrayList<>();
		
		// create a queue for nodes to visit 
		Queue<Node> toVisit = new LinkedList<Node>();

		// enqueue start node
		toVisit.add(start);	
		visited.add(start);

		while (!toVisit.isEmpty()) {
			// dequeue visiting node
			Node curr = toVisit.remove();

			// enqueue adjacent nodes 
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
	 * Print out a path with the least edges connecting 2 nodes. <br>
	 * Algorithm: <br>
	 * 1. Mark t with distance(s,t) = i and enqueue t. <br>
	 * 2. Find a adjacent node of the last enqueued node (n) that has distance =
	 * dist(n)-1 and enqueue it. <br>
	 * 3. If a neighbor of s is reached, go to step 4. If not, go to step 2.
	 * <br>
	 * 4. Enqueue s and end the algorithm, print out the queue in reversed order
	 * to see the path.
	 * 
	 * @param graph
	 *            The graph to work with.
	 * @param startNodeName
	 *            Name of the start node.
	 * @param endNodeName
	 *            Name of the end node.
	 * @return Sequence of nodes on the shortest path between start and end
	 */
	public static List<Node> shortestPath(GkaGraph graph, String startNodeName, String endNodeName, boolean isVisualized) {
		// check if nodes exist
		if (!graph.getNodeNames().contains(startNodeName) || !graph.getNodeNames().contains(endNodeName)) {
			return null;
		}
		
		// get start and end nodes
		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		// create a queue for nodes to visit 
		Queue<Node> toVisit = new LinkedList<>();
		
		// create a HashMap between each node and its parent 
		HashMap<Node, Node> parents = new HashMap<>();

		// enqueue start node 
		toVisit.add(start);
		parents.put(start, null);

		while (!toVisit.isEmpty()) {
			// dequeue visiting node 
			Node curr = toVisit.remove();

			// stop if end is found 
			if (curr == end) {
				break;
			}

			// enqueue adjacent nodes 
			Iterator<Node> neighbors = curr.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node n = neighbors.next();
				if (!parents.containsKey(n) && curr.hasEdgeToward(n)) {
					toVisit.add(n);
					parents.put(n, curr);
				}			
			}
		}

		// return null if end is not found 
		if (parents.get(end) == null) {
			return null;
		}
		
		// trace back the path with HashMap 
		List<Node> out = new LinkedList<>();
		Node curr = end;
		while (curr != null) {
			out.add(0, curr);
			curr = parents.get(curr);
		}
		
		// visualize the result
		if (isVisualized) {
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
	 *            A graph to work with
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
