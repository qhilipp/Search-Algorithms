package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

/**
 * always gives a the same cost for every path
 * @param <Node>
 */
public class ConstantPathEvaluator<Node> implements PathEvaluator<Node> {

	private double constant = 0;
	
	/**
	 * uses a path cost of zero
	 */
	public ConstantPathEvaluator() {}
	
	/**
	 * specify the path cost
	 * @param 	constant	the path cost
	 */
	public ConstantPathEvaluator(double constant) {
		this.constant = constant;
	}
	
	@Override
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path) {
		return constant;
	}

}
