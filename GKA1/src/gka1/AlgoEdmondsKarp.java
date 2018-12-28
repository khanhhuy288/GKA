package gka1;

import java.util.ArrayList;
import java.util.List;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.AbstractEdge;
import org.graphstream.graph.implementations.AbstractNode;

public class AlgoEdmondsKarp {
	private static class Edge extends AbstractEdge {
		public String id;
		public AbstractNode source;
		public AbstractNode target;
		public boolean directed;
		private Edge residual;
		private long flow;
		private final long capacity;
		
	    protected Edge(String id, AbstractNode source, AbstractNode target, boolean directed) {
			super(id, source, target, directed);
			this.capacity= Long.valueOf(getAttribute("weight"));
		}

	    

	    public boolean isResidual() {
	      return capacity == 0;
	    }

	    public long remainingCapacity() {
	      return capacity - flow;
	    }

	    public void augment(long bottleNeck) {
	      flow += bottleNeck;
	      residual.flow -= bottleNeck;
	    }

	    
	  }
	
	private static abstract class NetworkFlowSolverBase {

	    // To avoid overflow, set infinity to a value less than Long.MAX_VALUE;
	    static final long INF = Long.MAX_VALUE / 2;

	    // Inputs: n = number of nodes, s = source, t = sink
	    final int n, s, t;

	    // 'visited' and 'visitedToken' are variables used in graph sub-routines to 
	    // track whether a node has been visited or not. In particular, node 'i' was 
	    // recently visited if visited[i] == visitedToken is true. This is handy 
	    // because to mark all nodes as unvisited simply increment the visitedToken.
	    private int visitedToken = 1;
	    private int[] visited;

	    // Indicates whether the network flow algorithm has ran. The solver only 
	    // needs to run once because it always yields the same result.
	    protected boolean solved;

	    /**
	     *  The maximum flow. Calculated by calling the {@link #solve} method.
	     */
	    protected long maxFlow;

	    // The adjacency list representing the flow graph.
	    protected List<Edge>[] graph;

	    /**
	     * Creates an instance of a flow network solver. Use the {@link #addEdge}
	     * method to add edges to the graph.
	     *
	     * @param n - The number of nodes in the graph including s and t.
	     * @param s - The index of the source node, 0 <= s < n
	     * @param t - The index of the sink node, 0 <= t < n and t != s
	     */
	    public NetworkFlowSolverBase(GkaGraph graph, int s, int t) {

	      this.s = s; 
	      this.t = t; 
	      
	      visited = new int[n];
	    }

	    

	    /**
	     * Adds a directed edge (and its residual edge) to the flow graph.
	     *
	     * @param from     - The index of the node the directed edge starts at.
	     * @param to       - The index of the node the directed edge ends at.
	     * @param capacity - The capacity of the edge
	     */
	    public void addEdge(int from, int to, long capacity) {
	      if (capacity <= 0) 
	        throw new IllegalArgumentException("Forward edge capacity <= 0");
	      Edge e1 = new Edge(from, to, capacity);
	      Edge e2 = new Edge(to, from, 0);
	      e1.residual = e2;
	      e2.residual = e1;
	      graph[from].add(e1);
	      graph[to].add(e2);
	    }


	    // Returns the maximum flow from the source to the sink.
	    public long getMaxFlow() {
	      execute();
	      return maxFlow;
	    }

	    // Marks node 'i' as visited.
	    public void visit(int i) {
	      visited[i] = visitedToken;
	    }

	    // Returns true/false depending on whether node 'i' has been visited or not.
	    public boolean visited(int i) {
	      return visited[i] == visitedToken;
	    }

	    // Resets all nodes as unvisited. This is especially useful to do
	    // between iterations finding augmenting paths, O(1)
	    public void markAllNodesAsUnvisited() {
	      visitedToken++;
	    }

	    // Wrapper method that ensures we only call solve() once
	    private void execute() {
	      if (solved) return; 
	      solved = true;
	      solve();
	    }

	    // Method to implement which solves the network flow problem.
	    public abstract void solve();
	  }

}
