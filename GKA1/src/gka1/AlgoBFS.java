package gka1;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

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
	 * @param nodeName
	 *            Name of the start node
	 */
	public static LinkedList<Node> traverse(GkaGraph graph, String nodeName) {
		// create a queue for visited nodes
		LinkedList<Node> queue = new LinkedList<Node>();

		// create a list for result
		LinkedList<Node> traverseList = new LinkedList<Node>();

		// get start node
		Node start = graph.getNode(graph.createNode(nodeName));

		// mark the start node as visited and enqueue it
		start.setAttribute("visited", "yes");
		queue.add(start);

		while (queue.size() != 0) {
			// dequeue visiting node, print it and save it to result
			Node currentNode = queue.poll();
			System.out.print(currentNode.getAttribute("name") + " ");
			traverseList.add(currentNode);

			// iterate through all adjacent nodes
			// mark unvisited nodes as visited and enqueue them
			Iterator<Node> neighbors = currentNode.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node node = neighbors.next();
				if (node.getAttribute("visited") != "yes" && currentNode.getEdgeToward(node) != null) {
					node.setAttribute("visited", "yes");
					queue.add(node);
				}
			}
		}

		System.out.println();

		return traverseList;
	}

	/**
	 * Calculate the shortest distance between 2 nodes in graph using BFS. <br>
	 * Algorithm: <br>
	 * 1. Set dist(s) = 0 and enqueue s. <br>
	 * 2. Dequeue the first node in the queue. Iterate through its adjacent nodes.
	 * If the node is unvisited, set dist = dist(currentNode) + 1. <br>
	 * 3. If t is reached, the result is dist(currentNode) + 1, the algorithm ends.
	 * If not, go to step 2. <br>
	 * If the loop ends before t is reached, return -1.
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param startNodeName
	 *            Name of start node
	 * @param endNodeName
	 *            Name of end node
	 * @return The smallest number of edges needed to connect s and t. <br>
	 *         Return -1 if 2 nodes aren't connected.
	 */
	public static int distance(GkaGraph graph, String startNodeName, String endNodeName) {
		// get start and end nodes
		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		// set default
		int result = -1;

		// create a queue for BFS and enqueue start node
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(start);

		// set distance for start node to 0, others to -1 (unvisited)
		for (Node node : graph) {
			node.setAttribute("dist", -1);
		}
		start.setAttribute("dist", 0);

		while (!queue.isEmpty()) {
			// dequeue visiting node
			Node currentNode = queue.poll();
			
			if (result >= 0) {
				break;
			}

			// iterate through all adjacent nodes
			Iterator<Node> neighbors = currentNode.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node node = neighbors.next();

				// set new distance to unvisited node and enqueue it
				if ((int) node.getAttribute("dist") == -1 && currentNode.getEdgeToward(node) != null) {
					node.setAttribute("dist", (int) currentNode.getAttribute("dist") + 1);
					queue.add(node);
				}

				// stop if end node is found
				if (node == end) {
					result = (int) node.getAttribute("dist");
					break;
				}
			}
		}

		return result;

	}

	/**
	 * Print out a path with the least edges connecting 2 nodes. <br>
	 * Algorithm: <br>
	 * 1. Mark t with distance(s,t) = i and enqueue t. <br>
	 * 2. Find a adjacent node of the last enqueued node (n) that has distance =
	 * dist(n)-1 and enqueue it. <br>
	 * 3. If a neighbor of s is reached, go to step 4. If not, go to step 2. <br>
	 * 4. Enqueue s and end the algorithm, print out the queue in reversed order to
	 * see the path.
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param startNodeName
	 *            Name of the start node
	 * @param endNodeName
	 *            Name of the end node
	 */
	public static Node[] shortestPath(GkaGraph graph, String startNodeName, String endNodeName) {
		// get start and end nodes
		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		// get distance between start and end nodes
		int distance = distance(graph, startNodeName, endNodeName);

		// find shortest path only if distance >= 0
		if (distance < 0) {
			System.out.printf("%s and %s aren't connected", startNodeName, endNodeName);
			return null;
		} else {
			// create a stack for result and enqueue end node
			Stack<Node> result = new Stack<Node>();
			result.add(end);

			// create a queue for BFS and enqueue end node
			LinkedList<Node> queue = new LinkedList<Node>();
			queue.add(end);

			end.setAttribute("dist", distance);

			while (!queue.isEmpty()) {
				// dequeue visiting node
				Node currentNode = queue.poll();

				int currentDist = (int) currentNode.getAttribute("dist");

				// stop if a neighbor of start is found
				if (currentDist == 1) {
					break;
				}

				// iterate through adjacent nodes to find a parent node that lies on the
				// shortest path
				Iterator<Node> neighbors = currentNode.getNeighborNodeIterator();
				while (neighbors.hasNext()) {
					Node node = neighbors.next();

					// find one parent node that has the wanted distance to start node and
					// enqueue it
					if ((int) node.getAttribute("dist") == currentDist - 1 && node.getEdgeToward(currentNode) != null) {
						queue.add(node);
						result.add(node);
						break;
					}

				}
			}
			result.add(start);

			// print out result
			System.out.print("A shortest path between " + startNodeName + " und " + endNodeName + ": ");

			Node[] pathArray = new Node[result.size()];
			for (int i = 0; i < pathArray.length; i++) {
				pathArray[i] = result.pop();
				System.out.print(pathArray[i].getAttribute("name") + " ");
			}
			System.out.println();

			return pathArray;
		}
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
	 * Print out and visualize BFS traversal starting from a given node.
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param nodeName
	 *            Name of the start node
	 */
	public static void visualizeTraversal(GkaGraph graph, String nodeName) {
		clearMarks(graph);
		LinkedList<Node> traverseList = traverse(graph, nodeName);
		for (Node node : traverseList) {
			sleepTr();
			node.setAttribute("ui.class", "marked");
			sleepTr();
		}
	}

	/**
	 * Print out and visualize a path with the least edges connecting 2 nodes.
	 * 
	 * @param graph
	 *            The graph to work with
	 * @param startNodeName
	 *            Name of the start node
	 * @param endNodeName
	 *            Name of the end node
	 */
	public static void visualizeShortestPath(GkaGraph graph, String startNodeName, String endNodeName) {
		clearMarks(graph);
		Node[] pathArray = shortestPath(graph, startNodeName, endNodeName);
		pathArray[0].addAttribute("ui.class", "marked");
		pathArray[pathArray.length - 1].addAttribute("ui.class", "marked");
		sleepSP();
		for (int i = 1; i < pathArray.length; i++) {
			sleepSP();
			pathArray[i].addAttribute("ui.class", "marked");
			pathArray[i - 1].getEdgeBetween(pathArray[i]).addAttribute("ui.class", "marked");
		}
	}

	/**
	 * Pause during traversal between each node.
	 */
	protected static void sleepTr() {
		try {
			Thread.sleep(800);
		} catch (Exception e) {
		}
	}

	/**
	 * Pause during finding shortest path.
	 */
	protected static void sleepSP() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
}
