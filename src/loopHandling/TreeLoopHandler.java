package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

public class TreeLoopHandler<Node> implements LoopHandler<Node> {

	@Override
	public void initialize() {}

	@Override
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode) {
		return true;
	}

}
