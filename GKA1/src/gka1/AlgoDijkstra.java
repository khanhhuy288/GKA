package gka1;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.graphstream.graph.Node;

public class AlgoDijkstra {
	public static void main(String[] args) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = GkaUtils.read("VLtest.gka");
		graph.display();
		graph.beautify();
	}
	public static List<Node> shortestPath(GkaGraph graph, String startNodeName, String endNodeName,
			boolean isVisualized) {
		

		return null;
	}

}
