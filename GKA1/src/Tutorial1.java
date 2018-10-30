import java.util.HashMap;
import java.util.UUID;

import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

import scala.collection.parallel.mutable.ParHashSetCombiner.AddingFlatHashTable;

public class Tutorial1 {
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Tutorial 1");
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();
		
		
		
		graph.setStrict(false);
		graph.setAutoCreate(true);
		String edgeId1 = UUID.randomUUID().toString();
		String edgeId2 = UUID.randomUUID().toString();
		
		String idNode1 = UUID.randomUUID().toString();
		String idNode2 = UUID.randomUUID().toString();
	
		graph.addEdge(edgeId1, idNode1, idNode2, true);
		
		Node node1 = graph.getNode(idNode1);
		node1.addAttribute("name", "John");
		
		Node node2 = graph.getNode(idNode2);
		node2.addAttribute("name", "Max");
		
		Edge edge1 = graph.getEdge(edgeId1);
		edge1.addAttribute("name", "Father");
		
		graph.addEdge(edgeId2, idNode1, idNode2, true);
		
		Edge edge2 = graph.getEdge(edgeId2);
		edge2.addAttribute("name", "Teacher");
		
        for (Node node : graph) {
            node.addAttribute("label", node.getAttribute("name").toString());
        }
        
        for (Edge edge : graph.getEachEdge()) {
        	edge.addAttribute("label", edge.getAttribute("name").toString());
        }
	}
	
	protected static String styleSheet =
            "node {" +
            "	text-size: 15;" +
            "	shape: circle;" +
            "	stroke-mode: plain;" +
            "	fill-color: white;" +
            "	size: 20px;" +
            "	text-alignment: center;" +
            "}";
}