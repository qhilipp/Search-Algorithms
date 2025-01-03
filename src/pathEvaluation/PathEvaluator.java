package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

/**
 * an interface that the StateSpace uses to evaluate the cost of a path
 * @param <Node>
 */
@FunctionalInterface
public interface PathEvaluator<Node> {

	/**
	 * @param 	space	the StateSpace that the path is in
	 * @param 	path	the path that is being evaluated
	 * @return			the cost of the path
	 */
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path);
	
}
