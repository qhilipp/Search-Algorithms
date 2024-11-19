package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

import goalTest.ListGoalTester;
import heuristic.Heuristic;

public class Graph<Node> extends StateSpace<Node> {

	private Node start;
	private HashMap<Node, ArrayList<Edge>> edges = new HashMap<>();
	
	public Graph(Heuristic<Node> heuristic, Node start, Node...nodes) {
		super(heuristic, new ListGoalTester<Node>());
		for(Node node : nodes) {
			edges.put(node, new ArrayList<>());
		}
		this.start = start;
		edges.put(start, new ArrayList<>());
	}
	
	public void connect(Node from, Node to, double weight) {
		ArrayList<Edge> neighbors = edges.get(from);
		if(neighbors == null) neighbors = new ArrayList<>();
		neighbors.add(new Edge(to, weight));
		edges.put(from, neighbors);
	}
	
	public void setGoal(Node node) {
		setGoal(node, true);
	}
	
	public void setGoal(Node node, boolean value) {
		if(value) {
			((ListGoalTester<Node>) this.goalTester).add(node);
		} else {
			((ListGoalTester<Node>) this.goalTester).remove(node);
		}
	}
	
	@Override
	public Node getStart() {
		return start;
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
	
	class Edge {
		Node to;
		double weight;
		
		Edge(Node to, double weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	
}
