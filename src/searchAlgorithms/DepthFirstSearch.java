package searchAlgorithms;

import heuristic.ConstantHeuristic;
import pathEvaluation.ConstantPathEvaluator;
import searchStrategy.DepthFirst;
import searchStrategy.SearchStrategy;
import stateSpace.StateSpace;

public class DepthFirstSearch<Node> extends GeneralSearch<Node> {

	public DepthFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new DepthFirst<>()), new ConstantPathEvaluator<>(), new ConstantHeuristic<>());
	}

}
