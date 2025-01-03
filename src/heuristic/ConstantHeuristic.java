package heuristic;

import stateSpace.StateSpace;

/**
 * a Heuristic that always gives the same future cost for all nodes
 * @param <Node>
 */
public class ConstantHeuristic<Node> implements Heuristic<Node> {

	double constant = 0;
	
	/**
	 * uses a future cost of zero
	 */
	public ConstantHeuristic() {}
	
	/**
	 * @param 	constant	the future cost that is being returned for all nodes
	 */
	public ConstantHeuristic(double constant) {
		this.constant = constant;
	}
	
	@Override
	public double futureCost(StateSpace<Node> space, Node node) {
		return constant;
	}

}
