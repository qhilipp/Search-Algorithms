package searchAlgorithms;

import heuristic.ConstantHeuristic;
import pathEvaluation.ConstantPathEvaluator;
import searchStrategy.DepthFirst;
import stateSpace.StateSpace;

public class DepthFirstSearch<Node> extends GeneralSearch<Node> {

	public DepthFirstSearch(StateSpace<Node> space) {
		super(space, new DepthFirst<>(), new ConstantPathEvaluator<>(), new ConstantHeuristic<>());
	}

}
