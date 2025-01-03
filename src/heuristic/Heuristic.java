package heuristic;

import stateSpace.StateSpace;

/**
 * an interface that the StateSpace uses to estimate the future cost for a node within a given StateSpace
 * @param <Node>
 */
@FunctionalInterface
public interface Heuristic<Node> {
	
	/**
	 * 
	 * @param 	space	the StateSpace that the node is in
	 * @param 	node	the node that is being evaluated for its future cost
	 * @return			the future cost of that node
	 */
	public double futureCost(StateSpace<Node> space, Node node);
}
