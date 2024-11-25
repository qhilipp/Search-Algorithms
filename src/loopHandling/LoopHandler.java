package loopHandling;

import java.util.ArrayList;
import java.util.HashMap;

public interface LoopHandler<Node> {

	public void initialize();
	public boolean shouldVisitNode(Node node, ArrayList<Node> path, double pathCost, HashMap<Node, Double> minCostToNode);
	
}
