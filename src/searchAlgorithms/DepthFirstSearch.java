package searchAlgorithms;

import java.util.ArrayList;

import searchStrategie.DepthFirst;
import stateSpace.StateSpace;

public class DepthFirstSearch<Node> extends GeneralSearch<Node> {

	public DepthFirstSearch(StateSpace<Node> space) {
		super(space, new DepthFirst<ArrayList<Node>>());
	}

}
