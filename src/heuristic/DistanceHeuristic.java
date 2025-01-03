package heuristic;

import java.util.ArrayList;
import java.util.Arrays;

import goalTest.ListGoalTester;
import stateSpace.StateSpace;
import util.Measurement;
import util.Position;

/**
 * a Heuristic that estimates the future cost by the distance to the closest goal node. It requires the StateSpace to have a ListGoalTester
 * @param <Node>
 */
public class DistanceHeuristic<Node extends Position> implements Heuristic<Node> {

	private Measurement measurement = Measurement.EUCLIDEAN;
	
	/**
	 * uses Measurement.EUCLIDEAN as a measurement
	 */
	public DistanceHeuristic() {}
	
	/**
	 * @param 	measurement	the measurement that is being used
	 */
	public DistanceHeuristic(Measurement measurement) {
		this.measurement = measurement;
	}
	
	@Override
	public double futureCost(StateSpace<Node> space, Node node) {
		double minDistance = Double.MAX_VALUE;
		ArrayList<Node> goals = new ArrayList<>();
		
		if(space.getGoalTester() instanceof ListGoalTester) {
			goals = ((ListGoalTester<Node>) space.getGoalTester()).getGoals();
		}
		
		for(Node goal : goals) minDistance = Math.min(minDistance, node.getPosition().distance(goal.getPosition(), measurement));
		return minDistance;
	}

}
