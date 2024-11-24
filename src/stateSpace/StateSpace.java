package stateSpace;

import java.util.ArrayList;
import java.util.HashSet;

import goalTest.GoalTester;

public abstract class StateSpace<Node> {
	
	private GoalTester<Node> goalTester;
	private Node start;
	
	public StateSpace(Node start, GoalTester<Node> goalTester) {
		this.start = start;
		this.goalTester = goalTester;
	}
	
	public abstract ArrayList<Node> getNeighbors(Node node);
	public abstract double getCost(Node from, Node to);
	
	public Node getStart() {
		return start;
	}
	
	public void setStart(Node start) {
		this.start = start;
	}
	
	public boolean isGoal(Node node) {
		return goalTester.isGoal(node);
	}
	
	public GoalTester<Node> getGoalTester() {
		return goalTester;
	}
	
	public ArrayList<Node> getNodes() {
		HashSet<Node> nodes = new HashSet<>();
		nodes.add(getStart());
		boolean change = true;
		int iterations = 0;
		while(change) {
			int previousSize = nodes.size();
			HashSet<Node> newNodes = new HashSet<>();
			for(Node node : nodes) {
				ArrayList<Node> n = getNeighbors(node);
				newNodes.addAll(getNeighbors(node));
			}
			nodes.addAll(newNodes);			
			change = previousSize != nodes.size() && iterations++ < 10000;
		}
		return new ArrayList<>(nodes);
	}
}
