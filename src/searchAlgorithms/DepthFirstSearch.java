package searchAlgorithms;

import heuristic.ConstantHeuristic;
import pathEvaluation.ConstantPathEvaluator;
import searchStrategy.Alphabetical;
import searchStrategy.DepthFirst;
import searchStrategy.SearchStrategy;
import stateSpace.StateSpace;

/**
 * chooses the node with the deepest depth
 * @param 	<Node>	the node type that the StateSpace uses
 */
public class DepthFirstSearch<Node> extends GeneralSearch<Node> {

	/**
	 * uses a SearchStrategy with DepthFirst and Alphabetical PathSorter, a ConstantPathEvaluator and a ConstantHeuristic
	 * @param 	space	the StateSpace that the algorithm searches in
	 */
	public DepthFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new DepthFirst<>(), new Alphabetical<>()), new ConstantPathEvaluator<>(), new ConstantHeuristic<>());
	}

}
