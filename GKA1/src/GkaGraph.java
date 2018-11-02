import java.util.HashMap;
import java.util.UUID;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * GkaGraph has useful functions for working with graph's properties.
 * 
 * @author Huy Tran PC
 *
 */
public class GkaGraph extends MultiGraph {
	// hash map with node names as keys and node ids as values
	private HashMap<String, String> nodeNameToIdMap;

	/**
	 * Constructor for a GkaGraph.
	 * 
	 * @param id
	 *            Id of the graph
	 */
	public GkaGraph(String id) {
		super(id);
		this.nodeNameToIdMap = new HashMap<String, String>();
	}

	/**
	 * Get the hash map with node names as keys and node ids as values.
	 * 
	 * @return The hash map
	 */
	public HashMap<String, String> getNodeNameToIdMap() {
		return nodeNameToIdMap;
	}

	/**
	 * Get the id of a node from the hash map. <br>
	 * If name is new, create a new node.
	 * 
	 * @param nodeName
	 *            Name of a node
	 * @return Id of that node.
	 */
	public String getNodeId(String nodeName) {
		String nodeId;

		if (!nodeNameToIdMap.containsKey(nodeName)) {
			nodeId = createStringId();
			this.addNode(nodeId);
			Node node1 = this.getNode(nodeId);
			node1.addAttribute("name", nodeName);
			nodeNameToIdMap.put(nodeName, nodeId);
		} else {
			nodeId = nodeNameToIdMap.get(nodeName);
		}

		return nodeId;
	}

	/**
	 * Find all edges with properties and get their string presentation in GKA
	 * format.
	 * 
	 * @return String presentation in GKA format of all edges and their
	 *         properties.
	 */
	public String getEdgesString() {
		String str = "";

		for (Edge edge : this.getEachEdge()) {
			// add name of node 1
			str += edge.getNode0().getAttribute("name");

			// add direction
			if (edge.isDirected()) {
				str += " -> " + edge.getNode1().getAttribute("name");
			} else {
				str += " -- " + edge.getNode1().getAttribute("name");
			}

			// add name of edge
			if (edge.hasAttribute("name")) {
				str += " (" + edge.getAttribute("name") + ")";
			}

			// add weight of edge
			if (edge.hasAttribute("weight")) {
				str += " : " + edge.getAttribute("weight");
			}

			str += ";\n";
		}

		return str;
	}

	/**
	 * Find single nodes and get their string presentation in GKA format.
	 * 
	 * @return String presentation in GKA format of all single nodes.
	 */
	public String getSingleNodesString() {
		String str = "";

		for (Node node : this.getEachNode()) {
			if (node.getDegree() == 0) {
				str += node.getAttribute("name") + ";\n";
			}
		}

		return str;
	}

	/**
	 * Add an edge to the graph.
	 * @param edgeId Id of the edge
	 * @param nodeId1 Id of node 1 
	 * @param nodeId2 Id of node 2  
	 * @param direction Direction on the edge
	 */
	public void addEdge(String edgeId, String nodeId1, String nodeId2, String direction) {
		if (direction.equals("--")) {
			this.addEdge(edgeId, nodeId1, nodeId2);
		} else if (direction.equals("->")) {
			this.addEdge(edgeId, nodeId1, nodeId2, true);
		}
	}

	/**
	 * Add the properties on an edge. 
	 * @param edgeId Id of the edge
	 * @param edgeName Name of the edge
	 * @param edgeWeight Weight of the edge
	 */
	public void addEdgeAttr(String edgeId, String edgeName, String edgeWeight) {
		Edge edge = this.getEdge(edgeId);

		if (edgeName != null) {
			edge.addAttribute("name", edgeName);
		}

		if (edgeWeight != null) {
			edge.addAttribute("weight", Integer.valueOf(edgeWeight));
		}
	}

	/**
	 * Add labels to nodes and edges.
	 */
	public void addLabels() {
		for (Node node : this) {
			node.addAttribute("label", (String) node.getAttribute("name"));
		}

		for (Edge edge : this.getEachEdge()) {
			// ??? labels for name is overwritten by labels for weight
			if (edge.hasAttribute("name")) {
				edge.addAttribute("label", edge.getAttribute("name").toString());
			}

			if (edge.hasAttribute("weight")) {
				edge.addAttribute("label", (int) edge.getAttribute("weight"));
			}
		}
	}

	/**
	 * Create a unique string id used for creating graph, node, edge objects.
	 * @return A unique string id. 
	 */
	public static String createStringId() {
		return UUID.randomUUID().toString();
	}

}
