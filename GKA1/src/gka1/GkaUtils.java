package gka1;

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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;

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
		// String filename = "graphTest1.gka";
		// GkaGraph graph = GkaUtils.read(filename);
		// GkaUtils.save(graph, "savedGraphTest1.gka");
		// GkaGraph savedGraph = GkaUtils.read("savedGraphTest1.gka");
		// savedGraph.display();
		// savedGraph.beautify();

		// String[] filenames = {"graph01.gka", "graph02.gka", "graph03.gka",
		// "graph04.gka", "graph05.gka",
		// "graph06.gka", "graph07.gka", "graph08.gka", "graph09.gka",
		// "graph10.gka", "graph10.gka"};
		//
		// GkaGraph myGraph;
		//
		// for (String filename : filenames) {
		// myGraph = GkaUtils.read(filename);
		// myGraph.display();
		// myGraph.beautify();
		// }

		GkaGraph bigNet = GkaUtils.generateNetwork(6, 10, true, 1, 10, 0);
		bigNet.display();
		bigNet.beautify();
	}

	/**
	 * Get the string presentation of a list of nodes.
	 * 
	 * @param nodes
	 *            List of nodes.
	 * @return List of node names.
	 */
	public static List<String> toNodesString(List<Node> nodes) {
		List<String> res = new ArrayList<>();
		if (nodes == null) {
			return res;
		}
		for (Node node : nodes) {
			res.add(node.getAttribute("name"));
		}
		return res;
	}

	/**
	 * Read a GKA file from the <em>gkaFiles</em> folder.
	 * 
	 * @param filename
	 *            Name of the GKA file.
	 * @param stylesheetName
	 *            Name of the stylesheet.
	 * @return A GkaGraph object.
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws FileNotFoundException
	 *             if the file path cannot be found
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static GkaGraph read(String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// create empty graph
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		// regex for GKA format of each line
		String regex = "^\\s*([\\wÄäÖöÜüß]+)\\s*((->|--)\\s*([\\wÄäÖöÜüß]+)\\s*(\\(\\s*([\\wÄäÖöÜüß\\s]+)\\s*\\)\\s*)?(:\\s*(\\d+)\\s*)?)?;\\s*$";
		Pattern pattern = Pattern.compile(regex);

		// set file path
		String filePath = System.getProperty("user.dir") + "/gkaFiles/" + filename;
		File file = new File(filePath);

		// read file line by line
		try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
			String line;
			while ((line = br.readLine()) != null) {
				Matcher matcher = pattern.matcher(line);

				// find the properties of a line
				if (matcher.matches()) {
					String nodeName1 = matcher.group(1);
					String isDirected = matcher.group(3);
					String nodeName2 = matcher.group(4);
					String edgeName = matcher.group(6);
					// remove extra whitespaces
					if (edgeName != null) {
						edgeName = edgeName.trim().replaceAll("\\s+", " ");
					}
					String edgeWeight = matcher.group(8);

					// create an edge or a single node
					graph.createEdge(nodeName1, nodeName2, isDirected, edgeName, edgeWeight);
				}
			}
		}

		// set viewer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		return graph;
	}

	/**
	 * Save a GkaGraph object as a GKA file to the <em>gkaFiles</em> folder.
	 * 
	 * @param graph
	 *            A graph in GKA format.
	 * @param filename
	 *            Name of the new GKA file.
	 * @throws UnsupportedEncodingException
	 *             if the encoding is not supported
	 * @throws FileNotFoundException
	 *             if the file path cannot be found
	 * @throws IOException
	 *             if an I/O exception occurs
	 */
	public static void save(GkaGraph graph, String filename)
			throws UnsupportedEncodingException, FileNotFoundException, IOException {
		// set save path
		String filePath = System.getProperty("user.dir") + "/gkaFiles/" + filename;
		File file = new File(filePath);

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

	/**
	 * Generate a random graph with edge weight and without seed.
	 * 
	 * @param nodeNum
	 *            Number of nodes.
	 * @param edgeNum
	 *            Number of edges.
	 * @param isDirected
	 *            Whether graph is directed.
	 * @param hasEdgeName
	 *            Whether name edges should be added.
	 * @return A GkaGraph object.
	 */
	public static GkaGraph generateRandom(int nodeNum, int edgeNum, boolean isDirected, boolean hasEdgeName,
			int edgeWeightMin, int edgeWeightMax) {
		return generateRandom(nodeNum, edgeNum, isDirected, hasEdgeName, edgeWeightMin, edgeWeightMax, null);
	}

	public static GkaGraph generateNetwork(int nodeNum, int edgeNum, boolean hasEdgeName, int edgeWeightMin,
			int edgeWeightMax) {
		return generateNetwork(nodeNum, edgeNum, hasEdgeName, edgeWeightMin, edgeWeightMax, null);
	}

	/**
	 * Generate a random graph without edge weights and seed.
	 * 
	 * @param nodeNum
	 *            Number of nodes.
	 * @param edgeNum
	 *            Number of edges.
	 * @param isDirected
	 *            Whether graph is directed.
	 * @param hasEdgeName
	 *            Whether name edges should be added.
	 * @return A GkaGraph object.
	 */
	public static GkaGraph generateRandom(int nodeNum, int edgeNum, boolean isDirected, boolean hasEdgeName) {
		return generateRandom(nodeNum, edgeNum, isDirected, hasEdgeName, 2, 1, null);
	}

	/**
	 * Generate a random simple graph with a fixed number of nodes and edges. Edge
	 * names are optional. Edge weight is also optional and can be set within a
	 * range.
	 * 
	 * @param nodeNum
	 *            Number of nodes.
	 * @param edgeNum
	 *            Number of edges.
	 * @param isDirected
	 *            Whether graph is directed.
	 * @param hasEdgeName
	 *            Whether edge names should be added.
	 * @param edgeWeightMin
	 *            The minimum weight of an edge.
	 * @param edgeWeightMax
	 *            The maximum wieght of an edge.
	 * @param seed
	 *            Seed for random generator.
	 * @return A GkaGraph object.
	 */
	public static GkaGraph generateRandom(int nodeNum, int edgeNum, boolean isDirected, boolean hasEdgeName,
			int edgeWeightMin, int edgeWeightMax, Integer seed) {
		// create empty graph
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		// add nodes
		for (int i = 0; i < nodeNum; i++) {
			graph.createNode(String.valueOf(i));
		}

		// get list of node names
		ArrayList<String> nodesNameList = new ArrayList<>(graph.getNodeNames());

		// set directivity
		String isDirectedStr = isDirected ? "->" : "--";

		// add edges with random nodes, names or weight
		Random rand = seed != null ? new Random(seed) : new Random();
		String nodeName1;
		String nodeName2;
		Boolean hasEdgeWeight = edgeWeightMin <= edgeWeightMax;
		int edgeWeight;
		for (int i = 0; i < edgeNum; i++) {
			// create 2 different random nodes
			nodeName1 = nodesNameList.get(rand.nextInt(nodesNameList.size()));
			do {
				nodeName2 = nodesNameList.get(rand.nextInt(nodesNameList.size()));
			} while (nodeName2 == nodeName1);
			Node node1 = graph.getNode(graph.createNode(nodeName1));
			Node node2 = graph.getNode(graph.createNode(nodeName2));

			// create an edge between the 2 nodes if they aren't yet connected
			if (!node1.hasEdgeToward(node2)) {
				if (hasEdgeWeight) {
					edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
					if (hasEdgeName) {
						graph.createEdge(nodeName1, nodeName2, isDirectedStr, "e" + i, String.valueOf(edgeWeight));
					} else {
						graph.createEdge(nodeName1, nodeName2, isDirectedStr, null, String.valueOf(edgeWeight));
					}
				} else {
					if (hasEdgeName) {
						graph.createEdge(nodeName1, nodeName2, isDirectedStr, "e" + i, null);
					} else {
						graph.createEdge(nodeName1, nodeName2, isDirectedStr, null, null);
					}
				}
			}
		}

		// set viewer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		return graph;
	}

	public static GkaGraph generateNetwork(int nodeNum, int edgeNum, boolean hasEdgeName, int edgeWeightMin,
			int edgeWeightMax, Integer seed) {
		// negative Anzahl Knoten
		if (nodeNum < 0 || edgeNum < 0) {
			throw new IllegalArgumentException("Number of nodes and edges should be positive.");
		}
		// Kantenanzahl nicht größer als Knotenanzahl
		if (edgeNum < nodeNum - 1) {
			throw new IllegalArgumentException("Cannot create Network, because the created graph is not connected.");
		}
		// Nicht mindestens 2 Knoten für Quelle und Senke
		if (nodeNum < 2) {
			throw new IllegalArgumentException("Muss mindestens 2 Knoten besitzen für ein Netzwerk (Quelle und Senke)");
		}

		if (edgeNum > nodeNum * (nodeNum - 1) / 2) {
			throw new IllegalArgumentException("No multi-edges");
		}

		// create empty graph
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		// add nodes
		for (int i = 0; i < nodeNum; i++) {
			graph.createNode(String.valueOf(i));
		}

		// get list of node names
		ArrayList<String> nodesNameList = new ArrayList<>(graph.getNodeNames());

		// add edges with random nodes, names or weight
		Random rand = seed != null ? new Random(seed) : new Random();
		String nodeName1;
		String nodeName2;
		Boolean hasEdgeWeight = edgeWeightMin <= edgeWeightMax;
		int edgeWeight;

		for (int i = 0; i < edgeNum; i++) {
			// create 2 different random nodes, node1 can't be "n-1", node2 can't be "0"
			nodeName1 = nodesNameList.get(rand.nextInt(nodeNum - 1));
			do {
				nodeName2 = nodesNameList.get(rand.nextInt(nodeNum));
			} while (nodeName2 == nodeName1 || nodeName2 == "0");
			Node node1 = graph.getNode(graph.createNode(nodeName1));
			Node node2 = graph.getNode(graph.createNode(nodeName2));

			// create an edge between the 2 nodes if they aren't yet connected
			if (!node1.hasEdgeBetween(node2)) {
				if (hasEdgeWeight) {
					edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
					graph.createEdge(nodeName1, nodeName2, "->", null, String.valueOf(edgeWeight));
				} else {
					graph.createEdge(nodeName1, nodeName2, "->", null, null);
				}
			}
		}

		for (int i = 0; i < nodeNum; i++) {
			Node node = graph.getNode(graph.createNode(String.valueOf(i)));
			Iterator<Edge> enteringEdge = node.getEnteringEdgeIterator();
			if (i > 0 && !enteringEdge.hasNext()) {
				String startName;
				do {
					startName = nodesNameList.get(rand.nextInt(nodeNum - 1));
				} while (String.valueOf(i).equals(startName));
				if (hasEdgeWeight) {
					edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
					graph.createEdge(startName, String.valueOf(i), "->", null, String.valueOf(edgeWeight));
				} else {
					graph.createEdge(startName, String.valueOf(i), "->", null, null);
				}
			}

			Iterator<Edge> leavingEdge = node.getLeavingEdgeIterator();
			if (i < nodeNum - 1 && !leavingEdge.hasNext()) {
				String endName;
				do {
					endName = nodesNameList.get(1 + rand.nextInt(nodeNum - 1));
					System.out.println(endName + "(" + Integer.valueOf(endName).toString() + ")");
				} while (String.valueOf(i).equals(endName) || String.valueOf(0).equals(endName));
				if (hasEdgeWeight) {
					edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
					graph.createEdge(String.valueOf(i), endName, "->", null, String.valueOf(edgeWeight));
				} else {
					graph.createEdge(String.valueOf(i), endName, "->", null, null);
				}
			}
		}

		// set viewer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		return graph;
	}

}
