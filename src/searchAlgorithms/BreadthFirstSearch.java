package searchAlgorithms;

import heuristic.ConstantHeuristic;
import searchStrategy.Uniform;
import stateSpace.StateSpace;

public class BreadthFirstSearch<Node> extends GeneralSearch<Node> {

	public BreadthFirstSearch(StateSpace<Node> space) {
		super(space, new Uniform<>(), new ConstantHeuristic<>());
	}

}
