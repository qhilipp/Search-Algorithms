package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

public class DepthPathEvaluator<Node> implements PathEvaluator<Node> {

	@Override
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path) {
		return Math.max(0, path.size() - 1);
	}

}
