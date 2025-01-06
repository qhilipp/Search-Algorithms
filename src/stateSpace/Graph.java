package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

import goalTest.ListGoalTester;
import heuristic.Heuristic;

/**
 * a finite graph
 * @param	<Node>	the type of the node
 */
public class Graph<Node> extends StateSpace<Node> {

	private HashMap<Node, ArrayList<Edge>> edges = new HashMap<>();
	
	/**
	 * @param	start	the starting node
	 * @param 	nodes	a list of all nodes contained in the graph
	 */
	public Graph(Node start, Node...nodes) {
		super(start, new ListGoalTester<>());
		for(Node node : nodes) {
			edges.put(node, new ArrayList<>());
		}
		edges.put(start, new ArrayList<>());
	}
	
	/**
	 * connects two nodes in one direction
	 * @param 	from	the node from which the connection starts
	 * @param 	to		the node at which the connection ends
	 * @param 	weight	the weight/cost of the connection
	 */
	public void connect(Node from, Node to, double weight) {
		ArrayList<Edge> neighbors = edges.get(from);
		if(neighbors == null) neighbors = new ArrayList<>();
		neighbors.add(new Edge(to, weight));
		edges.put(from, neighbors);
	}
	
	/**
	 * flags the given node as a goal node
	 * @param	node	the node which becomes a goal node
	 */
	public void setGoal(Node node) {
		setGoal(node, true);
	}
	
	/**
	 * flags the given node to be either a goal node or not according to the given boolean value
	 * @param	node	the node which either becomes a goal or not a goal
	 * @param 	value	whether or not the given node becomes a goal
	 */
	public void setGoal(Node node, boolean value) {
		if(value) {
			((ListGoalTester<Node>) getGoalTester()).add(node);
		} else {
			((ListGoalTester<Node>) getGoalTester()).remove(node);
		}
	}

	@Override
	public ArrayList<Node> getNeighbors(Node node) {
		ArrayList<Node> nodes = new ArrayList<>();
		for(Edge edge : edges.get(node)) {
			nodes.add(edge.to);
		}
		return nodes;
	}

	@Override
	public double getCost(Node from, Node to) {
		for(Edge edge : edges.get(from)) {
			if(edge.to.equals(to)) return edge.weight;
		}
		return -1;
	}
	
	/**
	 * a helper class that holds the end node and weight of a connection
	 */
	class Edge {
		Node to;
		double weight;
		
		/**
		 * @param	to		the ending node of the edge
		 * @param 	weight	the weight/cost of the edge
		 */
		Edge(Node to, double weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	
}
