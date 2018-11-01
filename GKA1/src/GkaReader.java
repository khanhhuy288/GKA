import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

import sun.font.CreatedFontTracker;

public class GkaReader {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String filename = "graph01.gka";
		Path path = Paths.get(System.getProperty("user.dir"), "/gka-Dateien/" + filename);
		File file = new File(path.toString());
		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		Pattern pattern = Pattern.compile(regex);

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		GkaGraph graph = new GkaGraph("My Graph");

		graph.addAttribute("ui.stylesheet", "url('file:///D:/GitHub/GKA/GKA1/src/stylesheet')");
		graph.display();

		HashMap<String, String> nodeNameNodeIdMap = graph.getNodeNameToIdMap();

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.matches()) {
					String nameNode1 = matcher.group(1);
					String direction = matcher.group(3);
					String nameNode2 = matcher.group(4);
					String edgeName = matcher.group(6);
					String edgeWeight = matcher.group(8);

					String node1_ID = graph.getNodeId(nameNode1);

					if (nameNode2 != null) {
						String node2_ID = graph.getNodeId(nameNode2);
						String edgeId = GkaGraph.createStringId();

						if (direction.equals("--")) {
							graph.addEdge(edgeId, node1_ID, node2_ID);

						} else if (direction.equals("->")) {
							graph.addEdge(edgeId, node1_ID, node2_ID, true);
						}

						Edge edge = graph.getEdge(edgeId);

						if (edgeName != null) {
							edge.addAttribute("name", edgeName);
						}

						if (edgeWeight != null) {
							edge.addAttribute("weight", Integer.valueOf(edgeWeight));
						}
					}

				}
			}
		}

		// add labels for nodes and edges
		for (Node node : graph) {
			node.addAttribute("label", (String) node.getAttribute("name"));
		}

		for (Edge edge : graph.getEachEdge()) {
			if (edge.hasAttribute("name")) {
				edge.addAttribute("label", edge.getAttribute("name").toString());
			}

			if (edge.hasAttribute("weight")) {
				edge.addAttribute("label", (int) edge.getAttribute("weight"));
			}
		}

	}

}
