package stateSpace;

import java.util.ArrayList;

import goalTest.GoalTester;

public abstract class StateSpace<Node> {
	
	private GoalTester<Node> goalTester;
	
	public StateSpace(GoalTester<Node> goalTester) {
		this.goalTester = goalTester;
	}
	
	public abstract Node getStart();
	public abstract ArrayList<Node> getNeighbors(Node node);
	public abstract double getCost(Node from, Node to);
	
	public boolean isGoal(Node node) {
		return goalTester.isGoal(node);
	}
	
	public GoalTester<Node> getGoalTester() {
		return goalTester;
	}
}
