package searchAlgorithms;

import heuristic.DistanceHeuristic;
import pathEvaluation.SumPathEvaluator;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

public class AStarSearch<Node extends Position> extends GeneralSearch<Node> {

	public AStarSearch(StateSpace<Node> space) {
		super(space, new Uniform<>(), new SumPathEvaluator<>(), new DistanceHeuristic<>());
	}

}
