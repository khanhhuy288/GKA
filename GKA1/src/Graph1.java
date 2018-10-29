import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;

public class Graph1 {
	protected static String styleSheet =
            "node {" +
            "	text-size: 20;" +
            "	shape: circle;" +
            "	stroke-mode: plain;" +
            "	fill-color: white;" +
            "	size: 20px;" +
            "	text-alignment: center;" +
            "}" +
            "edge {" +
            "	text-size: 20;" +
            "	text-alignment: along;" +
            "	text-background-mode: rounded-box;" +
            "	stroke-mode: plain;" +
            "}";
	
	public static void main(String args[]) {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph g = new SingleGraph("example");
		
		g.addAttribute("ui.stylesheet", styleSheet);
		g.display();	
		
		g.addNode("A");
		g.addNode("B");
		g.addNode("C");
		g.addNode("D");
		g.addNode("E");
		g.addNode("F");
		g.addNode("G");
		g.addEdge("AB", "A", "B").addAttribute("length", 14);
		Edge ab = g.getEdge("AB");
		System.out.println(ab.isDirected());
		g.addEdge("AC", "A", "C", true).addAttribute("length", 9);
		g.addEdge("CA", "C", "A", true).addAttribute("length", 10);
		g.addEdge("AD", "A", "D").addAttribute("length", 7);
		g.addEdge("BC", "B", "C").addAttribute("length", 2);
		g.addEdge("CD", "C", "D").addAttribute("length", 10);
		g.addEdge("BE", "B", "E").addAttribute("length", 9);
		g.addEdge("CF", "C", "F").addAttribute("length", 11);
		g.addEdge("DF", "D", "F").addAttribute("length", 15);
		g.addEdge("EF", "E", "F").addAttribute("length", 6);
		for (Node n : g)
			n.addAttribute("label", n.getId());
		for (Edge e : g.getEachEdge())
			e.addAttribute("label", "" + (int) e.getNumber("length"));
	}
}
