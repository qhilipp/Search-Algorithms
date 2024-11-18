package heuristic;

public class ConstantHeuristic<Node> implements Heuristic<Node> {

	double constant = 0;
	
	public ConstantHeuristic() {}
	
	public ConstantHeuristic(double constant) {
		this.constant = constant;
	}
	
	@Override
	public double futureCost(Node node) {
		return constant;
	}

}
