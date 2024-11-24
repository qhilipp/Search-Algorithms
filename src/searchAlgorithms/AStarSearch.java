package searchAlgorithms;

import heuristic.DistanceHeuristic;
import heuristic.Heuristic;
import pathEvaluation.SumPathEvaluator;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

public class AStarSearch<Node extends Position> extends GeneralSearch<Node> {

	public AStarSearch(StateSpace<Node> space) {
		super(space, new Uniform<>(), new SumPathEvaluator<>(), new DistanceHeuristic<>());
	}
	
	public AStarSearch(StateSpace<Node> space, Heuristic<Node> heuristic) {
		super(space, new Uniform<>(), new SumPathEvaluator<>(), heuristic);
	}

}
