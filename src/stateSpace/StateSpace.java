package stateSpace;

import java.util.ArrayList;

import goalTest.GoalTester;
import heuristic.Heuristic;

public abstract class StateSpace<Node> {
	
	protected Heuristic<Node> heuristic;
	protected GoalTester<Node> goalTester;
	
	public StateSpace(Heuristic<Node> heuristic, GoalTester<Node> goalTester) {
		this.heuristic = heuristic;
		this.goalTester = goalTester;
	}
	
	public abstract Node getStart();
	public abstract ArrayList<Node> getNeighbors(Node node);
	public abstract double getCost(Node from, Node to);
	
	public boolean isGoal(Node node) {
		return goalTester.isGoal(node);
	}
	
	public double futureCost(Node node) {
		return heuristic.futureCost(node);
	}
}
