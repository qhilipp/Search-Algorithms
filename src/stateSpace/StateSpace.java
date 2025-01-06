package stateSpace;

import java.util.ArrayList;
import java.util.HashSet;

import goalTest.GoalTester;

/**
 * the environment that the search algorithms search in
 * it consists of a start node, a goal tester and a getNeighbors and getCost method
 * @param	<Node>	the node type
 */
public abstract class StateSpace<Node> {
	
	private GoalTester<Node> goalTester;
	private Node start;
	
	/**
	 * @param	start		the starting node
	 * @param	goalTester	the goal test interface
	 */
	public StateSpace(Node start, GoalTester<Node> goalTester) {
		this.start = start;
		this.goalTester = goalTester;
	}
	
	/**
	 * describes what nodes can be directly reached from the given node
	 * @param	node	the node from which the neighbors are being evaluated
	 * @return	a list of all directly neighboring nodes
	 */
	public abstract ArrayList<Node> getNeighbors(Node node);
	
	/**
	 * @param	from	the node from which you travel from
	 * @param 	to		the node to which you travel
	 * @return	the cost of traveling from the first to the second node
	 */
	public abstract double getCost(Node from, Node to);
	
	/**
	 * @return	the node from which the search algorithms start
	 */
	public Node getStart() {
		return start;
	}
	
	/**
	 * sets the starting node
	 * @param 	start	the node from which the search algorithms start
	 */
	public void setStart(Node start) {
		this.start = start;
	}
	
	/**
	 * @param 	node	the node that is being tested
	 * @return	whether or not the given node is a goal node
	 */
	public boolean isGoal(Node node) {
		return goalTester.isGoal(node);
	}
	
	/**
	 * @return	the goal tester
	 */
	public GoalTester<Node> getGoalTester() {
		return goalTester;
	}
	
	/**
	 * this method only returns all nodes if the StateSpace is finite, otherwise, the first 10000 nodes are being returned
	 * @return	all nodes contained in the StateSpace
	 */
	public ArrayList<Node> getNodes() {
		HashSet<Node> nodes = new HashSet<>();
		nodes.add(getStart());
		boolean change = true;
		int iterations = 0;
		while(change) {
			int previousSize = nodes.size();
			HashSet<Node> newNodes = new HashSet<>();
			for(Node node : nodes) {
				newNodes.addAll(getNeighbors(node));
			}
			nodes.addAll(newNodes);			
			change = previousSize != nodes.size() && iterations++ < 10000;
		}
		return new ArrayList<>(nodes);
	}
}
