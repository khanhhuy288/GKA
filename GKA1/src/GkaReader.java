import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GkaReader {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String filename = "graph01.gka";
		Path filePath = Paths.get(System.getProperty("user.dir") + "/gka-Dateien/" + filename);
		Path stylesheetPath = Paths.get(System.getProperty("user.dir") + "/src/stylesheet");
	
		GkaGraph myGraph = GkaReader.read(filePath, stylesheetPath);
		myGraph.display();
	}
	
	public static GkaGraph read(Path filePath) throws FileNotFoundException, IOException {
		return read(filePath, null);
	}
	
	public static GkaGraph read(Path filePath, Path stylesheetPath) throws FileNotFoundException, IOException {
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		if (stylesheetPath != null) {
			graph.addAttribute("ui.stylesheet", "url('file:///" + stylesheetPath.toString() + "')");
			System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		}

		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		Pattern pattern = Pattern.compile(regex);
		
		File file = new File(filePath.toString());
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
						} else if (direction.equals("->")){
							graph.addEdge(edgeId, node1_ID, node2_ID, true);
						}

						graph.addEdgeAttr(edgeId, edgeName, edgeWeight);
					}

				}
			}
		}

		// add labels for nodes and edges
		graph.addLabels();
		
		return graph;
	}
	
	

}
