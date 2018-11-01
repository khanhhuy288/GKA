import java.util.HashMap;
import java.util.UUID;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GkaGraph extends MultiGraph {
	private HashMap<String, String> nodeNameToIdMap;
	
	public GkaGraph(String id) {
		super(id);
		this.nodeNameToIdMap = new HashMap<String, String>(); 
	}
	
	public HashMap<String, String> getNodeNameToIdMap() {
		return nodeNameToIdMap;
	}

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
	
	public void addEdgeAttr(String edgeId, String edgeName, String edgeWeight) {
		Edge edge = this.getEdge(edgeId);
		
		if (edgeName != null) {
			edge.addAttribute("name", edgeName);
		}

		if (edgeWeight != null) {
			edge.addAttribute("weight", Integer.valueOf(edgeWeight));
		}
	}
	
	public void addLabels() {
		for (Node node : this) {
			node.addAttribute("label", (String) node.getAttribute("name"));
		}

		for (Edge edge : this.getEachEdge()) {
			if (edge.hasAttribute("name")) {
				edge.addAttribute("label", edge.getAttribute("name").toString());
			}

			if (edge.hasAttribute("weight")) {
				edge.addAttribute("label", (int) edge.getAttribute("weight"));
			}
		}
	}
	
	public static String createStringId() {
		return UUID.randomUUID().toString();
	} 
	
}
