import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

public class GkaUtils {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String filename = "graph07.gka";
		GkaGraph myGraph = GkaUtils.read(filename);
		myGraph.display();
		GkaUtils.save(myGraph, "holy.gka");
	}

	/**
	 * Read a gka file and return a GkaGraph object.
	 * 
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static GkaGraph read(String filename) throws FileNotFoundException, IOException {
		Path stylesheetPath = Paths.get(System.getProperty("user.dir") + "/stylesheet/defaultStylesheet");
		return read(filename, stylesheetPath);
	}

	/**
	 * Read a gka file and return a GkaGraph object.
	 * 
	 * @param filePath
	 * @param stylesheetPath
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static GkaGraph read(String filename, Path stylesheetPath)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		if (stylesheetPath != null) {
			graph.addAttribute("ui.stylesheet", "url('file:///" + stylesheetPath.toString() + "')");
		}

		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		Pattern pattern = Pattern.compile(regex);

		String filePath = System.getProperty("user.dir") + "/gka-Dateien/" + filename;
		File file = new File(filePath);
		try (BufferedReader br = new BufferedReader(
				new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				if (matcher.matches()) {
					String nameNode1 = matcher.group(1);
					String direction = matcher.group(3);
					String nameNode2 = matcher.group(4);
					String edgeName = matcher.group(6);
					String edgeWeight = matcher.group(8);

					String nodeId1 = graph.getNodeId(nameNode1);

					if (nameNode2 != null) {
						String nodeId2 = graph.getNodeId(nameNode2);

						String edgeId = GkaGraph.createStringId();

						if (direction.equals("--")) {
							graph.addEdge(edgeId, nodeId1, nodeId2);
						} else if (direction.equals("->")) {
							graph.addEdge(edgeId, nodeId1, nodeId2, true);
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

	/**
	 * Save a GkaGraph object as gka file.
	 * 
	 * @param graph
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void save(GkaGraph graph, String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String filePath = System.getProperty("user.dir") + "/gka-Dateien/" + filename;
		File file = new File(filePath.toString());
		try (BufferedWriter writer = new BufferedWriter(
				new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
			String str = "";
			
			str += graph.getEdgesString();
			str += graph.getSingleNodesString();
			
			writer.write(str);
			
		}
	}

}
