package heuristic;

import java.util.ArrayList;
import java.util.Arrays;

import util.Measurement;
import util.Vector;

public class DistanceHeuristic implements Heuristic<Vector> {

	private Measurement measurement = Measurement.EUCLIDEAN;
	private ArrayList<Vector> goals;
	
	public DistanceHeuristic(Vector...goals) {
		this.goals = new ArrayList<>(Arrays.asList(goals));
	}
	
	public DistanceHeuristic(ArrayList<Vector> goals) {
		this.goals = goals;
	}
	
	public DistanceHeuristic(Measurement measurement, ArrayList<Vector> goals) {
		this.measurement = measurement;
		this.goals = goals;
	}
	
	@Override
	public double futureCost(Vector node) {
		double minDistance = Double.MAX_VALUE;
		for(Vector goal : goals) minDistance = Math.min(minDistance, node.distance(goal, measurement));
		return minDistance;
	}

}
