import java.util.HashMap;

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
	
}
