package heuristic;

import stateSpace.StateSpace;

@FunctionalInterface
public interface Heuristic<Node> {
	public double futureCost(StateSpace<Node> space, Node node);
}
