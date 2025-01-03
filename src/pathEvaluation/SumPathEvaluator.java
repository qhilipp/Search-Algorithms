package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

/**
 * sums the cost from every node to its successor in the path
 * @param 	<Node>	the node type that the StateSpace uses
 */
public class SumPathEvaluator<Node> implements PathEvaluator<Node> {

	@Override
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path) {
		double cost = 0;
		for(int i = 0; i < path.size() - 1; i++) {
			cost += space.getCost(path.get(i), path.get(i + 1));
		}
		return cost;
	}

}
