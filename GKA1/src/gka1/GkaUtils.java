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

//		for (int i = 0; i < 5; i++) {
//			Random rand = new Random();
//			// 2 <= nodeNum <= 10
//			int nodeNum = 2 + rand.nextInt(10 - 2 + 1);
//			// n-1 <= edgeNum <= n(n-3)+3, n is nodeNum
//			int edgeNum = nodeNum - 1 + rand.nextInt(nodeNum * (nodeNum - 3) / 2 + 2);
//			GkaGraph bigNet = GkaUtils.generateNetwork(nodeNum, edgeNum, 1, 100);
//			bigNet.display();
//			bigNet.beautify();
//		}
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

	/**
	 * Generate a random flow network and without seed.
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
	public static GkaGraph generateNetwork(int nodeNum, int edgeNum, int edgeWeightMin, int edgeWeightMax) {
		return generateNetwork(nodeNum, edgeNum, edgeWeightMin, edgeWeightMax, null);
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
	 *            The maximum weight of an edge.
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

	/**
	 * Generate a random flow network with a fixed number of nodes and edges. Edge
	 * weight can be set within a range. Source and sink of the network are nodes
	 * with index 0 and (nodeNum - 1) respectively.
	 * 
	 * @param nodeNum
	 *            Number of nodes.
	 * @param edgeNum
	 *            Number of edges.
	 * @param edgeWeightMin
	 *            The minimum weight of an edge.
	 * @param edgeWeightMax
	 *            The maximum weight of an edge.
	 * @param seed
	 *            Seed for random generator.
	 * @return A GkaGraph object.
	 */
	public static GkaGraph generateNetwork(int nodeNum, int edgeNum, int edgeWeightMin, int edgeWeightMax,
			Integer seed) {
		if (nodeNum < 0 || edgeNum < 0) {
			throw new IllegalArgumentException("Number of nodes and edges must be positive.");
		}

		// edgeNum >= nodeNum - 1, so that the graph could be connected
		if (edgeNum < nodeNum - 1) {
			throw new IllegalArgumentException(
					"Cannot create network, because the to be created graph is not connected.");
		}

		// need at least 2 nodes to make a network (source and sink)
		if (nodeNum < 2) {
			throw new IllegalArgumentException("At least two nodes are needed to creat a network.");
		}

		// maximum number of edges for a graph to be simple
		if (edgeNum > nodeNum * (nodeNum - 1) / 2) {
			throw new IllegalArgumentException("Too many edges. No multi-edge is allowed in a network.");
		}

		if (edgeWeightMin >= edgeWeightMax) {
			throw new IllegalArgumentException("Minimum edge weight must be smaller than maximum edge weight.");
		}

		// create empty graph
		GkaGraph graph = new GkaGraph(GkaGraph.createStringId());

		// add nodes
		for (int i = 0; i < nodeNum; i++) {
			graph.createNode(String.valueOf(i));
		}

		// get list of node names
		ArrayList<String> nodesNameList = new ArrayList<>(graph.getNodeNames());

		Random rand = seed != null ? new Random(seed) : new Random();
		String nodeName1;
		String nodeName2;

		// create (nodeNum - 1) edges to connect the nodes, ensure the graph will be
		// connected
		int createdEdgeNum = 0;
		for (int i = 1; i < nodeNum; i++) {
			String iNodeName = String.valueOf(i);

			// find a random node that is neither the sink nor the current node
			do {
				nodeName1 = nodesNameList.get(rand.nextInt(nodeNum - 1));
			} while (nodeName1.equals(iNodeName));

			Node node1 = graph.getNode(graph.createNode(nodeName1));
			Node iNode = graph.getNode(graph.createNode(iNodeName));

			// create an edge of random weight from the random node to the current node
			if (!node1.hasEdgeBetween(iNode)) {
				int edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
				graph.createEdge(nodeName1, iNodeName, "->", null, String.valueOf(edgeWeight));
				createdEdgeNum++;
			}
		}

		// connect random nodes until the we have edgeNum edges. Edges aren't allowed to
		// go from sink or go to source
		while (createdEdgeNum < edgeNum) {
			// node1 != sink
			nodeName1 = nodesNameList.get(rand.nextInt(nodeNum - 1));

			// node2 != source and node2 != node1
			do {
				nodeName2 = nodesNameList.get(rand.nextInt(nodeNum));
			} while (nodeName1.equals(nodeName2) || nodeName2.equals("0"));

			Node node1 = graph.getNode(graph.createNode(nodeName1));
			Node node2 = graph.getNode(graph.createNode(nodeName2));

			// create an edge between the 2 nodes if they aren't yet connected
			if (!node1.hasEdgeBetween(node2)) {
				int edgeWeight = rand.nextInt(edgeWeightMax - edgeWeightMin + 1) + edgeWeightMin;
				graph.createEdge(nodeName1, nodeName2, "->", null, String.valueOf(edgeWeight));
				createdEdgeNum++;
			}
		}

		// mark source and sink in the visualized graph
		Node source = graph.getNode(graph.createNode("0"));
		Node sink = graph.getNode(graph.createNode(String.valueOf(nodeNum - 1)));
		source.addAttribute("ui.class", "special");
		sink.addAttribute("ui.class", "special");

		// set viewer
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");

		return graph;
	}

}
