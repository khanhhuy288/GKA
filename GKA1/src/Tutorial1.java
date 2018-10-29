import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Tutorial1 {
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new SingleGraph("Tutorial 1");
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();
		
		graph.setStrict(false);
		graph.setAutoCreate( true );
		graph.addEdge("AB", "A", "B", true);
		graph.addEdge("BA", "B", "A", true);
		graph.addEdge("BC", "B", "C");
		graph.addEdge("BB", "B", "B", true);
		graph.addEdge("CA", "C", "A");
		
        for (Node node : graph) {
            node.addAttribute("ui.label", node.getId());
        }
        
        for (Edge edge : graph.getEachEdge()) {
        	edge.addAttribute("ui.label", edge.getId());
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