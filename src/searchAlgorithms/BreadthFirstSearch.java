package searchAlgorithms;

import heuristic.ConstantHeuristic;
import pathEvaluation.DepthPathEvaluator;
import searchStrategy.SearchStrategy;
import searchStrategy.Uniform;
import stateSpace.StateSpace;

public class BreadthFirstSearch<Node> extends GeneralSearch<Node> {

	public BreadthFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new Uniform<>()), new DepthPathEvaluator<>(), new ConstantHeuristic<>());
	}

}
