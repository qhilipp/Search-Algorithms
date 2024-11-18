package stateSpace;

import java.util.ArrayList;
import java.util.HashMap;

public class Graph<Content> implements StateSpace<Content> {

	private Content start;
	private HashMap<Content, Boolean> goalMap = new HashMap<>();
	private HashMap<Content, ArrayList<Edge>> edges = new HashMap<>();
	
	public Graph(Content start, Content...nodes) {
		for(Content node : nodes) {
			edges.put(node, new ArrayList<>());
		}
		this.start = start;
		edges.put(start, new ArrayList<>());
	}
	
	public void connect(Content from, Content to, double weight) {
		ArrayList<Edge> neighbors = edges.get(from);
		if(neighbors == null) neighbors = new ArrayList<>();
		neighbors.add(new Edge(to, weight));
		edges.put(from, neighbors);
	}
	
	public void setGoal(Content node) {
		setGoal(node, true);
	}
	
	public void setGoal(Content node, boolean value) {
		goalMap.put(node, value);
	}
	
	@Override
	public Content getStart() {
		return start;
	}

	@Override
	public ArrayList<Content> getNeighbors(Content node) {
		ArrayList<Content> nodes = new ArrayList<>();
		for(Edge edge : edges.get(node)) {
			nodes.add(edge.to);
		}
		return nodes;
	}

	@Override
	public boolean isGoal(Content node) {
		return goalMap.containsKey(node) ? goalMap.get(node) : false;
	}

	@Override
	public double getCost(Content from, Content to) {
		for(Edge edge : edges.get(from)) {
			if(edge.to.equals(to)) return edge.weight;
		}
		return -1;
	}
	
	class Edge {
		Content to;
		double weight;
		
		Edge(Content to, double weight) {
			this.to = to;
			this.weight = weight;
		}
	}
	
}
