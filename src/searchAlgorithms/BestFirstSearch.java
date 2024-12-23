package searchAlgorithms;

import heuristic.DistanceHeuristic;
import pathEvaluation.ConstantPathEvaluator;
import searchStrategy.SearchStrategy;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

public class BestFirstSearch<Node extends Position> extends GeneralSearch<Node> {

	public BestFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new Uniform<>()), new ConstantPathEvaluator<>(), new DistanceHeuristic<>());
	}

}
