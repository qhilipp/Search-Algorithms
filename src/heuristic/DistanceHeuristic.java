package heuristic;

import java.util.ArrayList;
import java.util.Arrays;

import goalTest.ListGoalTester;
import stateSpace.StateSpace;
import util.Measurement;
import util.Position;

public class DistanceHeuristic<P extends Position> implements Heuristic<P> {

	private Measurement measurement = Measurement.EUCLIDEAN;
	private ArrayList<P> goals;
	
	public DistanceHeuristic(P...goals) {
		this.goals = new ArrayList<>(Arrays.asList(goals));
	}
	
	public DistanceHeuristic(ArrayList<P> goals) {
		this.goals = goals;
	}
	
	public DistanceHeuristic(Measurement measurement, ArrayList<P> goals) {
		this.measurement = measurement;
		this.goals = goals;
	}
	
	@Override
	public double futureCost(StateSpace<P> space, P node) {
		double minDistance = Double.MAX_VALUE;
		if(goals.isEmpty()) {
			findGoals(space);
		}
		for(P goal : goals) minDistance = Math.min(minDistance, node.getPosition().distance(goal.getPosition(), measurement));
		return minDistance;
	}
	
	private void findGoals(StateSpace<P> space) {
		if(space.getGoalTester() instanceof ListGoalTester) {
			goals = ((ListGoalTester<P>) space.getGoalTester()).getGoals();
		} else {
			// TODO: Find goals in space
		}
	}

}
