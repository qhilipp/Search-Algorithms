package searchAlgorithms;

import heuristic.DistanceHeuristic;
import heuristic.Heuristic;
import pathEvaluation.SumPathEvaluator;
import searchStrategy.Alphabetical;
import searchStrategy.SearchStrategy;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

/**
 * chooses the node with the lowest combined past and future cost
 * @param <Node>	the node type that the StateSpace uses
 */
public class AStarSearch<Node> extends GeneralSearch<Node> {
	
	/**
	 * specify the heuristic
	 * @param 	space		the StateSpace that the algorithm searches in
	 * @param 	heuristic	the heuristic that the algorithm uses
	 */
	public AStarSearch(StateSpace<Node> space, Heuristic<Node> heuristic) {
		super(space, new SearchStrategy<>(new Uniform<>(), new Alphabetical<>()), new SumPathEvaluator<>(), heuristic);
	}
	
	/**
	 * uses a DistanceHeuristic
	 * @param 	<T>		the node type that the StateSpace uses
	 * @param 	space	the StateSpace that the algorithm searches in
	 * @return			the AStarSearch algorithm
	 */
	public static <T extends Position> AStarSearch<T> autoHeuristic(StateSpace<T> space) {
        return new AStarSearch<>(space, new DistanceHeuristic<>());
    }

}
