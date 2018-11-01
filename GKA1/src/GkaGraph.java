import java.util.HashMap;
import java.util.UUID;

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
	
	public static String createStringId() {
		return UUID.randomUUID().toString();
	} 
	
}
