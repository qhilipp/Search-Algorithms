package searchAlgorithms;

import heuristic.DistanceHeuristic;
import searchStrategy.Uniform;
import stateSpace.StateSpace;
import util.Position;

public class AStarSearch<Node extends Position> extends GeneralSearch<Node> {

	public AStarSearch(StateSpace<Node> space) {
		super(space, new Uniform<>(), new DistanceHeuristic<>());
	}

}
