package searchAlgorithms;

import heuristic.DistanceHeuristic;
import heuristic.Heuristic;
import pathEvaluation.SumPathEvaluator;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

public class AStarSearch<Node> extends GeneralSearch<Node> {

	public AStarSearch(StateSpace<Node> space, Heuristic<Node> heuristic) {
		super(space, new Uniform<>(), new SumPathEvaluator<>(), heuristic);
	}
	
	public static <T extends Position> AStarSearch<T> autoHeuristic(StateSpace<T> space) {
        return new AStarSearch<>(space, new DistanceHeuristic<>());
    }

}
