package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

@FunctionalInterface
public interface PathEvaluator<Node> {

	public double pastCost(StateSpace<Node> space, ArrayList<Node> path);
	
}
