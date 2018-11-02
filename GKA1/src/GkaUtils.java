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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility Class for reading a GKA file or saving a GkaGraph object as GKA
 * file.<br>
 * GKA format of each line: <br>
 * <strong>directed</strong>: &lt;name node 1&gt; [ -> &lt;name node 2&gt;
 * [(&lt;edge name&gt;)] [: &lt;edge weight&gt;]]<br>
 * <strong>undirected</strong>: &lt;name node 1&gt; [ -- &lt;name node 2&gt;
 * [(&lt;edge name&gt;)] [: &lt;edge weight&gt;]]<br>
 * 
 * @author Huy Tran PC
 *
 */
public class GkaUtils {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		String filename = "graph04.gka";
		GkaGraph myGraph = GkaUtils.read(filename, "defaultStylesheet");
		myGraph.display();
		GkaUtils.save(myGraph, "holy.gka");
	}

	/**
	 * Read a GKA file from the <em>gka-Dateien</em> folder. Default stylesheet
	 * in the <em>stylesheet</em> folder is used.
	 * 
	 * @param filename
	 *            Name of the GKA file
	 * @return A GkaGraph object
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws FileNotFoundException
	 *             if the file path cannot be found
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static GkaGraph read(String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		String stylesheetName = "defaultStylesheet";
		return read(filename, stylesheetName);
	}

	/**
	 * Read a GKA file from the <em>gka-Dateien</em> folder. Custom stylesheet
	 * in the <em>stylesheet</em> folder is used.
	 * 
	 * @param filename
	 *            Name of the GKA file
	 * @param stylesheetName
	 *            Name of the stylesheet.
	 * @return A GkaGraph object
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws FileNotFoundException
	 *             if the file path cannot be found
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static GkaGraph read(String filename, String stylesheetName)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// create empty graph
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		// set viewer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		// set stylesheet
		String stylesheetPath = System.getProperty("user.dir") + "/stylesheet/" + stylesheetName;
		graph.addAttribute("ui.stylesheet", "url('file:///" + stylesheetPath + "')");

		// regex for GKA format of each line
		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;$";
		Pattern pattern = Pattern.compile(regex);

		// set file path
		String filePath = System.getProperty("user.dir") + "/gka-Dateien/" + filename;
		File file = new File(filePath);

		// read file line by line
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				// find the properties of a line
				if (matcher.matches()) {
					String nameNode1 = matcher.group(1);
					String direction = matcher.group(3);
					String nameNode2 = matcher.group(4);
					String edgeName = matcher.group(6);
					String edgeWeight = matcher.group(8);

					// create or get node 1
					String nodeId1 = graph.getNodeId(nameNode1);

					if (nameNode2 != null) {
						// create or get node 2
						String nodeId2 = graph.getNodeId(nameNode2);

						// create edge with its properties
						String edgeId = GkaGraph.createStringId();
						graph.addEdge(edgeId, nodeId1, nodeId2, direction);
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
	 * Save a GkaGraph object as a GKA file to the <em>gka-Dateien</em> folder.
	 * 
	 * @param graph
	 * @throws IOException
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public static void save(GkaGraph graph, String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// set save path
		String filePath = System.getProperty("user.dir") + "/gka-Dateien/" + filename;
		File file = new File(filePath.toString());

		// write GKA object to file
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
			String str = "";

			// add string of all edges 
			str += graph.getEdgesString();
			
			// add string of single nodes
			str += graph.getSingleNodesString();

			writer.write(str);
		}
	}

}
