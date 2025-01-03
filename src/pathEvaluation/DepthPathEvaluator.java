package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

/**
 * uses the length of the path for its cost
 * @param <Node>
 */
public class DepthPathEvaluator<Node> implements PathEvaluator<Node> {

	@Override
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path) {
		return Math.max(0, path.size() - 1);
	}

}
