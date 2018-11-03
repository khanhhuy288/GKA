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
	 * Print out and visualize BFS traversal starting from a given node <br>
	 * Algorithm: <br>
	 * 1. Mark the starting node and add it to queue <br>
	 * 2. Dequeue the visiting node <br>
	 * 3. Iterate through its adjacent nodes, if it isn't marked, mark it and add it
	 * to queue <br>
	 * 4. If the queue is empty, the algorithm ends, else dequeue the first node in
	 * the queue and go to step 3 <br>
	 * 
	 * @param g
	 * @param nodeName
	 */
	public static void traverse(GkaGraph g, String nodeName) {
		Node n = g.getNode(g.createNode(nodeName));

		// Reset all marks
		for (Node node : g) {
			node.setAttribute("ui.class", "unmarked");
		}
		for (Edge edge : g.getEachEdge()) {
			edge.setAttribute("ui.class", "unmarked");
		}

		// Create a queue for BFS
		LinkedList<Node> queue = new LinkedList<Node>();

		// Create a list to store the order of nodes for visualizing
		LinkedList<Node> res = new LinkedList<Node>();

		// Mark the current node as visited and enqueue it
		n.setAttribute("class", "marked");
		queue.add(n);
		res.add(n);

		while (queue.size() != 0) {
			// Dequeue visited node and print it
			n = queue.poll();
			System.out.print(n.getAttribute("name") + " ");

			// Iterate through all adjacent nodes
			// If a adjacent has not been visited, then mark it visited and enqueue it
			Iterator<Node> neighbors = n.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node next = neighbors.next();
				if (next.getAttribute("class") != "marked") {
					next.setAttribute("class", "marked");
					queue.add(next);
					res.add(next);
				}
			}
		}

		System.out.println();

		// visualize traversal
		for (Node node : res) {
			sleepTr();
			node.setAttribute("ui.class", "marked");
			sleepTr();
		}
	}

	/**
	 * Calculate the shortest distance between 2 nodes in graph using BFS <br>
	 * Algorithm: <br>
	 * 1. Set dist(s) = i = 0 <br>
	 * 2. Iterate through all adjacent nodes of visiting node (dist = i). If the
	 * node is unvisited, set dist = i+1 <br>
	 * 3. If t is reached, the result is i+1, the algorithm ends. If not, increase i
	 * by 1 and go to step 2. <br>
	 * If the loop ends before t is reached, return -1
	 * 
	 * @param g
	 *            the graph
	 * @param startName
	 *            name of start node
	 * @param endName
	 *            name of end node
	 * @return the least number of edges between s and t (default = -1, 2 nodes
	 *         aren't connected)
	 */
	public static int distance(GkaGraph g, String startName, String endName) {
		Node s = g.getNode(g.createNode(startName));
		Node t = g.getNode(g.createNode(endName));
		int result = -1;

		// Create a queue for BFS and enqueue s
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.add(s);

		// set distance for s is 0, others are -1 (unvisited)
		for (Node n : g) {
			n.setAttribute("dist", -1);
		}
		s.setAttribute("dist", 0);

		while (!queue.isEmpty()) {
			// pop visited node
			Node node = queue.poll();

			// iterate through all adjacent nodes
			Iterator<Node> neighbors = node.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node next = neighbors.next();

				if ((int) next.getAttribute("dist") == -1) {
					// set new distance to s
					next.setAttribute("dist", (int) node.getAttribute("dist") + 1);

					// add to queue for the next examination
					queue.add(next);
				}

				// the algorithm ends if t is reached
				if (next == t) {
					result = (int) next.getAttribute("dist");
					// break;
				}
			}
		}

		return result;

	}

	/**
	 * Print out and visualize a path with the least edges between 2 nodes <br>
	 * Algorithm: <br>
	 * 1. Mark t with distance(s,t) = i <br>
	 * 2. Find and mark a adjacent node of the current visiting node that has
	 * distance = i-1 <br>
	 * 3. If s is reached, end the algorithm. If not, decrease i by 1 and go to step
	 * 2
	 * 
	 * @param g
	 *            the graph
	 * @param startName
	 *            name of start node
	 * @param endName
	 *            name of end node
	 */
	public static void shortestPath(GkaGraph g, String startName, String endName) {
		Node s = g.getNode(g.createNode(startName));
		Node t = g.getNode(g.createNode(endName));

		// Reset all marks
		for (Node node : g) {
			node.setAttribute("ui.class", "unmarked");
		}
		for (Edge edge : g.getEachEdge()) {
			edge.setAttribute("ui.class", "unmarked");
		}

		// distance between s and t
		int distance = distance(g, startName, endName);

		// check if s and t are connected
		if (distance < 0) {
			System.out.printf("%s and %s aren't connected", startName, endName);
		} else {
			// result stack
			Stack<Node> result = new Stack<Node>();
			result.add(t);

			// Create a queue for BFS and enqueue t
			LinkedList<Node> queue = new LinkedList<Node>();
			queue.add(t);

			t.setAttribute("dist", distance);

			while (!queue.isEmpty()) {
				// pop visited node
				Node node = queue.poll();

				// the algorithm ends if s is reached
				if (node == s) {
					break;
				}

				// iterate through adjacent nodes to find a 'parent' node that lies on a
				// shortest path
				Iterator<Node> neighbors = node.getNeighborNodeIterator();
				while (neighbors.hasNext()) {
					Node next = neighbors.next();

					// wanted distance to start node
					int wantedDist = (int) node.getAttribute("dist") - 1;

					// found a suitable node
					if ((int) next.getAttribute("dist") == wantedDist) {
						// add to queue for the next examination
						queue.add(next);

						// add wanted 'parent' node to the stack
						result.add(next);

						break;
					}

				}
			}

			// print out result
			System.out.print("A shortest path between " + startName + " und " + endName + ": ");

			Node[] resArr = new Node[result.size()];
			for (int i = 0; i < resArr.length; i++) {
				resArr[i] = result.pop();
				System.out.print(resArr[i].getAttribute("name") + " ");
			}
			System.out.println();

			// visualize shortest path
			resArr[0].addAttribute("ui.class", "marked");
			resArr[resArr.length - 1].addAttribute("ui.class", "marked");
			sleep();
			for (int i = 1; i < resArr.length; i++) {
				sleep();
				resArr[i].addAttribute("ui.class", "marked");
				resArr[i - 1].getEdgeBetween(resArr[i]).addAttribute("ui.class", "marked");
			}
		}
	}

	protected static void sleepTr() {
		try {
			Thread.sleep(600);
		} catch (Exception e) {
		}
	}
	
	protected static void sleep() {
		try {
			Thread.sleep(1000);
		} catch (Exception e) {
		}
	}
}
