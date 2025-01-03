package searchAlgorithms;

import heuristic.ConstantHeuristic;
import stateSpace.StateSpace;

/**
 * chooses the node with the lowest past cost
 * @param <Node>
 */
public class UniformSearch<Node> extends AStarSearch<Node> {

	/**
	 * uses AStarSearch with a ConstantHeuristic
	 * @param space
	 */
	public UniformSearch(StateSpace<Node> space) {
		super(space, new ConstantHeuristic<>());
	}

}
