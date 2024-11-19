package pathEvaluation;

import java.util.ArrayList;

import stateSpace.StateSpace;

public class ConstantPathEvaluator<Node> implements PathEvaluator<Node> {

	private double constant = 0;
	
	public ConstantPathEvaluator() {}
	
	public ConstantPathEvaluator(double constant) {
		this.constant = constant;
	}
	
	@Override
	public double pastCost(StateSpace<Node> space, ArrayList<Node> path) {
		return constant;
	}

}
