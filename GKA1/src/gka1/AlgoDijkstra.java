package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Currency;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.graphstream.algorithm.AStar.DistanceCosts;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

import com.sun.javafx.scene.traversal.WeightedClosestCorner;
import com.sun.org.apache.xalan.internal.xsltc.compiler.util.CompareGenerator;

import scala.annotation.meta.getter;
import scala.collection.mutable.UnrolledBuffer;
import sun.java2d.pipe.AlphaColorPipe;

public class AlgoDijkstra {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("VLtest.gka");
		List<Node> shortestPath = AlgoDijkstra.shortestPath(graph, "1", "6", false);
		graph.display();
		graph.beautify();
	}

	public static List<Node> shortestPath(GkaGraph graph, String startNodeName, String endNodeName,
			boolean isVisualized) {
		// check if nodes exist
		if (!graph.getNodeNames().contains(startNodeName) || !graph.getNodeNames().contains(endNodeName)) {
			throw new IllegalArgumentException("Node not found in graph.");
		}

		// create a node weight comparator
		Comparator<Node> distanceToStartNodeComparator = (n1, n2) -> {
			return Integer.valueOf(n1.getAttribute("distance").toString()) - Integer.valueOf(n2.getAttribute("distance").toString());
		};

		Map<Node, Double> totalCosts = new HashMap<>();
		Map<Node, Node> prevNodes = new HashMap<>();
		PriorityQueue<Node> minPQ = new PriorityQueue<>(distanceToStartNodeComparator);
		List<Node> visited = new ArrayList<>();

		Node start = graph.getNode(graph.createNode(startNodeName));
		Node end = graph.getNode(graph.createNode(endNodeName));

		totalCosts.put(start, 0.0);
		start.addAttribute("distance", totalCosts.get(start));
		minPQ.add(start);

		prevNodes.put(start, null);

		for (Node node : graph) {
			if (node != start) {
				totalCosts.put(node, Double.POSITIVE_INFINITY);
				node.addAttribute("distance", Double.POSITIVE_INFINITY);
			}
		}

		while (!minPQ.isEmpty()) {
			Node newSmallest = minPQ.remove();
			visited.add(newSmallest);

			Iterator<Node> neighbors = newSmallest.getNeighborNodeIterator();
			while (neighbors.hasNext()) {
				Node n = neighbors.next();
				if (!visited.contains(n) && newSmallest.hasEdgeToward(n)) {
					double distance = graph.getShortestDist(newSmallest, n);
					double altPath = totalCosts.get(newSmallest) + distance;

					if (altPath < totalCosts.get(n)) {
						totalCosts.put(n, altPath);
						prevNodes.put(n, newSmallest);

						if (!minPQ.contains(n)) {
							minPQ.remove(n);
						}

						n.addAttribute("distance", altPath);
						minPQ.add(n);
					}
				}
			}
		}

		return null;
	}

}
