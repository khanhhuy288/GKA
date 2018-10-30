import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Scanner;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;

public class GkaReader {
	public static void main(String[] args) throws MalformedURLException, IOException {
		String filename = "graph00.gka";
		Path path = Paths.get(System.getProperty("user.dir"), "/gka-Dateien/" + filename);
		Scanner input = new Scanner(new URL("file:///" + path.toAbsolutePath()).openStream(), "UTF-8");
		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		Pattern pattern = Pattern.compile(regex);

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		Graph graph = new MultiGraph("Tutorial 1");
		String styleSheet = 
				"node {" 
				+ "	text-size: 15;" 
				+ "	shape: circle;" 
				+ "	fill-color: yellow;" 
				+ "	size: 30px;"
				+ "	text-alignment: center;" 
				+ "}";
		graph.addAttribute("ui.stylesheet", styleSheet);
		graph.display();

		HashMap<String, String> nodeNameNodeIdMap = new HashMap<String, String>();

		while (input.hasNextLine()) {
			String line = input.nextLine();
			Matcher matcher = pattern.matcher(line);

			if (matcher.matches()) {
				String nameNode1 = matcher.group(1);
				String direction = matcher.group(3);
				String nameNode2 = matcher.group(4);
				String edgeName = matcher.group(6);
				String edgeWeight = matcher.group(8);

				String node1_ID;
				if (!nodeNameNodeIdMap.containsKey(nameNode1)) {
					node1_ID = UUID.randomUUID().toString();
					graph.addNode(node1_ID);
					Node node1 = graph.getNode(node1_ID);
					node1.addAttribute("name", nameNode1);
					nodeNameNodeIdMap.put(nameNode1, node1_ID);
				} else {
					node1_ID = nodeNameNodeIdMap.get(nameNode1);
				}

				if (nameNode2 != null) {
					String node2_ID;
					if (!nodeNameNodeIdMap.containsKey(nameNode2)) {
						node2_ID = UUID.randomUUID().toString();
						graph.addNode(node2_ID);
						Node node2 = graph.getNode(node2_ID);
						node2.addAttribute("name", nameNode2);
						nodeNameNodeIdMap.put(nameNode2, node2_ID);
					} else {
						node2_ID = nodeNameNodeIdMap.get(nameNode2);
					}

					String edgeId = UUID.randomUUID().toString();

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

		input.close();

	}
}
