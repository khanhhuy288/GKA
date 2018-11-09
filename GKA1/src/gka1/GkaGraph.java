package gka1;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

/**
 * GkaGraph has useful methods for working with graph's attributes.
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
	 *            Id of the graph.
	 */
	public GkaGraph(String id) {
		super(id);
		this.nodeNameToIdMap = new HashMap<String, String>();
	}
	
	/**
	 * Create a new node with a given name. If name exists in the hash map, 
     * the method just retrieves the id of the created node.  
	 * 
	 * @param nodeName
	 *            Name of a node.	
	 * @return Id of that node.
	 */
	public String createNode(String nodeName) {
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
	 * Create an edge. If there's no node 2, a single node is created.
	 * @param nodeName1 Name of node 1.
	 * @param nodeName2 Name of node 2. 
	 * @param isDirected Whether graph is directed.
	 * @param edgeName Name of the edge.
	 * @param edgeWeight Weight of the edge.
	 */
	public void createEdge(String nodeName1, String nodeName2, String isDirected, String edgeName, String edgeWeight) {
		// create or get node 1
		String nodeId1 = this.createNode(nodeName1);
		
		if (nodeName2 != null) {
			// create or get node 2
			String nodeId2 = this.createNode(nodeName2);

			// create edge 
			String edgeId = GkaGraph.createStringId();
			
			if (isDirected.equals("--")) {
				this.addEdge(edgeId, nodeId1, nodeId2);
			} else if (isDirected.equals("->")) {
				this.addEdge(edgeId, nodeId1, nodeId2, true);
			}
			
			// add edge's attributes
			Edge edge = this.getEdge(edgeId);
			
			if (edgeName != null) {
				edge.addAttribute("name", edgeName);
			}

			if (edgeWeight != null) {
				edge.addAttribute("weight", Integer.valueOf(edgeWeight));
			}
		}
	}

	/**
	 * Add labels to nodes and edges.
	 */
	public void addLabels() {
		// add labels to nodes 
		for (Node node : this) {
			node.addAttribute("label", node.getAttribute("name").toString());
		}

		// add labels to edges 
		for (Edge edge : this.getEachEdge()) {
			ArrayList<String> edgeProps = new ArrayList<>();
			
			if (edge.hasAttribute("name")){
				edgeProps.add(edge.getAttribute("name"));
			}

			if (edge.hasAttribute("weight")) {
				edgeProps.add(edge.getAttribute("weight").toString());
			}
			
			edge.addAttribute("label", String.join(" : ", edgeProps));
		}
	}
	
	/**
	 * Find all edges with properties and get their string presentation in GKA
	 * format.
	 * 
	 * @return String representation in GKA format of all edges and their
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
	 * Create a unique string id used for creating graph, node, edge objects.
	 * @return A unique string id. 
	 */
	public static String createStringId() {
		return UUID.randomUUID().toString();
	}
	
	public Set<String> getNodeNames() {
		return nodeNameToIdMap.keySet();
	}

	/**
	 * Beautify the graph with default stylesheet. 
	 */
	public void beautify() {
		beautify("defaultStylesheet");
	}
	
	/**
	 * Beautify the graph with custom stylesheet.
	 * @param stylesheetName Name of the stylesheet. 
	 */
	public void beautify(String stylesheetName) {
		// set stylesheet
		String stylesheetPath = System.getProperty("user.dir") + "/stylesheet/" + stylesheetName;
		this.addAttribute("ui.stylesheet", "url('file:///" + stylesheetPath + "')");
		
		// improve viewing quality
		this.addAttribute("ui.quality");
        this.addAttribute("ui.antialias");
		
        // add labels to nodes and edges
		this.addLabels();
	}
}
