package searchAlgorithms;

import heuristic.DistanceHeuristic;
import pathEvaluation.ConstantPathEvaluator;
import searchStrategy.Alphabetical;
import searchStrategy.SearchStrategy;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

/**
 * chooses the node with the closest distance to a goal node
 * @param 	<Node>	the node type that the StateSpace uses
 */
public class BestFirstSearch<Node extends Position> extends GeneralSearch<Node> {

	/**
	 * uses a SearchStrategy with Uniform PathSorter, a ConstantPathEvaluator and a DistanceHeuristic
	 * @param 	space	the StateSpace that the algorithm searches in
	 */
	public BestFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new Uniform<>(), new Alphabetical<>()), new ConstantPathEvaluator<>(), new DistanceHeuristic<>());
	}

}
