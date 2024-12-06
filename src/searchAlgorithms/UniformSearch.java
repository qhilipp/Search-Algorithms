package searchAlgorithms;

import heuristic.ConstantHeuristic;
import stateSpace.StateSpace;

public class UniformSearch<Node> extends AStarSearch<Node> {

	public UniformSearch(StateSpace<Node> space) {
		super(space, new ConstantHeuristic<>());
	}

}
