package searchAlgorithms;

import heuristic.ConstantHeuristic;
import pathEvaluation.DepthPathEvaluator;
import searchStrategy.Alphabetical;
import searchStrategy.SearchStrategy;
import searchStrategy.Uniform;
import stateSpace.StateSpace;

/**
 * chooses the node with the shallowest depth
 * @param 	<Node>	the node type that the StateSpace uses
 */
public class BreadthFirstSearch<Node> extends GeneralSearch<Node> {

	/**
	 * uses a SearchStrategy with Uniform and Alphabetical PathSorter, a DepthPathEvaluator and a ConstantHeuristic
	 * @param	space the StateSpace that the algorithm searches in
	 */
	public BreadthFirstSearch(StateSpace<Node> space) {
		super(space, new SearchStrategy<>(new Uniform<>(), new Alphabetical<>()), new DepthPathEvaluator<>(), new ConstantHeuristic<>());
	}

}
